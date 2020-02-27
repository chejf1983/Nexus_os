/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.common;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import nahon.comm.event.EventCenter;
import nahon.comm.faultsystem.LogCenter;
import sps.control.manager.ISpDevice;
import sps.dev.data.SSpectralDataPacket;
import sps.platform.SpectralPlatService;

/**
 *
 * @author chejf
 */
public abstract class CTestApp {

    protected AppManager parent;

    public CTestApp(AppManager parent) {
        this.parent = parent;
    }

    // <editor-fold defaultstate="collapsed" desc="设备操作列表">  
    public static int DKTEST = 0;
    public static int TESTDATA = 1;

    public EventCenter<Integer> TESTEVENT_CENTER = new EventCenter();
    protected boolean test_flag = false;

    // <editor-fold defaultstate="collapsed" desc="暗电流测试">  
    public void DKTest() {
        this.parent.RunCommand(() -> {
            try (ISpDevice dev = SpectralPlatService.GetInstance().GetSingleDevManager().GetSelectDev()) {
                //申请设备控制权
                dev.Open();

                //设置测试条件
                dev.SetCollectPar(this.parent.TestConfig.collect_par);

                this.parent.TimeFlag.SetTimeFlag();
                //采集暗电流
                SSpectralDataPacket dk_data = dev.DKModify();
                parent.TimeFlag.StopTime();

                //显示数据
                TESTEVENT_CENTER.CreateEvent(DKTEST, dk_data);
            } catch (Exception ex) {
                LogCenter.Instance().SendFaultReport(Level.SEVERE, "采集异常:", ex);
            }
        });
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="单次测试">  
    public void SingleTest() {
        this.parent.RunCommand(() -> {
            try (ISpDevice dev = SpectralPlatService.GetInstance().GetSingleDevManager().GetSelectDev()) {
                //申请设备控制权
                dev.Open();

                //设置测试条件
                dev.SetCollectPar(parent.TestConfig.collect_par);
                dev.SetCollectConfig(parent.TestConfig.collect_config);

                parent.TimeFlag.SetTimeFlag();
                //采集数据
                SSpectralDataPacket test_data = dev.CollectData();
                parent.TimeFlag.StopTime();

                //显示数据
                DataModify(test_data);
            } catch (Exception ex) {
                LogCenter.Instance().SendFaultReport(Level.SEVERE, "采集异常:", ex);
            }
        });

    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="连续测试">  
    public void SusTainTest() {
        this.parent.RunCommand(() -> {
            try (ISpDevice dev = SpectralPlatService.GetInstance().GetSingleDevManager().GetSelectDev()) {
                //申请设备控制权
                dev.Open();

                test_flag = true;

                //设置测试条件
                dev.SetCollectPar(this.parent.TestConfig.collect_par);
                dev.SetCollectConfig(this.parent.TestConfig.collect_config);

                while (test_flag) {
                    this.parent.TimeFlag.SetTimeFlag();
                    //采集数据
                    SSpectralDataPacket test_data = dev.CollectData();
                    parent.TimeFlag.StopTime();

                    //显示数据
                    DataModify(test_data);

                    TimeUnit.MILLISECONDS.sleep((long) this.parent.TestConfig.collect_par.interval_time);
                }
            } catch (Exception ex) {
                LogCenter.Instance().SendFaultReport(Level.SEVERE, "采集异常:", ex);
            } finally {
                test_flag = false;
            }
        });
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="停止测试">  
    public void StopTest() {
        this.test_flag = false;
    }
    // </editor-fold> 

    //数据加工
    protected abstract void DataModify(SSpectralDataPacket data) throws Exception;

    // <editor-fold defaultstate="collapsed" desc="开关灯">  
    public boolean EnableLight(boolean value) {
        try (ISpDevice dev = SpectralPlatService.GetInstance().GetSingleDevManager().GetSelectDev()) {
            //申请设备控制权
            dev.Open();

            //设置测试条件
            dev.EnableExtern(value);
            
            //更新标志
            parent.TestConfig.collect_config.light_switch = value;

            return true;
        } catch (Exception ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, "设置异常", ex);
            return false;
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="自动积分光谱仪时间">  
    public static int MaxValue = 65535;
    public static double PerfactMinRate = 0.8d;//0.763d;
    public static double PerfactMaxRate = 0.915d;
    public static int MaxIntegerTime = 10000;

    public void AutoTestTime() {
        this.parent.RunCommand(() -> {
            //显示光谱仪参数设置
            try (ISpDevice dev = SpectralPlatService.GetInstance().GetSingleDevManager().GetSelectDev()) {
                dev.Open();
                test_flag = true;
                GlobalConfig TestConfig = this.parent.TestConfig;
                dev.SetCollectConfig(TestConfig.collect_config);
                //从1ms开始寻找
                TestConfig.collect_par.integralTime = 1;
                while (test_flag) {
                    //设置积分时间
                    dev.SetCollectPar(TestConfig.collect_par);
                    //刷新界面
                    TestConfig.ConfigUpdateEvent.CreateEvent(null);
                    //采集光谱
                    SSpectralDataPacket data = dev.CollectData();

                    //显示数据
                    TESTEVENT_CENTER.CreateEventAsync(DKTEST, (data));
                    double rate = data.data.IP / MaxValue;
                    if (rate > PerfactMinRate && rate < PerfactMaxRate) {
                        break;
                    } else {
                        float last_time = TestConfig.collect_par.integralTime;
                        //超过95%折半减少
                        if (rate > 0.95) {
                            TestConfig.collect_par.integralTime = last_time / 2;
                        } else {
                            //其他按照比例放大缩小
                            TestConfig.collect_par.integralTime = (int) (TestConfig.collect_par.integralTime * (PerfactMaxRate + PerfactMinRate) / (2 * rate));
                            //如果计算结果超过最大积分时间，停止计算，保留最后一次积分时间
                            if (TestConfig.collect_par.integralTime > MaxIntegerTime) {
                                TestConfig.collect_par.integralTime = last_time;
                                throw new Exception("积分时间太大,超过:" + MaxIntegerTime);
                            }
                        }
                    }

                    TimeUnit.MILLISECONDS.sleep((long) 10);
                }
            } catch (Exception ex) {
                LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
            } finally {
                test_flag = false;
            }
        });
    }
    // </editor-fold> 
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="SnapShot操作"> 
    public abstract void AddSnapShot();

    public abstract void DelSnapShot();

    public abstract Object[] GetSnapShots();
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="保存文件"> 
    public abstract void SaveToExcel(String filepath) throws Exception;
    
    public abstract void ReadExcel(String filepath) throws Exception;
    // </editor-fold> 
}
