/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.device.manager;

import java.util.logging.Level;
import nahon.comm.event.NEvent;
import nahon.comm.event.NEventListener;
import nahon.comm.faultsystem.LogCenter;
import nexus.devcie.config.ConfigDialog;
import nexus.main.entry.MainForm;
import sps.app.common.GlobalConfig;
import sps.control.manager.ISpDevice;
import sps.platform.SpectralPlatService;

/**
 *
 * @author jiche
 */
public class DeviceControlPanel extends javax.swing.JPanel {

    /**
     * Creates new form DeviceControlPanel
     */
    public DeviceControlPanel() {
        initComponents();

        initMyComponents();

        RegisterLanguage();
    }

    private void initMyComponents() {

        //设置窗口ICON
        this.Frame_list.setFrameIcon(null);
        this.Frame_list.setTitle("测试操作");
        //初始化界面
        Label_IntervalTime.setToolTipText("0-1hour");
        Label_IntegralTime.setToolTipText("0.1-60000ms");
        Label_AverageTime.setToolTipText("1-100times");
        Label_window.setToolTipText("1-10");

        //初始化定标按钮是否可见
//        String flag = SpectralPlatService.GetInstance().GetConfig().getProperty(SpectralSystemConfig.InternalFlag);
//        if (Integer.valueOf(flag) == 1) {
//            this.ToggleButton_LinearEnable.setVisible(true);
//            this.PNG_LineCalibrate.setVisible(true);
//        } else {
//            this.ToggleButton_LinearEnable.setVisible(false);
//            this.PNG_LineCalibrate.setVisible(false);
//        }
        InitDeviceInfo();
    }

    // <editor-fold defaultstate="collapsed" desc="界面参数初始化">
    private void InitDeviceInfo() {
        SpectralPlatService.GetInstance().GetAppManager().TestEvent.RegeditListener(new NEventListener<Boolean>() {
            @Override
            public void recevieEvent(NEvent<Boolean> event) {
                //更新控制面板使能状态
                UpdateControlPaneState(event.GetEvent());
            }
        });

        UpdateControlPaneState(SpectralPlatService.GetInstance().GetAppManager().IsRunning());

        GlobalConfig TestConfig = SpectralPlatService.GetInstance().GetAppManager().TestConfig;

        //跟新按电流使能状态
        SpectralPlatService.GetInstance().GetAppManager().TestConfig.ConfigUpdateEvent.RegeditListener(new NEventListener() {
            @Override
            public void recevieEvent(NEvent event) {
                //更新平均次数数据
                Average_Input.setText(String.valueOf(TestConfig.collect_par.averageTime));
                //平滑窗口
                Average_Input1.setText(String.valueOf(TestConfig.collect_config.window));
                //更新积分时间状态
                IntegeralTime_Input.setText(String.valueOf(TestConfig.collect_par.integralTime));
                //初始化采样间隔
                IntegervalTime_Input.setText(String.valueOf((float) TestConfig.collect_par.interval_time / 1000));
                //更新灯开关
                ToggleButton_LightEnable.setSelected(TestConfig.collect_config.light_switch);
                //更新按电流选择开关
                ToggleButton_DarkEnable.setSelected(TestConfig.collect_config.dk_enable);
                //更新非线性开关
            }
        });
        ToggleButton_DarkEnable.setSelected(TestConfig.collect_config.dk_enable);

        //更新线性矫正使能状态
        ToggleButton_LinearEnable.setSelected(TestConfig.collect_config.linearEnable);

        //更新平均次数数据
        Average_Input.setText(String.valueOf(TestConfig.collect_par.averageTime));

        //平滑窗口
        Average_Input1.setText(String.valueOf(TestConfig.collect_config.window));

        //更新积分时间状态
        IntegeralTime_Input.setText(String.valueOf(TestConfig.collect_par.integralTime));
        //初始化采样间隔
        IntegervalTime_Input.setText(String.valueOf((float) TestConfig.collect_par.interval_time / 1000));

        //设置当前光源状态
        ToggleButton_LightEnable.setSelected(TestConfig.collect_config.light_switch);

    }

