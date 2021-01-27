/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.common;

import java.util.logging.Level;
import nahon.comm.event.NEventCenter;
import nahon.comm.faultsystem.LogCenter;
import sps.app.absorb.AbsApp;
import sps.app.std.StanderApp;
import sps.app.transmit.TrsApp;
import sps.platform.SpectralPlatService;

/**
 *
 * @author Administrator
 */
public class AppManager {

    // <editor-fold defaultstate="collapsed" desc="app列表">  
    // <editor-fold defaultstate="collapsed" desc="Stander App">     
    private StanderApp comm_app;

    public StanderApp GetCommonApp() {
        if (this.comm_app == null) {
            comm_app = new StanderApp(this);
        }
        return this.comm_app;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="Absorber App">     
    private AbsApp abs_app;

    public AbsApp GetAbsApp() {
        if (this.abs_app == null) {
            abs_app = new AbsApp(this);
        }
        return this.abs_app;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="Transmist App">     
    private TrsApp trs_app;

    public TrsApp GetTrsApp() {
        if (this.trs_app == null) {
            trs_app = new TrsApp(this);
        }
        return this.trs_app;
    }
    // </editor-fold> 

    public CTestApp GetCurrentApp() {
        return currentApp;
    }

    private CTestApp currentApp = this.GetCommonApp();

    public void SwitchApp(String AppFlag) {
        if (AppFlag.contentEquals(StanderApp.class.getSimpleName())) {
            this.currentApp = this.GetCommonApp();
            return;
        }

        if (AppFlag.contentEquals(AbsApp.class.getSimpleName())) {
            this.currentApp = this.GetAbsApp();
            return;
        }
        
        if (AppFlag.contentEquals(TrsApp.class.getSimpleName())) {
            this.currentApp = this.GetTrsApp();
            return;
        }

        LogCenter.Instance().SendFaultReport(Level.SEVERE, "未知模块" + AppFlag);
        this.currentApp = this.GetCommonApp();
    }
//    private AbsorbeApp abs_app = new AbsorbeApp();
//
//    public AbsorbeApp GetAbsorbeApp() {
//        return this.abs_app;
//    }
//
//    private ReflectApp ref_app = new ReflectApp();
//
//    public ReflectApp GetReflectApp() {
//        return this.ref_app;
//    }
    // </editor-fold> 

    public GlobalConfig TestConfig = new GlobalConfig();
    public TimeConsume TimeFlag = new TimeConsume();

    // <editor-fold defaultstate="collapsed" desc="执行测试">  
    private boolean is_running = false;

    public boolean IsRunning() {
        return this.is_running;
    }

    public void RunCommand(Runnable comd) {
        if (this.is_running) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, "系统正忙");
            return;
        }
        this.is_running = true;
        TestEvent.CreateEvent(is_running);
        //通知更新测试条件
//        this.TestConfig.ConfigUpdateEvent.CreateEvent(null);
        SpectralPlatService.GetInstance().GetThreadPools().submit(() -> {
            comd.run();
            is_running = false;
            TestEvent.CreateEvent(is_running);
        });
    }

    public NEventCenter<Boolean> TestEvent = new NEventCenter();
    // </editor-fold> 
}
