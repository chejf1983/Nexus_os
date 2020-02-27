/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sps.platform.math;

/**
 *
 * @author Administrator
 */
public class CruveSimilater {
    //最小2乘法
    //Y=kX+b
    //k=（（XY）平--X平*Y平）/（（X2）平--(X平）2），
    //b=Y平--kX平；
    //X平=1/n∑Xi，
    //(XY）平=1/n∑XiYi；[1] 
    public static class SlopPara {
        public float b = 0;
        public float k = 0;
    }

    public static SlopPara GetSlop(double[] Yvalue, double[] Xvalue, int pos, int length) {        
        SlopPara tmp = new SlopPara();
        
        if (length <= 0) {
            return tmp;
        }
        float Xp, Yp, XpYp, X2p;
        Xp = Yp = XpYp = X2p = 0;
        for (int i = 0; i < length; i++) {
            Xp += Xvalue[pos + i];
            Yp += Yvalue[pos + i];
            XpYp += Xvalue[pos + i] * Yvalue[pos + i];
            X2p += Xvalue[pos + i] * Xvalue[pos + i];
        }
        Xp /= length;
        Yp /= length;
        XpYp /= length;
        X2p /= length;

        tmp.k = (XpYp - Xp * Yp) / (X2p - Xp * Xp);
        tmp.b = Yp - tmp.k * Xp;

        return tmp;
    }
}
