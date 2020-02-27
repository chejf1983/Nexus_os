/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.data;

import java.math.BigDecimal;
import java.util.Date;
import sps.platform.math.Newton;

/**
 *
 * @author Administrator
 */
public class SSpectralDataPacket {

    public Date time;
    public SSPData data;

    public SSpectralDataPacket(double[] waveIndex, double[] datavalue) {
        this.data = (new SSPData(waveIndex, datavalue));
        this.time = new Date();
    }

    public SSpectralDataPacket(SSPData ADValue) {
        this.data = (ADValue);
        this.time = new Date();
    }

    public SSpectralDataPacket(SSpectralDataPacket data) {
        this.time = data.time;
        this.data = new SSPData(data.data);
    }

    public SSpectralDataPacket ConvertSPData(float startnm, float stopnm, float interval) {
        int data_num = (int) ((stopnm - startnm) / interval);
        double[] waveIndex = new double[data_num];

        for (int i = 0; i < data_num; i++) {
            waveIndex[i] = startnm + interval * i;
        }

        double[] datavalue = Newton.predictd(this.data.waveIndex, this.data.datavalue, waveIndex);
        for (int i = 0; i < datavalue.length; i++) {
            datavalue[i] = new BigDecimal(datavalue[i]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        return new SSpectralDataPacket(waveIndex, datavalue);
    }
}
