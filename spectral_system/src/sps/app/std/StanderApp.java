/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.std;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import nahon.comm.exl2.XlsSheetReader;
import sps.dev.data.SSpectralDataPacket;
import nahon.comm.exl2.XlsSheetWriter;
import nahon.comm.exl2.xlsTable_R;
import nahon.comm.exl2.xlsTable_W;
import nahon.comm.faultsystem.LogCenter;
import nahon.comm.file2.FileReader2;
import nahon.comm.file2.FileWriter2;
import nahon.comm.file2.fileTable_W;
import sps.app.common.AppManager;
import sps.app.common.CTestApp;
import sps.control.manager.ISpDevice;
import sps.platform.SpectralPlatService;
import sps.platform.SystemConfig;

/**
 *
 * @author Administrator
 */
public class StanderApp extends CTestApp {

    public StanderApp(AppManager parent) {
        super(parent);
    }

    // <editor-fold defaultstate="collapsed" desc="snapshot操作">   
    private final ArrayList<SSpectralDataPacket> snapshots = new ArrayList();

    private final int MaxSnapShot = 10;

    @Override
    public void AddSnapShot() {
        controllock.lock();
        try {
            if (this.currentdata != null) {
                if (this.snapshots.size() < MaxSnapShot) {
                    this.snapshots.add(currentdata);
                }
            }
        } finally {
            controllock.unlock();
        }
    }

    @Override
    public void DelSnapShot() {
        if (!this.snapshots.isEmpty()) {
            this.snapshots.remove(this.snapshots.size() - 1);
        }
    }

