/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.test;

import nahon.comm.file2.FileWriter2;
import nahon.comm.file2.fileTable_W;
import nexus.app.os.UIOSApp;
import nexus.main.entry.StartFlash;
import sps.platform.SpectralPlatService;

/**
 *
 * @author chejf
 */
public class test {

    public static void main(String args[]) throws Exception {
        saveFile();
        /* Create and display the form */
        SpectralPlatService.GetInstance().GetConfig().setProperty("COM", "Y");
        SpectralPlatService.GetInstance().GetConfig().setProperty("INTDLL", "Y");
        java.awt.EventQueue.invokeLater(() -> {
            new StartFlash().setVisible(true);
        });
    }
    
    public static void saveFile() throws Exception {

        try (FileWriter2 fw = FileWriter2.OpenFile("./model.cfg")) {
            fileTable_W t1 = fw.CreateTable(UIOSApp.class.getSimpleName(), 3, "value");
            String projectname = System.getProperty("user.dir");
            String pn = projectname.substring(projectname.lastIndexOf('\\') + 1, projectname.length());
            t1.AddRow(pn);//添加项目名称
            t1.AddRow("气体检测");//添加中文名称
            t1.AddRow(UIOSApp.class.getName());
            t1.Finish();
        }
    }
}
