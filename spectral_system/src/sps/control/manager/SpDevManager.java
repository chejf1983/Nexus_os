/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.control.manager;

import java.util.ArrayList;
import java.util.logging.Level;
import nahon.comm.faultsystem.LogCenter;
import sps.platform.SpectralPlatService;

/**
 *
 * @author chejf
 */
public class SpDevManager {

    // <editor-fold defaultstate="collapsed" desc="搜索设备"> 
    private ArrayList<ISpDevice> devlist = new ArrayList();
    private static ISPDevSearch search_instance;

    public static void SetSPDevDriver(ISPDevSearch instance) {
        try {
            String need_clean = SpectralPlatService.GetInstance().GetConfig().getProperty("INTDLL", "Y");
            if (need_clean.contentEquals("Y")) {
                LogCenter.Instance().PrintLog(Level.INFO, instance.InitDriver(true));
                SpectralPlatService.GetInstance().GetConfig().setProperty("INTDLL", "N");
            } else {
                LogCenter.Instance().PrintLog(Level.INFO, instance.InitDriver(false));
            }
            search_instance = instance;
        } catch (Exception ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
        }
    }

    public boolean SearchDevice() {
        this.devlist.clear();

        if (search_instance == null) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, "没有加载驱动");
            return false;
        }

        String need_com = SpectralPlatService.GetInstance().GetConfig().getProperty("COM", "N");
        if (need_com.contentEquals("Y")) {
            for (ISpDevice dev : search_instance.SearchDeviceWithCom()) {

                try (ISpDevice tdev = dev) {
                    tdev.Open();
                    tdev.InitDevice();
                    this.devlist.add(tdev);
                } catch (Exception ex) {
                    LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
                }
            }
        } else {
            for (ISpDevice dev : search_instance.SearchDevice()) {
                try (ISpDevice tdev = dev) {
                    tdev.Open();
                    tdev.InitDevice();
                    this.devlist.add(tdev);
                } catch (Exception ex) {
                    LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
                }
            }
        }
        this.SetSelectIndex(-1);
        return true;
    }

    public ISpDevice[] GetDevList() {
        return this.devlist.toArray(new ISpDevice[0]);
    }

    // <editor-fold defaultstate="collapsed" desc="切换设备"> 
    private int selectindex = -1;

    public int GetSelectIndex() {
        return this.selectindex;
    }

    public void SetSelectIndex(int index) {
        this.selectindex = index;
    }

    public ISpDevice GetSelectDev() throws Exception {
        if (selectindex == -1 || selectindex >= this.devlist.size()) {
            throw new Exception("没有选中设备");
        }
        return this.devlist.get(selectindex);
    }
    // </editor-fold>
    // </editor-fold> 
}