    @Override
    public SSpectralDataPacket[] GetSnapShots() {
        return this.snapshots.toArray(new SSpectralDataPacket[0]);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="保存EXCEL"> 
    private static final String TABLEINFO = "STDTABLE";

    @Override
    public void SaveToExcel(String filepath) throws Exception {
        controllock.lock();
        try (XlsSheetWriter sheet = XlsSheetWriter.CreateSheet(filepath, "数值")) {

            //记录描述信息
            xlsTable_W info_table = sheet.CreateNewTable(TABLEINFO, 2,
                    "类型",
                    "数量");
            info_table.WriterLine("当前光谱", this.currentdata == null ? 0 : 1);
            info_table.WriterLine("副本", this.snapshots.size());
            info_table.Finish();

            //记录主数据
            if (this.currentdata != null) {
                int table_len = currentdata.data.datavalue.length;
                xlsTable_W table = sheet.CreateNewTable("当前光谱", table_len,
                        "波长(nm)", "测量值");
                for (int i = 0; i < table_len; i++) {
                    table.WriterLine(currentdata.data.waveIndex[i], currentdata.data.datavalue[i]);
                }
                table.Finish();
            }

            //记录snapshot数据
            for (int i = 0; i < this.snapshots.size(); i++) {
                int table_len = this.snapshots.get(i).data.datavalue.length;
                xlsTable_W table = sheet.CreateNewTable("副本" + "-" + i, table_len, XlsSheetWriter.DirecTion.Horizontal,
                        "波长(nm)", "测量值");
                for (int j = 0; j < table_len; j++) {
                    table.WriterLine(this.snapshots.get(i).data.waveIndex[j], this.snapshots.get(i).data.datavalue[j]);
                }
                table.Finish();
            }
        } finally {
            controllock.unlock();
        }
    }

    @Override
    public void ReadExcel(String filepath) throws Exception {
        controllock.lock();
        try (XlsSheetReader sheet = XlsSheetReader.XlsSheetReader(filepath)) {
            xlsTable_R info_table = sheet.FindeNextTable();
            if (!info_table.table_name.contentEquals(TABLEINFO)) {
                throw new Exception("不匹配的数据");
            }

            //读取信息
            String scurrent_data_num = info_table.rows.get(0)[1];
            String snapshot_data_num = info_table.rows.get(1)[1];

            int i_cdata_num = Double.valueOf(scurrent_data_num).intValue();
            int i_sndata_num = Double.valueOf(snapshot_data_num).intValue();

            //读取主光谱
            if (i_cdata_num != 0) {
                xlsTable_R cdata_table = sheet.FindeNextTable();
                double[] wave = new double[cdata_table.rows.size()];
                double[] value = new double[cdata_table.rows.size()];
                for (int i = 0; i < wave.length; i++) {
                    wave[i] = Double.valueOf(cdata_table.rows.get(i)[0]);
                    value[i] = Double.valueOf(cdata_table.rows.get(i)[1]);
                }
                this.SetcurrentData(new SSpectralDataPacket(wave, value));
            }

            //读取snapshot
            this.snapshots.clear();
            for (int sn_index = 0; sn_index < i_sndata_num; sn_index++) {
                xlsTable_R sndata_table = sheet.FindeNextTable();
                double[] wave = new double[sndata_table.rows.size()];
                double[] value = new double[sndata_table.rows.size()];
                for (int i = 0; i < wave.length; i++) {
                    wave[i] = Double.valueOf(sndata_table.rows.get(i)[0]);
                    value[i] = Double.valueOf(sndata_table.rows.get(i)[1]);
                }
                this.snapshots.add(new SSpectralDataPacket(wave, value));
            }
        } finally {
            controllock.unlock();
        }
    }
    // </editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="数据处理">  
    private final ReentrantLock controllock = new ReentrantLock();
    private SSpectralDataPacket currentdata;

    private void SetcurrentData(SSpectralDataPacket spdata) {
        this.currentdata = spdata;
        UpdateWatchNode(spdata);
        this.TESTEVENT_CENTER.CreateEvent(TESTDATA, this.currentdata);
    }

    @Override
    protected void DataModify(SSpectralDataPacket spdata) {
        controllock.lock();
        try {
            for (int i = 0; i < spdata.data.datavalue.length; i++) {
                spdata.data.datavalue[i] = new BigDecimal(spdata.data.datavalue[i]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            SetcurrentData(spdata);
//            return spdata;
        } finally {
            controllock.unlock();
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="额外操作">  
    //计算RMS
    public double GetRMS() {
        if (this.currentdata != null) {
            double dataRMS = 0.0f;
            double dataAve = 0.0f;

            int length = currentdata.data.datavalue.length;

            for (int i = 0; i < length; i++) {
                dataAve += currentdata.data.datavalue[i];
            }

            dataAve = dataAve / length;

            for (int i = 0; i < length; i++) {
                dataRMS += (currentdata.data.datavalue[i] - dataAve) * (currentdata.data.datavalue[i] - dataAve) / length;
            }

            dataRMS = Math.sqrt(dataRMS);
            return dataRMS;
        }
        return Double.NaN;
    }

    // <editor-fold defaultstate="collapsed" desc="添加观察点">  
    private ArrayList<WatchNode> nodes = null;
    public static String NODEFILE = "/node.cfg";
    public static String TableName = "NODETABLE";

    public ArrayList<WatchNode> GetWatchNode() {
        this.InitWatchNode();
        return this.nodes;
    }

    private void InitWatchNode() {
        if (nodes == null) {
            nodes = new ArrayList();

            try (FileReader2 file_reader = FileReader2.OpenFile(SystemConfig.ConfigDir + NODEFILE)) {
                xlsTable_R table = file_reader.FindeNextTable();
                if (table.table_name.contains(TableName) && table.column_names.length == 3) {
                    table.rows.forEach(node_infos -> {
                        String wave = node_infos[0];
                        double min = Double.valueOf(node_infos[1]);
                        double max = Double.valueOf(node_infos[2]);

                        //峰值点波长保存为MAX
                        if (wave.contentEquals(MAXNode.MaxFlag)) {
                            nodes.add(WatchNode.MAXNODE);
                            WatchNode.MAXNODE.range_min = min;
                            WatchNode.MAXNODE.range_max = max;
                        } else {
                            //添加点
                            nodes.add(new WatchNode(Double.valueOf(wave), min, max));
                        }
                    });
                }
            } catch (Exception ex) {
                LogCenter.Instance().PrintLog(Level.INFO, "没有找到Node配置文件", ex);
            }
        }
    }

    private void SaveWatchNode() {
        try (FileWriter2 f_writer = FileWriter2.OpenFile(SystemConfig.ConfigDir + NODEFILE)) {
            fileTable_W table = f_writer.CreateTable(TableName, nodes.size(), WatchNode.ColumNames);
            for (WatchNode node : this.nodes) {
                if (node != WatchNode.MAXNODE) {
                    table.AddRow(node.node_wave, node.range_min, node.range_max);
                } else {
                    table.AddRow(MAXNode.MaxFlag, node.range_min, node.range_max);
                }
            }
            table.Finish();
        } catch (Exception ex) {
            LogCenter.Instance().PrintLog(Level.INFO, "保存Node配置文件失败", ex);
        }
    }

    public void AddWatchNode(ArrayList<WatchNode> nodes) {
        this.controllock.lock();
        try {
            this.InitWatchNode();
            this.nodes = nodes;
            this.SaveWatchNode();
        } finally {
            this.controllock.unlock();
        }
    }

    public void UpdateWatchNode(SSpectralDataPacket data) {
        this.controllock.lock();
        try {
            this.InitWatchNode();
            nodes.forEach((node) -> {
                node.UpdateValue(data);
            });
        } finally {
            this.controllock.unlock();
        }
    }
    // </editor-fold> 
    // </editor-fold> 

    public void StartOSTest() {
        this.parent.RunCommand(() -> {
            try (ISpDevice dev = SpectralPlatService.GetInstance().GetSingleDevManager().GetSelectDev()) {
                //申请设备控制权
                dev.Open();

                //默认10次平均，10ms积分时间
                parent.TestConfig.collect_par.averageTime = 10;
                parent.TestConfig.collect_par.integralTime = 10;
                //设置测试条件
                dev.SetCollectPar(parent.TestConfig.collect_par);                               
                dev.SetCollectConfig(parent.TestConfig.collect_config);
                //计时
                parent.TimeFlag.SetTimeFlag();
                //关灯
                dev.EnableExtern(false);
                //按电流采集
                SSpectralDataPacket dk_data = dev.DKModify();
                //显示按电流
                TESTEVENT_CENTER.CreateEvent(DKTEST, dk_data);
                //使能按电流
                parent.TestConfig.collect_config.dk_enable = true;
                //开灯
                dev.EnableExtern(true);
                parent.TestConfig.collect_config.light_switch = true;
                //采集数据
                SSpectralDataPacket test_data = dev.CollectData();
                parent.TimeFlag.StopTime();

                //刷新界面
                parent.TestConfig.ConfigUpdateEvent.CreateEvent(null);
                //显示数据
                DataModify(test_data);
            } catch (Exception ex) {
                LogCenter.Instance().SendFaultReport(Level.SEVERE, "采集异常:", ex);
            }
        });

    }
}
