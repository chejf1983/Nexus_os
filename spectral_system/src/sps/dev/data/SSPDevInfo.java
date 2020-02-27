/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.data;

/**
 *
 * @author Administrator
 */
public class SSPDevInfo {

    public SSEquipmentInfo eia;
    public int index;

    public class SSEquipmentInfo {
    public String DeviceName = "Unknow";
    public String Hardversion = "Unknow";
    public String SoftwareVersion = "Unknow";
    public String BuildSerialNum = "Unknow";
    public String BuildDate = "Unknow";
    
    public boolean SameAs(SSEquipmentInfo other){
        return this.DeviceName.contentEquals(other.DeviceName) &&
                this.Hardversion.contentEquals(other.Hardversion) &&
                this.SoftwareVersion.contentEquals(other.SoftwareVersion) &&
                this.BuildSerialNum.contentEquals(other.BuildSerialNum) &&
                this.BuildDate.contentEquals(other.BuildDate);
    }
}
    
    public boolean SameAs(SSPDevInfo other) {
        if (other == null) {
            return false;
        }

        return this.eia.SameAs(other.eia)
                && this.index == (other.index);
    }
}
