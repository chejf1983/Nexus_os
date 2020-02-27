/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.device.manager;

import javax.swing.table.AbstractTableModel;
import sps.dev.data.SSPDevInfo.SSEquipmentInfo;

/**
 *
 * @author Administrator
 */
public class EIAModel extends AbstractTableModel {

    private final String[] name = new String[]{
        "设备名称",
        "序列号",
        "生产日期",
        "软件版面",
        "硬件版本"};

    private String[] value;

    public static int[] column_width = new int[]{100, 0};

    public EIAModel(SSEquipmentInfo eia) {
        if (eia != null) {
            value = new String[]{
                eia.DeviceName,
                eia.BuildSerialNum,
                eia.BuildDate,
                eia.SoftwareVersion,
                String.valueOf(eia.Hardversion)};
        } else {
            value = new String[name.length];
            for (int i = 0; i < value.length; i++) {
                value[i] = "";
            }
        }
    }

    @Override
    public int getRowCount() {
        return name.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == 0) {
            return name[rowIndex];
        } else {
            return value[rowIndex];
        }
    }

    @Override
    public String getColumnName(int column) {
        return null;
    }

}
