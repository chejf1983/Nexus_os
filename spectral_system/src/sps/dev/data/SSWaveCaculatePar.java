/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.data;

/**
 *
 * @author Administrator
 */
public class SSWaveCaculatePar {
    public double C0 = 0d;
    public double C1 = 0d;
    public double C2 = 0d;
    public double C3 = 0d;

    public SSWaveCaculatePar() {

    }

    public SSWaveCaculatePar(SSWaveCaculatePar spar) {
        this.C0 = spar.C0;
        this.C1 = spar.C1;
        this.C2 = spar.C2;
        this.C3 = spar.C3;
    }

    public boolean EqualTo(SSWaveCaculatePar other) {
        return this.C0 == other.C0
                && this.C1 == other.C1
                && this.C2 == other.C2
                && this.C3 == other.C3;
    }
}
