/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.os;

import java.util.logging.Level;
import sps.dev.data.SSpectralDataPacket;
import nahon.comm.faultsystem.LogCenter;
import sps.app.common.AppManager;
import static sps.app.common.CTestApp.DKTEST;
import sps.app.std.StanderApp;
import sps.control.manager.ISpDevice;
import sps.control.manager.SpDevManager;

/**
 *
 * @author Administrator
 */
public class OSApp extends StanderApp {

    public void StartOSTest() {
        AppManager.R().RunCommand(() -> {
            try (ISpDevice dev = SpDevManager.R().GetSelectDev()) {
                //申请设备控制权
                dev.Open();

                //默认10次平均，10ms积分时间
                SpDevManager.R().TestConfig.collect_par.averageTime = 10;
                SpDevManager.R().TestConfig.collect_par.integralTime = 10;
                //设置测试条件
                dev.SetCollectPar(SpDevManager.R().TestConfig.collect_par);
                dev.SetCollectConfig(SpDevManager.R().TestConfig.collect_config);
                //计时
                AppManager.R().TimeFlag.SetTimeFlag();
                //关灯
                dev.EnableExtern(false);
                //按电流采集
                SSpectralDataPacket dk_data = dev.DKModify();
                //显示按电流
                TESTEVENT_CENTER.CreateEvent(DKTEST, dk_data);
                //使能按电流
                SpDevManager.R().TestConfig.collect_config.dk_enable = true;
                //开灯
                dev.EnableExtern(true);
                SpDevManager.R().TestConfig.collect_config.light_switch = true;
                //采集数据
                SSpectralDataPacket test_data = dev.CollectData();
                AppManager.R().TimeFlag.StopTime();

                //刷新界面
                SpDevManager.R().TestConfig.ConfigUpdateEvent.CreateEvent(null);
                //显示数据
                DataModify(test_data);
            } catch (Exception ex) {
                LogCenter.Instance().SendFaultReport(Level.SEVERE, "采集异常:", ex);
            }
        });

    }

}
