/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.devcie.config;

import javax.swing.table.AbstractTableModel;
import sps.dev.data.SSConfigItem;

/**
 *
 * @author chejf
 */
public class ConfigTableModel extends AbstractTableModel {

    private SSConfigItem[] list = new SSConfigItem[0];
    private final String[] names = new String[]{"名称(范围)", "数值"};
    public static int[] column_len = {140, 150};

    public ConfigTableModel(SSConfigItem[] list) {
        this.list = list;
    }

    public SSConfigItem[] GetValues() {
        return this.list;
    }

    @Override
    public String getColumnName(int i) {
        return this.names[i];
    }

    @Override
    public int getRowCount() {
        return this.list.length;
    }

    @Override
    public int getColumnCount() {
        return names.length;
    }

    private String GetRange(int rowIndex) {
        return list[rowIndex].unit.contentEquals("") ? "" : "(" + list[rowIndex].unit + ")";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            if (list[rowIndex].inputtype == SSConfigItem.ItemType.W
                    || list[rowIndex].inputtype == SSConfigItem.ItemType.S
                    || list[rowIndex].inputtype == SSConfigItem.ItemType.B) {
                return "* " + list[rowIndex].data_name + GetRange(rowIndex);
            } else {
                return list[rowIndex].data_name + GetRange(rowIndex);
            }
        } else {
            if (list[rowIndex].inputtype == SSConfigItem.ItemType.B) {
                return Boolean.valueOf(list[rowIndex].value);
            } else {
                return list[rowIndex].value;
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            return list[rowIndex].inputtype == SSConfigItem.ItemType.W
                    || list[rowIndex].inputtype == SSConfigItem.ItemType.S
                    || list[rowIndex].inputtype == SSConfigItem.ItemType.B;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object o, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            list[rowIndex].SetValue(o.toString());
        }
    }

}
