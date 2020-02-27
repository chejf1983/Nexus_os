/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.std;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import sps.dev.data.SSLinearParameter;

/**
 *
 * @author Administrator
 */
public class LinearParameterHelper {

    public static String fileEndMark = ".NCC";

    public static SSLinearParameter ReadLinearParameter(File file) throws Exception {
        if (!file.getPath().endsWith(fileEndMark)) {
            throw new Exception("Unkown File");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        int Type = (int) FindNumber("Type", reader);
        int ModeNumer = (int) FindNumber("ModeNum", reader);
        SSLinearParameter par = new SSLinearParameter(ModeNumer);
        par.SynType = Type;

        for (int nodeIndex = 0; nodeIndex < par.NodeNumber; nodeIndex++) {
            float wave = FindNumber("WL", reader);
            int kNumber = (int) FindNumber("KNum", reader);

            float[] tmp_adValue_array = new float[kNumber];
            float[] tmp_kpar_array = new float[kNumber];

            for (int adIndex = 0; adIndex < kNumber; adIndex++) {
                String result;
                if ((result = reader.readLine()) != null) {
                    String[] wavenode = result.split("#");

                    tmp_adValue_array[adIndex] = Float.parseFloat(wavenode[0]);
                    tmp_kpar_array[adIndex] = Float.parseFloat(wavenode[1]);
                } else {
                    reader.close();
                    throw new Exception(" Cant read ADValue and K parameter");
                }
            }
            par.NodeArray[nodeIndex] = par.new LinearParNode(wave, kNumber, tmp_adValue_array, tmp_kpar_array);
        }

        reader.close();
        return par;
    }

    private static float FindNumber(String Key, BufferedReader reader) throws Exception {
        String readLine = reader.readLine();
        if (!readLine.startsWith(Key)) {
            reader.close();
            throw new Exception("Error SynParameter File, Cant found " + Key);
        }

        return Float.valueOf(readLine.substring(readLine.indexOf(":") + 1));
    }

    public static void WriteLinearParameter(SSLinearParameter par, File dstfile) throws Exception {
        if (!dstfile.getPath().endsWith(fileEndMark)) {
            throw new Exception("Unkown File");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(dstfile));
        writer.write("Type:" + par.SynType);
        writer.newLine();
        writer.write("ModeNum:" + par.NodeNumber);
        writer.newLine();

        for (int nodeIndex = 0; nodeIndex < par.NodeNumber; nodeIndex++) {
            writer.write("WL:" + par.NodeArray[nodeIndex].NodeWave);
            writer.newLine();
            writer.write("KNum:" + par.NodeArray[nodeIndex].NodeKNumber);
            writer.newLine();

            for (int adIndex = 0; adIndex < par.NodeArray[nodeIndex].NodeKNumber; adIndex++) {
                writer.write(par.NodeArray[nodeIndex].ADValueArray[adIndex] + "#" + par.NodeArray[nodeIndex].KParArray[adIndex]);
                writer.newLine();
            }
        }

        writer.flush();
        writer.close();
    }

    public static SSLinearParameter BuildLinearParameter(int partype, LinearNodePar[] nodelist) {
        SSLinearParameter lpar = new SSLinearParameter(nodelist.length);
        lpar.SynType = partype;

        for (int i = 0; i < nodelist.length; i++) {
            LinearNodePar node = nodelist[i];

            //计算单个node
            float[] karray = new float[node.ADvalue.length];
            int index = 0;
            float maxad = node.ADvalue[index];
            
            for (int j = 0; j < karray.length; j++) {
                //计算AD与积分时间的比例
                if (node.itime[j] == 0) {
                    karray[j] = 0;
                } else {
                    karray[j] = node.ADvalue[j] / node.itime[j];
                }
                
                //寻找最大AD值
                if(node.ADvalue[j] > maxad){
                    index = j;
                    maxad = node.ADvalue[j];
                }
            }
            
            //以最大AD为标准，计算系数
            float maxk = karray[index];
            for (int j = 0; j < karray.length; j++) {
                karray[j] = maxk / karray[j];
            }

            lpar.NodeArray[i] = lpar.new LinearParNode(node.NodeID, node.ADvalue.length, node.ADvalue, karray);
        }

        return lpar;
    }
}
