/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SystemConfig {

    private Properties Config = new Properties();
    public static String ConfigDir = "./DevConfig";
    public static String ConfigFile = ConfigDir + "/SystemConfig.xml";
    public static String InternalFlag = "InternalFlag";

    public SystemConfig() {
        File file = new File(ConfigDir);
        if (!file.exists() && !file.isDirectory()) {
            System.out.println("//不存在");
            file.mkdir();
        }
    }

    public void setProperty(String key, String value) {
        Config.setProperty(key, value);
        this.SaveToFile();
    }

    public String getProperty(String key, String defvalue) {
        return Config.getProperty(key, defvalue);
    }

    public void ReadFromFile() {
        File file = new File(SystemConfig.ConfigFile);
        if (file.exists()) {
            try {
                Config.loadFromXML(new FileInputStream(file));
            } catch (IOException ex) {
                Logger.getLogger(SpectralPlatService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void SaveToFile() {
        File file = new File(SystemConfig.ConfigFile);
        try {
            Config.storeToXML(new FileOutputStream(file), "");
        } catch (IOException ex) {
            Logger.getLogger(SpectralPlatService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
