/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.control.manager;

import sps.dev.data.SSCollectConfig;
import sps.dev.data.SSCollectPar;
import sps.dev.data.SSConfigItem;
import sps.dev.data.SSLinearParameter;
import sps.dev.data.SSPDevInfo;
import sps.dev.data.SSWaveCaculatePar;
import sps.dev.data.SSpectralDataPacket;
import sps.dev.data.SSpectralPar;

/**
 *
 * @author Administrator
 */
public interface ISpDevice extends AutoCloseable {

    // <editor-fold defaultstate="collapsed" desc="基本控制接口">  
    public void Open() throws Exception;

    public boolean IsOpened();

    public boolean IsCmdCancled();

    public void Cancel();

    public SSPDevInfo GetDevInfo();

    public void InitDevice() throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="采集控制">      
    public SSpectralDataPacket CollectData() throws Exception;

    // <editor-fold defaultstate="collapsed" desc="采集参数">  
    public SSCollectPar GetCollectPar();

    public void SetCollectPar(SSCollectPar par) throws Exception;
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="采集设置">      
    public SSCollectConfig GetCollectCoinfig();

    public void SetCollectConfig(SSCollectConfig par) throws Exception;
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="暗电流">  
    public SSpectralDataPacket DKModify() throws Exception;

    public void ClearDKData();
    // </editor-fold>   
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="参数设置">  
    // <editor-fold defaultstate="collapsed" desc="非线性系数">  
    public SSLinearParameter GetLinearPar();

    public void SetLinearPar(SSLinearParameter par) throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="光谱仪参数系数">  
    public SSpectralPar GetSpectralPar();
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="波长系数性系数">  
    public SSWaveCaculatePar GetWaveParameter();

    public void SetWaveParameter(SSWaveCaculatePar par) throws Exception;
    // </editor-fold> 
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="外设控制">  
    public void EnableExtern(boolean value) throws Exception;

    public SSConfigItem[] GetExternConfig();

    public void SetExcternConfig(SSConfigItem[] config) throws Exception;
    // </editor-fold> 
}
