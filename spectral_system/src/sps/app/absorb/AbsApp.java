/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.absorb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import nahon.comm.exl2.XlsSheetReader;
import nahon.comm.exl2.XlsSheetWriter;
import nahon.comm.exl2.xlsTable_R;
import nahon.comm.exl2.xlsTable_W;
import nahon.comm.faultsystem.LogCenter;
import sps.app.common.AppManager;
import sps.app.common.CTestApp;
import static sps.app.common.CTestApp.TESTDATA;
import sps.control.manager.ISpDevice;
import sps.dev.data.SSpectralDataPacket;
import sps.platform.SpectralPlatService;

/**
 *
 * @author chejf
 */
public class AbsApp extends CTestApp {

    public AbsApp(AppManager parent) {
        super(parent);
    }

    // <editor-fold defaultstate="collapsed" desc="基本操作"> 
    // <editor-fold defaultstate="collapsed" desc="snapshot操作">   
    private final ArrayList<RateData> snapshots = new ArrayList();
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
    public RateData[] GetSnapShots() {
        return this.snapshots.toArray(new RateData[0]);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="保存EXCEL"> 
    private static final String TABLEINFO = "ABSTABLE";

    @Override
    public void SaveToExcel(String filepath) throws Exception {
        controllock.lock();
        try (XlsSheetWriter sheet = XlsSheetWriter.CreateSheet(filepath, "数据")) {
            //记录描述信息
            xlsTable_W info_table = sheet.CreateNewTable(TABLEINFO, 2,
                    "类型",
                    "数量");
            info_table.WriterLine("当前光谱", this.currentdata == null ? 0 : 1);
            info_table.WriterLine("副本", this.snapshots.size());
            info_table.Finish();

            if (this.currentdata != null) {
                int table_len = currentdata.Rate.length;
                xlsTable_W table = sheet.CreateNewTable("当前光谱", table_len, XlsSheetWriter.DirecTion.Horizontal,
                        "波长(nm)", "参考光", "测量光", "吸光度");
                for (int i = 0; i < table_len; i++) {
                    table.WriterLine(currentdata.basedata.data.waveIndex[i],
                            currentdata.basedata.data.datavalue[i],
                            currentdata.testdata.data.datavalue[i],
                            currentdata.Rate[i]);
                }
                table.Finish();
            }

            if (!this.snapshots.isEmpty()) {
                for (int i = 0; i < this.snapshots.size(); i++) {
                    int table_len = this.snapshots.get(i).Rate.length;
                    xlsTable_W table = sheet.CreateNewTable("副本" + "-" + i, table_len, XlsSheetWriter.DirecTion.Horizontal,
                            "波长(nm)", "参考光", "测量光", "吸光度");
                    for (int j = 0; j < table_len; j++) {
                        table.WriterLine(this.snapshots.get(i).basedata.data.waveIndex[j],
                                this.snapshots.get(i).basedata.data.datavalue[j],
                                this.snapshots.get(i).testdata.data.datavalue[j],
                                this.snapshots.get(i).Rate[j]);
                    }
                    table.Finish();
                }
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
                double[] value_ref = new double[cdata_table.rows.size()];
                double[] value_test = new double[cdata_table.rows.size()];
                double[] value_abs = new double[cdata_table.rows.size()];
                for (int i = 0; i < wave.length; i++) {
                    wave[i] = Double.valueOf(cdata_table.rows.get(i)[0]);
                    value_ref[i] = Double.valueOf(cdata_table.rows.get(i)[1]);
                    value_test[i] = Double.valueOf(cdata_table.rows.get(i)[2]);
                    value_abs[i] = Double.valueOf(cdata_table.rows.get(i)[3]);
                }

                this.base_data = new SSpectralDataPacket(wave, value_ref);
                this.currentdata = new RateData(base_data, new SSpectralDataPacket(wave, value_test), value_abs);
                this.TESTEVENT_CENTER.CreateEvent(TESTDATA, this.currentdata);
            }

            //读取snapshot
            this.snapshots.clear();
            for (int sn_index = 0; sn_index < i_sndata_num; sn_index++) {
                xlsTable_R sndata_table = sheet.FindeNextTable();
                double[] wave = new double[sndata_table.rows.size()];
                double[] value_ref = new double[sndata_table.rows.size()];
                double[] value_test = new double[sndata_table.rows.size()];
                double[] value_abs = new double[sndata_table.rows.size()];
                for (int i = 0; i < wave.length; i++) {
                    wave[i] = Double.valueOf(sndata_table.rows.get(i)[0]);
                    value_ref[i] = Double.valueOf(sndata_table.rows.get(i)[1]);
                    value_test[i] = Double.valueOf(sndata_table.rows.get(i)[2]);
                    value_abs[i] = Double.valueOf(sndata_table.rows.get(i)[3]);
                }
                this.snapshots.add(new RateData(new SSpectralDataPacket(wave, value_ref),
                        new SSpectralDataPacket(wave, value_test), value_abs));
            }
        } finally {
            controllock.unlock();
        }
    }
    // </editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="数据处理">  
    private final ReentrantLock controllock = new ReentrantLock();
    private RateData currentdata;

    @Override
    protected void DataModify(SSpectralDataPacket data) throws Exception {
        if (this.base_data == null) {
            throw new Exception("请先采集参考光");
        }

        double[] tmp = new double[data.data.datavalue.length];

        if (tmp.length != this.base_data.data.datavalue.length) {
            throw new Exception("参考光和测量光数据不匹配");
        }

         for (int i = 0; i < tmp.length; i++) {
            if (this.base_data.data.datavalue[i] <= 0 || this.base_data.data.datavalue[i] < data.data.datavalue[i]) {
                tmp[i] = 0;
            } else if (data.data.datavalue[i] <= 0) {
                try {
                    tmp[i] = Math.log10(this.base_data.data.datavalue[i] / 0.001d);
                } catch (Exception ex) {
                    tmp[i] = -1;
                }
            } else {
                try {
                    tmp[i] = Math.log10(this.base_data.data.datavalue[i] / data.data.datavalue[i]);
                } catch (Exception ex) {
                    tmp[i] = -1;
                }
//                    tmp[i] = tmp[i] > 100 ? 100 : tmp[i];                
            }
            tmp[i] = new BigDecimal(tmp[i]).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        this.currentdata = new RateData(this.base_data, data, tmp);
        this.TESTEVENT_CENTER.CreateEvent(TESTDATA, this.currentdata);
    }
    // </editor-fold>
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="采集参考光"> 
    public static int REFTEST = 0x11;
    private SSpectralDataPacket base_data = null;

    public void CollectReflectLight() {
        this.parent.RunCommand(() -> {
            parent.TimeFlag.SetTimeFlag();
            try (ISpDevice dev = SpectralPlatService.GetInstance().GetSingleDevManager().GetSelectDev()) {
                //申请设备控制权
                dev.Open();

                //设置测试条件
                dev.SetCollectPar(parent.TestConfig.collect_par);
                dev.SetCollectConfig(parent.TestConfig.collect_config);

                //采集数据
                SSpectralDataPacket collect_data = dev.CollectData();
                parent.TimeFlag.StopTime();

                //保存参考光
                this.base_data = collect_data;
                //采集参考光，删除所有旧数据
                this.currentdata = null;
                this.snapshots.clear();
                //显示数据
                TESTEVENT_CENTER.CreateEventAsync(REFTEST, collect_data);
            } catch (Exception ex) {
                LogCenter.Instance().SendFaultReport(Level.SEVERE, "采集异常:", ex);
            }
        });
    }
    // </editor-fold>

}
