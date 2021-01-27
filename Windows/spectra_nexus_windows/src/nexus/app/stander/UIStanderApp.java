package nexus.app.stander;

import java.awt.CardLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import nahon.comm.event.NEvent;
import nahon.comm.event.NEventListener;
import nahon.comm.faultsystem.LogCenter;
import chart.data.CSPData;
import chart.spchart.panel.SpectralChartPane;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JLabel;
import nexus.main.compent.FileDialogHelp;
import nexus.main.entry.MainForm;
import table.std.SPDataTablePane;
import org.jfree.chart.ChartUtilities;
import sps.app.std.StanderApp;
import sps.app.std.WatchNode;
import sps.dev.data.SSpectralDataPacket;
import sps.platform.SpectralPlatService;
import table.data.TSPData;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jiche
 */
public class UIStanderApp extends javax.swing.JPanel {

    public UIStanderApp() {
        initComponents();

        //初始化表格和曲线
        this.InitTableAndChart();

        this.InitAppControl();

        this.InitLanguange();
    }

    // <editor-fold defaultstate="collapsed" desc="初始化表格和曲线"> 
    private SPDataTablePane dataTablePane = new SPDataTablePane();

    private SpectralChartPane dataChartPane = new SpectralChartPane();

//    private BaseChartTable baseChartandTable = new BaseChartTable();
    private JSplitPane chartAnddataSplit;

