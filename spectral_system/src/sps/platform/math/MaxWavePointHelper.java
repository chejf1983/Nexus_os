/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.platform.math;

import sps.platform.math.CruveSimilater.SlopPara;
import java.util.ArrayList;

/**
 * 寻峰
 * @author Administrator
 */
public class MaxWavePointHelper {

    public static Integer[] FindMaxWavePoint(double[] Yvalue, double[] Xvalue) {        
        ArrayList<Integer> index = new ArrayList();
        
        if (Xvalue.length < 2) {
            return index.toArray(new Integer[0]);
        }
        int tmp = (int) (100 * (Xvalue[1] - Xvalue[0]));
        if (tmp == 0) {
            return index.toArray(new Integer[0]);
        }
        int Intervale = (int) (100 / tmp);


        int maxPoint = 0;
        for (int i = 0; i < Yvalue.length; i++) {
            if (Yvalue[maxPoint] < Yvalue[i]) {
                maxPoint = i;
            }
        }

        for (int i = 0; i < Yvalue.length; i++) {
            if (IsMaxPoint(Yvalue, i, 5 * Intervale)
                    && IsMaxPointBiggerEnough(Yvalue[i], Yvalue[maxPoint])
                    && IsSlopeCorrect(Yvalue, Xvalue, i, 10 * Intervale)) {
                index.add(i);
            }
        }

        return index.toArray(new Integer[0]);
    }

    public static double MaxPercentage = 0.05;

    private static boolean IsMaxPointBiggerEnough(double plusPoint, double MaxPoint) {
        return plusPoint > MaxPoint * MaxPercentage;
    }

    private static boolean IsMaxPoint(double[] waveArray, int index, int halfwindow) {
        if (index - halfwindow < 0 || index + halfwindow > waveArray.length) {
            return false;
        }

        for (int i = index - halfwindow; i < index; i++) {
            if (waveArray[index] <= waveArray[i]) {
                return false;
            }
        }

        for (int i = index; i < index + halfwindow; i++) {
            if (waveArray[index] < waveArray[i]) {
                return false;
            }
        }

        return true;
    }

    private static boolean IsSlopeCorrect(double[] Yvalue, double[] Xvalue, int index, int halfwindow) {
        if (index - halfwindow + 1 < 0 || index + halfwindow > Yvalue.length) {
            return false;
        }

        SlopPara left = CruveSimilater.GetSlop(Yvalue, Xvalue, index - halfwindow + 1, halfwindow);
        SlopPara right = CruveSimilater.GetSlop(Yvalue, Xvalue, index, halfwindow);

        if (left.k <= 0) {
            return false;
        }
        if (right.k > 0) {
            return false;
        }

        return true;
    }

    private static double SlopPercentage = 0.7;

    private static boolean IsSlopeCorrect(double[] waveArray, int index, int halfwindow) {
        if (index - halfwindow < 0 || index + halfwindow > waveArray.length) {
            return false;
        }

        double[] tmp = new double[2 * halfwindow];

        for (int j = index - halfwindow, i = 0; j < index + halfwindow; j++, i++) {
            tmp[i] = waveArray[j + 1] - waveArray[j];
        }

        double slop = 0;
        double abstractslop = 0;
        for (int i = 0; i < halfwindow; i++) {
            slop += tmp[i];
            abstractslop += Math.abs(tmp[i]);
        }
        if (slop < abstractslop * SlopPercentage) {
            return false;
        }

        slop = 0;
        abstractslop = 0;
        for (int i = halfwindow; i < tmp.length; i++) {
            slop += tmp[i];
            abstractslop += Math.abs(tmp[i]);
        }
        if (slop > -1 * abstractslop * SlopPercentage) {
            return false;
        }

        return true;
    }

}
