/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.control.manager;

/**
 *
 * @author Administrator
 */
public interface ISPDevSearch {

    //public ISPDevice SearchDevice(AbstractIO io, byte hostaddr, byte dstaddr, int timeout);
    public String InitDriver(boolean value) throws Exception;

    public ISpDevice[] SearchDevice();

    public ISpDevice[] SearchDeviceWithCom();
}
