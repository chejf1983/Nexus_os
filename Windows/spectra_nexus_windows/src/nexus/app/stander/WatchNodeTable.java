/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.app.stander;

import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.table.AbstractTableModel;
import nahon.comm.faultsystem.LogCenter;
import sps.app.std.WatchNode;

/**
 *
 * @author chejf
 */
public class WatchNodeTable extends AbstractTableModel {

    public final ArrayList<WatchNode> nodes;

    public WatchNodeTable(ArrayList<WatchNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int getRowCount() {
        return nodes.size();
    }

    @Override
    public int getColumnCount() {
        return WatchNode.ColumNames.length;
    }

    @Override
    public String getColumnName(int i) {
        return WatchNode.ColumNames[i];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        WatchNode node = this.nodes.get(rowIndex);
        if (columnIndex == 0) {
            if (node == WatchNode.MAXNODE) {
                return "峰值";
            } else {
                return node.node_wave;
            }
        }
        if (columnIndex == 1) {
            return node.range_min;
        }
        if (columnIndex == 2) {
            return node.range_max;
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return true;
    }

    @Override
    public void setValueAt(Object o, int rowIndex, int columnIndex) {
        WatchNode node = this.nodes.get(rowIndex);
        try {
            //峰值点不允许修改波长
            if (columnIndex == 0 && node != WatchNode.MAXNODE) {
                node.node_wave = Double.valueOf(o.toString());
            }
            if (columnIndex == 1) {
                node.range_min = Double.valueOf(o.toString());
            }
            if (columnIndex == 2) {
                node.range_max = Double.valueOf(o.toString());
            }
        } catch (Exception ex) {
            LogCenter.Instance().SendFaultReport(Level.SEVERE, ex);
        }
    }
}
