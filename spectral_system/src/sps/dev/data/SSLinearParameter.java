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
public class SSLinearParameter {

    public static int NonLinearPar = 0;
    public static int NodeLinearPar = 4;
    public static int WaveLinearPar = 3;

    public int SynType = NonLinearPar;

    public int NodeNumber = 0;
    public LinearParNode[] NodeArray;

    public SSLinearParameter(int NodeNum) {
        this.NodeNumber = NodeNum;
        this.NodeArray = new LinearParNode[NodeNum];
    }

    public SSLinearParameter(SSLinearParameter lpar) {
        this.NodeNumber = lpar.NodeNumber;
        this.SynType = lpar.SynType;
        this.NodeArray = new LinearParNode[lpar.NodeNumber];
        
        for(int i = 0; i < this.NodeArray.length; i++){
            LinearParNode node = lpar.NodeArray[i];
            float[] adarray = new float[node.ADValueArray.length];
            float[] karray = new float[node.KParArray.length];
            this.NodeArray[i] = new LinearParNode(node.NodeWave, node.NodeKNumber,
            adarray, karray);
        }
    }

    public class LinearParNode {

        public float NodeWave = 0;
        public int NodeKNumber = 0;
        public float[] ADValueArray;
        public float[] KParArray;

        public LinearParNode(float NodeWave, int NodeKNumber, float[] ADValue, float[] ADKPar) {
            this.NodeKNumber = NodeKNumber;
            this.NodeWave = NodeWave;
            this.KParArray = ADKPar;
            this.ADValueArray = ADValue;
        }

        public void SortNode() {
            for (int i = 0; i < NodeKNumber; i++) {
                for (int j = i + 1; j < NodeKNumber; j++) {
                    if (ADValueArray[i] > ADValueArray[j]) {
                        float tmp = ADValueArray[i];
                        ADValueArray[i] = ADValueArray[j];
                        ADValueArray[j] = tmp;
                        tmp = KParArray[i];
                        KParArray[i] = KParArray[j];
                        KParArray[j] = tmp;
                    }
                }
            }
        }
    }
}
