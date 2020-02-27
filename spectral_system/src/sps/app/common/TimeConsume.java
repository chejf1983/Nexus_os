/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.common;

import nahon.comm.event.EventCenter;

/**
 *
 * @author chejf
 */
public class TimeConsume {

    private long startTime = 0;

    public void SetTimeFlag() {
        startTime = System.nanoTime();//开始时间
    }

    public void StopTime() {
        TimeConsumeEvent.CreateEvent(System.nanoTime() - startTime);
    }

    public EventCenter<Long> TimeConsumeEvent = new EventCenter();
}
