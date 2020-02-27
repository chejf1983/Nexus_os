/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.std;

/**
 *
 * @author Administrator
 */
public class LinearNodePar {

    public final float NodeID;  //波长，或者像素
    public final float [] ADvalue; //ad值（去掉暗电流以后)
    public final float [] itime;   //ad值对应的积分时间

    public LinearNodePar(float node, float [] itime, float [] advalue) {
        this.NodeID = node;
        this.ADvalue = advalue;
        this.itime = itime;
    }
}
