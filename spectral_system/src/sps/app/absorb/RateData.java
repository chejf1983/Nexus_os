/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.absorb;

import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author chejf
 */
public class RateData {
    public final SSpectralDataPacket testdata;
    public final SSpectralDataPacket basedata;
    public double[] Rate;
    

    public RateData(SSpectralDataPacket basedata, SSpectralDataPacket testdata, double[] rate) {
        this.testdata = testdata;
        this.basedata = basedata;
        this.Rate = rate;
    }
}
