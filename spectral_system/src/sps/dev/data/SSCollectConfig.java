/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.data;

/**
 *
 * @author chejf
 */
public class SSCollectConfig {

    public static byte NoneKey = 0x00;
    public static byte BattwoseKey = 0x01;
    public static byte SquareKey = 0x02;

    //光源开关
    public boolean light_switch;
    public boolean linearEnable;
    public boolean dk_enable;
    public int filterKey;
    public int window;

    public SSCollectConfig(boolean light_switch, boolean linearEnable, boolean dk_enable, int filterKey, int window) {
        this.light_switch = light_switch;
        this.linearEnable = linearEnable;
        this.dk_enable = dk_enable;
        this.filterKey = filterKey;
        this.window = window;
    }
}