    //初始化表格界面
    private void InitTableAndChart() {
        //设置标题和主数据
        this.dataTablePane.SetTitle("光谱数据", "值");

        //设置最大最小范围
        this.dataChartPane.SetMaxRange(65535, -1000);

        //设置左右窗比例
        chartAnddataSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dataChartPane, null);
        chartAnddataSplit.setResizeWeight(1.0);
        /* 增加显示区域 */
        DisplayArea.setLayout(new CardLayout());
        DisplayArea.add(chartAnddataSplit);
    }

    //刷新表格
    private void UpdateTable(SSpectralDataPacket main) {
        this.dataTablePane.UpdateData(new TSPData(main.data.waveIndex, main.data.datavalue));
    }

    //刷新主曲线
    private void UpdateChart(SSpectralDataPacket main) {
        this.dataChartPane.DisplaySPData("main", new CSPData(main.data.waveIndex, main.data.datavalue));
    }

    //刷新快照
    private void UpdataChartSnapShort(SSpectralDataPacket[] sp) {
        String[] name = new String[sp.length];
        CSPData[] data = new CSPData[sp.length];
        for (int i = 0; i < name.length; i++) {
            name[i] = "SP-" + i;
            data[i] = new CSPData(sp[i].data.waveIndex, sp[i].data.datavalue);
        }
        this.dataChartPane.DisplaySnapShot(name, data);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="初始化语言"> 
    private void InitLanguange() {
        Button_SaveImage.setToolTipText("保存曲线图片");
        Button_SaveData.setToolTipText("导出Excel");
        Button_LoadData.setToolTipText("读取Excel数据");
        Button_AddSnapShot.setToolTipText("添加光谱副本");
        Button_DeletSnapShot.setToolTipText("删除光谱副本");
        ToggleButton_DataTable.setToolTipText("显示数据表格");

        ToggleButton_RMS.setToolTipText("显示RMS");
        Button_WatchNode.setToolTipText("增加观察点");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="初始化App控制"> 
    private StanderApp commapp;

    private void InitAppControl() {
        commapp = SpectralPlatService.GetInstance().GetAppManager().GetCommonApp();

        SpectralPlatService.GetInstance().GetAppManager().TestEvent.RegeditListener(new NEventListener<Boolean>() {
            @Override
            public void recevieEvent(NEvent<Boolean> event) {
                //更新控制面板使能状态
                ToggleButton_RMS.setEnabled(!event.GetEvent());
                Button_WatchNode.setEnabled(!event.GetEvent());
                Button_OSTest.setEnabled(!event.GetEvent());
            }
        });

        this.commapp.TESTEVENT_CENTER.RegeditListener(new NEventListener<Integer>() {
            @Override
            public void recevieEvent(NEvent<Integer> event) {
                if (event.GetEvent() == StanderApp.DKTEST
                        || event.GetEvent() == StanderApp.TESTDATA) {
                    DisplayData((SSpectralDataPacket) event.Info());
                }
            }
        });
    }

    private void DisplayData(SSpectralDataPacket data) {
        if (data != null) {
            /* display main data */
            this.UpdateTable(data);
            this.UpdateChart(data);
            this.DisplayRMS();
            this.DisplayWatchNode();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="RMS计算"> 
    private JLabel RMSLabel = null;

    private void DisplayRMS() {
        if (ToggleButton_RMS.isSelected()) {
            if (RMSLabel == null) {
                RMSLabel = this.dataChartPane.GetFreeTextPaint().CreateNewLable("");
            }
            RMSLabel.setText(String.format("RMS:%f", this.commapp.GetRMS()));
        } else {
            if (RMSLabel != null) {
                this.dataChartPane.GetFreeTextPaint().RemoveLable(RMSLabel);
                RMSLabel = null;
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WatchNode 显示"> 
    ArrayList<JLabel> watch_lable = new ArrayList();

    private void DisplayWatchNode() {
        //检查长度
        if (watch_lable.size() != this.commapp.GetWatchNode().size()) {
            watch_lable.forEach(label -> {
                this.dataChartPane.GetFreeTextPaint().RemoveLable(label);
            });
            watch_lable.clear();

            this.commapp.GetWatchNode().forEach((node) -> {
                watch_lable.add(this.dataChartPane.GetFreeTextPaint().CreateNewLable(""));
            });
        }

        //显示数据
        for (int i = 0; i < this.commapp.GetWatchNode().size(); i++) {
            WatchNode node = this.commapp.GetWatchNode().get(i);
            this.watch_lable.get(i).setText(node.toString());
            this.watch_lable.get(i).setForeground(node.IsValueInRange() ? Color.GREEN : Color.RED);
        }
    }
    // </editor-fold>
    // </editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DisplayArea = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        ToggleButton_DataTable = new javax.swing.JToggleButton();
        ToggleButton_RMS = new javax.swing.JToggleButton();
        Button_DeletSnapShot = new javax.swing.JButton();
        Button_AddSnapShot = new javax.swing.JButton();
        Button_SaveData = new javax.swing.JButton();
        Button_SaveImage = new javax.swing.JButton();
        Button_LoadData = new javax.swing.JButton();
        Button_WatchNode = new javax.swing.JButton();
        Button_OSTest = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(617, 406));

        DisplayArea.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout DisplayAreaLayout = new javax.swing.GroupLayout(DisplayArea);
        DisplayArea.setLayout(DisplayAreaLayout);
        DisplayAreaLayout.setHorizontalGroup(
            DisplayAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 613, Short.MAX_VALUE)
        );
        DisplayAreaLayout.setVerticalGroup(
            DisplayAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 346, Short.MAX_VALUE)
        );

        ToggleButton_DataTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/app/resources/datatable.png"))); // NOI18N
        ToggleButton_DataTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_DataTableActionPerformed(evt);
            }
        });

        ToggleButton_RMS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/app/resources/RMS.png"))); // NOI18N
        ToggleButton_RMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_RMSActionPerformed(evt);
            }
        });

        Button_DeletSnapShot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/app/resources/delete_snap.png"))); // NOI18N
        Button_DeletSnapShot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_DeletSnapShotActionPerformed(evt);
            }
        });

        Button_AddSnapShot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/app/resources/add_snap.png"))); // NOI18N
        Button_AddSnapShot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AddSnapShotActionPerformed(evt);
            }
        });

        Button_SaveData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/app/resources/xls_save.png"))); // NOI18N
        Button_SaveData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SaveDataActionPerformed(evt);
            }
        });

        Button_SaveImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/app/resources/bmp_save.png"))); // NOI18N
        Button_SaveImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SaveImageActionPerformed(evt);
            }
        });

        Button_LoadData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/app/resources/excel_32px_load.png"))); // NOI18N
        Button_LoadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_LoadDataActionPerformed(evt);
            }
        });

        Button_WatchNode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/app/resources/watch_32.png"))); // NOI18N
        Button_WatchNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_WatchNodeActionPerformed(evt);
            }
        });

        Button_OSTest.setText("检测");
        Button_OSTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_OSTestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(Button_SaveImage, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_SaveData, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_LoadData, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_AddSnapShot, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_DeletSnapShot, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(ToggleButton_DataTable, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(ToggleButton_RMS, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_WatchNode, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_OSTest)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_OSTest, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_WatchNode, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_LoadData, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_SaveImage, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_SaveData, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_AddSnapShot, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_DeletSnapShot, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_DataTable, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_RMS, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DisplayArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(DisplayArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="按钮"> 
    private void Button_SaveImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SaveImageActionPerformed
        try {
            File file = FileDialogHelp.GetFilePath(".png");
            if (file != null) {
                try (FileOutputStream bout = new java.io.FileOutputStream(file)) {
                    ChartUtilities.writeBufferedImageAsPNG(bout, this.dataChartPane.GetChartPanePNG());
                    bout.flush();
                }
            }
        } catch (IOException ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, "保存图片失败" + ex);
        }
    }//GEN-LAST:event_Button_SaveImageActionPerformed

    private void Button_SaveDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SaveDataActionPerformed
        try {
            File file = FileDialogHelp.GetFilePath(".xls");
            if (file != null) {
                this.commapp.SaveToExcel(file.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "保存成功!");
            }
        } catch (Exception ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, "保存记录失败!" + ex);
        }
    }//GEN-LAST:event_Button_SaveDataActionPerformed

    private void Button_AddSnapShotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AddSnapShotActionPerformed
        this.commapp.AddSnapShot();
        UpdataChartSnapShort(this.commapp.GetSnapShots());
    }//GEN-LAST:event_Button_AddSnapShotActionPerformed

    private void Button_DeletSnapShotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_DeletSnapShotActionPerformed
        this.commapp.DelSnapShot();
        UpdataChartSnapShort(this.commapp.GetSnapShots());
    }//GEN-LAST:event_Button_DeletSnapShotActionPerformed

    private void ToggleButton_DataTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_DataTableActionPerformed
        if (ToggleButton_DataTable.isSelected()) {
            chartAnddataSplit.setRightComponent(dataTablePane);
        } else {
            chartAnddataSplit.setRightComponent(null);
        }
    }//GEN-LAST:event_ToggleButton_DataTableActionPerformed

    private void ToggleButton_RMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_RMSActionPerformed
        this.DisplayRMS();
    }//GEN-LAST:event_ToggleButton_RMSActionPerformed

    private void Button_LoadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_LoadDataActionPerformed
        try {
            File file = FileDialogHelp.GetFilePath(".xls");
            if (file != null) {
                this.commapp.ReadExcel(file.getAbsolutePath());
                UpdataChartSnapShort(this.commapp.GetSnapShots());
                JOptionPane.showMessageDialog(this, "读取成功!");
            }
        } catch (Exception ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, "读取成功记录失败!" + ex);
        }
    }//GEN-LAST:event_Button_LoadDataActionPerformed

    private void Button_WatchNodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_WatchNodeActionPerformed
        new WatchNodeDialog(MainForm.instance, this.commapp).setVisible(true);
        watch_lable.forEach(label -> {
            this.dataChartPane.GetFreeTextPaint().RemoveLable(label);
        });
        watch_lable.clear();
    }//GEN-LAST:event_Button_WatchNodeActionPerformed

    private void Button_OSTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_OSTestActionPerformed
        this.commapp.StartOSTest();
    }//GEN-LAST:event_Button_OSTestActionPerformed

    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_AddSnapShot;
    private javax.swing.JButton Button_DeletSnapShot;
    private javax.swing.JButton Button_LoadData;
    private javax.swing.JButton Button_OSTest;
    private javax.swing.JButton Button_SaveData;
    private javax.swing.JButton Button_SaveImage;
    private javax.swing.JButton Button_WatchNode;
    private javax.swing.JPanel DisplayArea;
    private javax.swing.JToggleButton ToggleButton_DataTable;
    private javax.swing.JToggleButton ToggleButton_RMS;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