    private void UpdateControlPaneState(boolean isTesting) {
        this.IntegeralTime_Input.setEnabled(!isTesting);
        this.IntegervalTime_Input.setEnabled(!isTesting);
        this.Average_Input.setEnabled(!isTesting);
        this.Average_Input1.setEnabled(!isTesting);

        this.Button_AutoTestTime.setEnabled(!isTesting);
        this.ComboBox_CollectMode.setEnabled(!isTesting);

        this.Button_SingelCollect.setEnabled(!isTesting);
        this.Button_FrequenCollect.setEnabled(!isTesting);
        this.Button_StopCollect.setEnabled(isTesting);
        this.ToggleButton_LightEnable.setEnabled(!isTesting);

        this.Button_SetParameter.setEnabled(!isTesting);
        this.Button_DarkModify.setEnabled(!isTesting);

        this.ToggleButton_DarkEnable.setEnabled(!isTesting);
        this.ToggleButton_LinearEnable.setEnabled(!isTesting);
        this.ToggleButton_LightEnable.setEnabled(!isTesting);
    }

    // </editor-fold>
    //注册语言包
    private void RegisterLanguage() {
        Label_IntegralTime.setText("积分时间");
        Label_IntervalTime.setText("连续采样间隔");
        Label_AverageTime.setText("平均次数");
        Label_window.setText("平滑设置");

        Label_IntegralTimeUnit.setText("ms");
        Label_IntervalTimeUnit.setText("s");
        Label_AverageUnit.setText("次");

        Button_AutoTestTime.setText("自动积分");
        Button_FrequenCollect.setToolTipText("连续测试");
        Button_SingelCollect.setToolTipText("测试");
        Button_StopCollect.setToolTipText("停止测试");
        Button_SetParameter.setToolTipText("参数设置");
        Button_DarkModify.setToolTipText("采集暗电流");
        ToggleButton_LightEnable.setToolTipText("使能氙灯");

        ToggleButton_DarkEnable.setText("扣除暗电流");
        ToggleButton_LinearEnable.setText("使能非线性定标");

        this.ComboBox_CollectMode.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"软件触发", "硬件边沿触发(上升沿)", "硬件边沿触发(下降沿)", "硬件电平触发( 高电平)", "硬件电平触发(低电平)"}));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Frame_list = new javax.swing.JInternalFrame();
        jPanel2 = new javax.swing.JPanel();
        Button_StopCollect = new javax.swing.JButton();
        Button_SingelCollect = new javax.swing.JButton();
        Button_FrequenCollect = new javax.swing.JButton();
        Average_Input = new javax.swing.JTextField();
        IntegeralTime_Input = new javax.swing.JTextField();
        Label_IntegralTime = new javax.swing.JLabel();
        Label_AverageTime = new javax.swing.JLabel();
        Label_AverageUnit = new javax.swing.JLabel();
        Label_IntegralTimeUnit = new javax.swing.JLabel();
        Button_SetParameter = new javax.swing.JButton();
        ToggleButton_DarkEnable = new javax.swing.JToggleButton();
        Button_DarkModify = new javax.swing.JButton();
        Button_AutoTestTime = new javax.swing.JButton();
        PNG_DarkModify = new javax.swing.JLabel();
        Label_IntervalTime = new javax.swing.JLabel();
        IntegervalTime_Input = new javax.swing.JTextField();
        Label_IntervalTimeUnit = new javax.swing.JLabel();
        ToggleButton_LinearEnable = new javax.swing.JToggleButton();
        PNG_LineCalibrate = new javax.swing.JLabel();
        ComboBox_CollectMode = new javax.swing.JComboBox();
        ToggleButton_LightEnable = new javax.swing.JToggleButton();
        Label_window = new javax.swing.JLabel();
        Average_Input1 = new javax.swing.JTextField();
        Label_AverageUnit1 = new javax.swing.JLabel();

        Frame_list.setVisible(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        Button_StopCollect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/stop.png"))); // NOI18N
        Button_StopCollect.setEnabled(false);
        Button_StopCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_StopCollectActionPerformed(evt);
            }
        });

        Button_SingelCollect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/step.png"))); // NOI18N
        Button_SingelCollect.setEnabled(false);
        Button_SingelCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SingelCollectActionPerformed(evt);
            }
        });

        Button_FrequenCollect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/starting.png"))); // NOI18N
        Button_FrequenCollect.setEnabled(false);
        Button_FrequenCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_FrequenCollectActionPerformed(evt);
            }
        });

        Average_Input.setEnabled(false);
        Average_Input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                Average_InputFocusLost(evt);
            }
        });

        IntegeralTime_Input.setEnabled(false);
        IntegeralTime_Input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                IntegeralTime_InputFocusLost(evt);
            }
        });

        Label_IntegralTime.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_IntegralTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Label_IntegralTime.setText("积分时间：");

        Label_AverageTime.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_AverageTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Label_AverageTime.setText("平均次数：");

        Label_AverageUnit.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_AverageUnit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Label_AverageUnit.setText("time");

        Label_IntegralTimeUnit.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_IntegralTimeUnit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Label_IntegralTimeUnit.setText("ms");

        Button_SetParameter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/setting.png"))); // NOI18N
        Button_SetParameter.setEnabled(false);
        Button_SetParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SetParameterActionPerformed(evt);
            }
        });

        ToggleButton_DarkEnable.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        ToggleButton_DarkEnable.setEnabled(false);
        ToggleButton_DarkEnable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ToggleButton_DarkEnableItemStateChanged(evt);
            }
        });

        Button_DarkModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/darkcollect.png"))); // NOI18N
        Button_DarkModify.setEnabled(false);
        Button_DarkModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_DarkModifyActionPerformed(evt);
            }
        });

        Button_AutoTestTime.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Button_AutoTestTime.setText(" AutoTestTime");
        Button_AutoTestTime.setToolTipText("");
        Button_AutoTestTime.setEnabled(false);
        Button_AutoTestTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AutoTestTimeActionPerformed(evt);
            }
        });

        PNG_DarkModify.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PNG_DarkModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/switchoff.png"))); // NOI18N
        PNG_DarkModify.setText(" ");
        PNG_DarkModify.setToolTipText("");
        PNG_DarkModify.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Label_IntervalTime.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_IntervalTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Label_IntervalTime.setText("采样间隔：");

        IntegervalTime_Input.setText("1");
        IntegervalTime_Input.setEnabled(false);
        IntegervalTime_Input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                IntegervalTime_InputFocusLost(evt);
            }
        });

        Label_IntervalTimeUnit.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_IntervalTimeUnit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Label_IntervalTimeUnit.setText("s");

        ToggleButton_LinearEnable.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        ToggleButton_LinearEnable.setToolTipText("");
        ToggleButton_LinearEnable.setEnabled(false);
        ToggleButton_LinearEnable.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ToggleButton_LinearEnable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ToggleButton_LinearEnableItemStateChanged(evt);
            }
        });

        PNG_LineCalibrate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PNG_LineCalibrate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/switchoff.png"))); // NOI18N
        PNG_LineCalibrate.setText(" ");
        PNG_LineCalibrate.setToolTipText("");
        PNG_LineCalibrate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        ComboBox_CollectMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ComboBox_CollectMode.setEnabled(false);

        ToggleButton_LightEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/light_off.png"))); // NOI18N
        ToggleButton_LightEnable.setPreferredSize(new java.awt.Dimension(57, 33));
        ToggleButton_LightEnable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ToggleButton_LightEnableItemStateChanged(evt);
            }
        });
        ToggleButton_LightEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_LightEnableActionPerformed(evt);
            }
        });

        Label_window.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_window.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Label_window.setText("平滑设置：");

        Average_Input1.setEnabled(false);
        Average_Input1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                Average_Input1FocusLost(evt);
            }
        });

        Label_AverageUnit1.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_AverageUnit1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Label_AverageUnit1.setText("1-10");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(ComboBox_CollectMode, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PNG_LineCalibrate)
                                    .addComponent(Button_SetParameter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Button_SingelCollect))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(Button_FrequenCollect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Button_StopCollect)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Button_DarkModify, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(ToggleButton_LightEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Button_AutoTestTime, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(PNG_DarkModify, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ToggleButton_DarkEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(ToggleButton_LinearEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Label_window, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Label_IntervalTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(Label_IntegralTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Label_AverageTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(IntegeralTime_Input, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Average_Input, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(IntegervalTime_Input, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                            .addComponent(Average_Input1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label_IntegralTimeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_AverageUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_IntervalTimeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_AverageUnit1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {PNG_DarkModify, PNG_LineCalibrate});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Button_DarkModify, Button_FrequenCollect, Button_SetParameter, Button_SingelCollect, Button_StopCollect, ToggleButton_LightEnable});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ToggleButton_DarkEnable, ToggleButton_LinearEnable});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(Label_IntervalTimeUnit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Label_IntegralTimeUnit)
                            .addComponent(IntegeralTime_Input)
                            .addComponent(Label_IntegralTime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Label_AverageUnit)
                            .addComponent(Average_Input)
                            .addComponent(Label_AverageTime)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Label_IntervalTime)
                        .addComponent(IntegervalTime_Input)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Average_Input1)
                    .addComponent(Label_window)
                    .addComponent(Label_AverageUnit1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ComboBox_CollectMode, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_SingelCollect, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_FrequenCollect, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_StopCollect, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_DarkModify, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ToggleButton_LightEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_SetParameter, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(Button_AutoTestTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PNG_DarkModify, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_DarkEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ToggleButton_LinearEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PNG_LineCalibrate))
                .addGap(5, 5, 5))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ToggleButton_DarkEnable, ToggleButton_LinearEnable});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {PNG_DarkModify, PNG_LineCalibrate});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Button_DarkModify, Button_FrequenCollect, Button_SetParameter, Button_SingelCollect, Button_StopCollect, ToggleButton_LightEnable});

        javax.swing.GroupLayout Frame_listLayout = new javax.swing.GroupLayout(Frame_list.getContentPane());
        Frame_list.getContentPane().setLayout(Frame_listLayout);
        Frame_listLayout.setHorizontalGroup(
            Frame_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Frame_listLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Frame_listLayout.setVerticalGroup(
            Frame_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Frame_listLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Frame_list, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Frame_list)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ToggleButton_LightEnableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ToggleButton_LightEnableItemStateChanged
        SpectralPlatService.GetInstance().GetAppManager().GetCurrentApp().EnableLight(ToggleButton_LightEnable.isSelected());

        if (ToggleButton_LightEnable.isSelected()) {
            ToggleButton_LightEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/light_on.png")));
        } else {
            ToggleButton_LightEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/light_off.png")));
        }
    }//GEN-LAST:event_ToggleButton_LightEnableItemStateChanged

    private void ToggleButton_DarkEnableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ToggleButton_DarkEnableItemStateChanged
        SpectralPlatService.GetInstance().GetAppManager().TestConfig.collect_config.dk_enable = this.ToggleButton_DarkEnable.isSelected();
        //更新按电流按钮图标
        if (this.ToggleButton_DarkEnable.isSelected()) {
            PNG_DarkModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/switchon.png")));
        } else {
            PNG_DarkModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/switchoff.png")));
        }

    }//GEN-LAST:event_ToggleButton_DarkEnableItemStateChanged

    private void ToggleButton_LinearEnableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ToggleButton_LinearEnableItemStateChanged
        SpectralPlatService.GetInstance().GetAppManager().TestConfig.collect_config.linearEnable = this.ToggleButton_LinearEnable.isSelected();

        if (this.ToggleButton_LinearEnable.isSelected()) {
            PNG_LineCalibrate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/switchon.png")));
        } else {
            PNG_LineCalibrate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/switchoff.png")));
        }
    }//GEN-LAST:event_ToggleButton_LinearEnableItemStateChanged

    private void Button_AutoTestTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AutoTestTimeActionPerformed
        SpectralPlatService.GetInstance().GetAppManager().GetCurrentApp().AutoTestTime();
    }//GEN-LAST:event_Button_AutoTestTimeActionPerformed

    private void Button_SingelCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SingelCollectActionPerformed
        SpectralPlatService.GetInstance().GetAppManager().GetCurrentApp().SingleTest();
    }//GEN-LAST:event_Button_SingelCollectActionPerformed

    private void Button_FrequenCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_FrequenCollectActionPerformed
        SpectralPlatService.GetInstance().GetAppManager().GetCurrentApp().SusTainTest();
    }//GEN-LAST:event_Button_FrequenCollectActionPerformed

    private void Button_StopCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_StopCollectActionPerformed
        SpectralPlatService.GetInstance().GetAppManager().GetCurrentApp().StopTest();
    }//GEN-LAST:event_Button_StopCollectActionPerformed

    private void Button_DarkModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_DarkModifyActionPerformed
        SpectralPlatService.GetInstance().GetAppManager().GetCurrentApp().DKTest();
        ToggleButton_DarkEnable.setSelected(true);
    }//GEN-LAST:event_Button_DarkModifyActionPerformed

    private void Button_SetParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SetParameterActionPerformed
        SpectralPlatService.GetInstance().GetAppManager().RunCommand(() -> {
            //显示光谱仪参数设置
            try (ISpDevice dev = SpectralPlatService.GetInstance().GetSingleDevManager().GetSelectDev()) {
                dev.Open();

//                System.out.println(MainForm.instance);
                new ConfigDialog(MainForm.instance, dev).setVisible(true);
            } catch (Exception ex) {
                LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
            }
        });

    }//GEN-LAST:event_Button_SetParameterActionPerformed

    private void IntegervalTime_InputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IntegervalTime_InputFocusLost
        try {
            SpectralPlatService.GetInstance().GetAppManager().TestConfig.collect_par.interval_time = (int) (Float.valueOf(this.IntegervalTime_Input.getText()) * 1000);
        } catch (NumberFormatException ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
        }
    }//GEN-LAST:event_IntegervalTime_InputFocusLost

    private void IntegeralTime_InputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IntegeralTime_InputFocusLost
        try {
            SpectralPlatService.GetInstance().GetAppManager().TestConfig.collect_par.integralTime = Float.valueOf(this.IntegeralTime_Input.getText());
        } catch (NumberFormatException ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
        }
    }//GEN-LAST:event_IntegeralTime_InputFocusLost

    private void Average_InputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Average_InputFocusLost
        try {
            SpectralPlatService.GetInstance().GetAppManager().TestConfig.collect_par.averageTime = Integer.valueOf(this.Average_Input.getText());
        } catch (Exception ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
        }
    }//GEN-LAST:event_Average_InputFocusLost

    private void ToggleButton_LightEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_LightEnableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ToggleButton_LightEnableActionPerformed

    private void Average_Input1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Average_Input1FocusLost
        try {
            SpectralPlatService.GetInstance().GetAppManager().TestConfig.collect_config.window = Integer.valueOf(this.Average_Input1.getText());
        } catch (Exception ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
        }
    }//GEN-LAST:event_Average_Input1FocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Average_Input;
    private javax.swing.JTextField Average_Input1;
    private javax.swing.JButton Button_AutoTestTime;
    private javax.swing.JButton Button_DarkModify;
    private javax.swing.JButton Button_FrequenCollect;
    private javax.swing.JButton Button_SetParameter;
    private javax.swing.JButton Button_SingelCollect;
    private javax.swing.JButton Button_StopCollect;
    private javax.swing.JComboBox ComboBox_CollectMode;
    private javax.swing.JInternalFrame Frame_list;
    private javax.swing.JTextField IntegeralTime_Input;
    private javax.swing.JTextField IntegervalTime_Input;
    private javax.swing.JLabel Label_AverageTime;
    private javax.swing.JLabel Label_AverageUnit;
    private javax.swing.JLabel Label_AverageUnit1;
    private javax.swing.JLabel Label_IntegralTime;
    private javax.swing.JLabel Label_IntegralTimeUnit;
    private javax.swing.JLabel Label_IntervalTime;
    private javax.swing.JLabel Label_IntervalTimeUnit;
    private javax.swing.JLabel Label_window;
    private javax.swing.JLabel PNG_DarkModify;
    private javax.swing.JLabel PNG_LineCalibrate;
    private javax.swing.JToggleButton ToggleButton_DarkEnable;
    private javax.swing.JToggleButton ToggleButton_LightEnable;
    private javax.swing.JToggleButton ToggleButton_LinearEnable;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
