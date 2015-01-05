/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bzip2;

import static bzip2.bzip2.ByteSize;

/**
 *
 * @author Wojciech
 */
public class MTF {
    
    public static int[] codeMTF(int[] resBWT) {
        //Move To Front
        int[] resMTF = new int[resBWT.length];
        int states[] = new int[ByteSize];
        for (int i = 0; i < states.length; i++) {
            states[i] = i;
        }
        
        int x;
        for (int i = 0; i < resBWT.length; i++) {
            for (x = 0; x < states.length && states[x] != resBWT[i]; x++)
                ;
            if (x != 0) {
                int tmp = states[x];
                for (int j = x; j > 0; j--) {
                    states[j] = states[j - 1];
                }
                states[0] = tmp;
            }
            resMTF[i] = x;
        }
        
        return resMTF;
    }

    public static int[] decodeMTF(int[] resMTF) {
        //Move To Front
        int[] resBWT = new int[resMTF.length];
        int states[] = new int[ByteSize];
        
        for (int i = 0; i < states.length; i++) {
            states[i] = i;
        }
        int x;
        for (int i = 0; i < resBWT.length; i++) {
            resBWT[i] = states[resMTF[i]];
            int tmp = states[resMTF[i]];
            for (int j = resMTF[i]; j > 0; j--) {
                states[j] = states[j - 1];
            }
            states[0] = tmp;
        }
        return resBWT;
    }

}
