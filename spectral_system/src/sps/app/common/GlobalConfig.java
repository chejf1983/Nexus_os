/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.common;

import nahon.comm.event.NEventCenter;
import sps.dev.data.SSCollectConfig;
import sps.dev.data.SSCollectPar;

/**
 *
 * @author chejf
 */
public class GlobalConfig {

    public SSCollectPar collect_par = new SSCollectPar(1, 1, 1, SSCollectPar.SoftMode);
    public SSCollectConfig collect_config = new SSCollectConfig(false, true, false, SSCollectConfig.NoneKey, 1);

    public NEventCenter ConfigUpdateEvent = new NEventCenter();
}
