/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.std;

import java.math.BigDecimal;
import sps.dev.data.SSpectralDataPacket;
import sps.platform.math.Newton;

/**
 *
 * @author chejf
 */
public class WatchNode {

    public static WatchNode MAXNODE = new MAXNode();
    public static String[] ColumNames = new String[]{"波长", "最小值", "最大值"};
    public double node_wave;
    public double node_value;
    public double range_min;
    public double range_max;

    public WatchNode(double wave, double range_min, double range_max) {
        this.node_wave = wave;
        this.range_min = range_min;
        this.range_max = range_max;
    }

    public WatchNode(WatchNode other) {
        this.node_wave = other.node_wave;
        this.range_min = other.range_min;
        this.range_max = other.range_max;
    }

    public void UpdateValue(SSpectralDataPacket data) {
        this.node_value = Newton.predicts(data.data.waveIndex, data.data.datavalue, data.data.waveIndex.length, node_wave);
        node_value = new BigDecimal(node_value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public boolean IsValueInRange() {
        return this.node_value < this.range_max && this.node_value > this.range_min;
    }

    @Override
    public String toString() {
        return this.node_wave + " nm:" + this.node_value + "(" + this.range_min + "-" + this.range_max + ")";
    }
}

class MAXNode extends WatchNode {

    public static String MaxFlag = "峰值";

    public MAXNode() {
        super(0, 0, 65535);
    }

    @Override
    public void UpdateValue(SSpectralDataPacket data) {
        node_value = data.data.datavalue[0];
        node_wave = data.data.waveIndex[0];
        //寻找最大值
        for (int i = 0; i < data.data.datavalue.length; i++) {
            if (data.data.datavalue[i] > node_value) {
                node_value = data.data.datavalue[i];
                node_wave = data.data.waveIndex[i];
            }
        }
    }

    @Override
    public String toString() {
        return MaxFlag + " " + super.toString();
    }
}
