/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.devcie.config;

import javax.swing.table.AbstractTableModel;
import sps.dev.data.SSWaveCaculatePar;

/**
 *
 * @author Administrator
 */
public class TableWavePar extends AbstractTableModel {

    String[] parcString = {"C0:", "C1:", "C2:", "C3:"};
    final SSWaveCaculatePar swPar;
    public double[] parcValue;

    public TableWavePar(SSWaveCaculatePar swPar) {
        this.parcValue = new double[]{swPar.C0, swPar.C1, swPar.C2, swPar.C3};
        this.swPar = swPar;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "光谱系数";
            //return LanguageHelper.;
        } else {
            return "值";
            //  return parcValue[rowIndex];
        }
    }

    @Override
    public int getRowCount() {
        return parcString.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return parcString[rowIndex];
        } else {
            return parcValue[rowIndex];
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            this.parcValue[rowIndex] = Double.valueOf(aValue.toString());
        } catch (Exception ex) {
        }
    }
};
