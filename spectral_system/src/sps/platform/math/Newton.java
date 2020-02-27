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
public class Newton {

    /*牛顿插值法*/
//    public static double[] predictd(double[] X, double[] Y, double[] X0) {
//        double Y0[] = new double[X0.length];
//
//        int x_left = 0;
//        int x_right = 0;
//        for (int i = 0; i < X0.length; i++) {
//            for (int j = x_left; j < X.length; j++) {
//                if (X[j] >= X0[i]) {
//                    x_left = j - 1 > 0 ? j - 1 : 0;
//                    x_right = j;
//                    if (x_left == x_right) {
//                        Y0[i] = Y[x_left];
//                    } else {
//                        double u = (X0[i] - X[x_left]) / (X[x_right] - X[x_left]);
//                        Y0[i] = Y[x_left] + u * (Y[x_right] - Y[x_left]);
//                    }
//                    break;
//                }
//            }
//        }
//        return Y0;
//    }

    public static double[] predictd(double[] X, double[] Y, double[] X0) {
        double[] ret = new double[X0.length];
        
        for (int i = 0; i < X0.length; i++) {
            ret[i] = predicts(X, Y, X.length, X0[i]);
        }
        //return predictd(x, y, x0);
        return ret;
    }
    
    public static double predicts(double[] index, double[] value, int value_length, double newIndex) {
        int smallindex = 0;
        int bigerindex = index.length - 1;

        if (index.length == 1) {
            smallindex = bigerindex = 0;
        } else if (newIndex <= index[smallindex]) {
            /* small than first point */
            bigerindex = smallindex + 1;
        } else if (newIndex >= index[bigerindex]) {
            /* bigger than last point */
            smallindex = bigerindex - 1;
        } else {
            for (int i = 0; i < value_length; i++) {
                int halfindex = (bigerindex + smallindex) / 2;
                if (newIndex > index[halfindex]) {
                    smallindex = halfindex;
                } else if (newIndex < index[halfindex]) {
                    bigerindex = halfindex;
                } else {
                    bigerindex = halfindex;
                    smallindex = halfindex - 1;
                }

                if (smallindex == bigerindex + 1) {
                    break;
                }
            }
        }

        double slope = 0;
        if ((index[bigerindex] - index[smallindex]) != 0) {
            slope = (newIndex - index[smallindex]) / (index[bigerindex] - index[smallindex]);
        }

        double result = value[smallindex] + slope * (value[bigerindex] - value[smallindex]);
        return result;
    }

    public static double[] predictf(float[] X, float[] Y, float[] X0) {
        double[] x = new double[X.length];
        double[] y = new double[Y.length];
        //double[] x0 = new double[X0.length];
        double[] ret = new double[X0.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = X[i];
            y[i] = Y[i];
        }

        for (int i = 0; i < X0.length; i++) {
            ret[i] = predicts(x, y, x.length, X0[i]);
        }
        //return predictd(x, y, x0);
        return ret;
    }
}
