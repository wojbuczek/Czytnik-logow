/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzip2;

import static bzip2.BWT.codeBTW;
import static bzip2.BWT.decodeBWT;
import static bzip2.Huffman.codeHUFF;
import static bzip2.Huffman.decodeHuff;
import static bzip2.MTF.codeMTF;
import static bzip2.MTF.decodeMTF;
import czytnik.logow.AlgorytmKompresji;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Wojciech
 */
public class bzip2 implements AlgorytmKompresji {

    public final static int ByteSize = 256;
    
    public static int primaryIndex;
    public static int lengthOfDict;
    public static ArrayList<Byte> Dict;
    
    public static int progress;
    
    @Override
    public byte[] dekompresuj(byte[] daneSkompresowane) {
        
        
        //System.out.println("Rozpoczynam dekodowanie Bzip2");
        
        
        Dict.clear();
        primaryIndex=0;
        int k = daneSkompresowane[0];
        for(int i=0; i<k; i++){
            primaryIndex += (int) (daneSkompresowane[i+1]*Math.pow(128, i));
        }
        
        lengthOfDict = daneSkompresowane[k+1];
        
        for(int i=k+2; i<lengthOfDict+k+2; i++){
           Dict.add( daneSkompresowane[i] );
        }
        
        //System.out.println(Arrays.toString(daneSkompresowane));
        //System.out.println(Dict.toString());
        //System.out.println(2+lengthOfDict+" "+daneSkompresowane[2+lengthOfDict]);
        //Huffman decode
        
        //System.out.println("Dekodowanie Huffmana");
        int []resHuff = decodeHuff(daneSkompresowane);
        
        //Move To Front decode
        //System.out.println("Dekodowanie Move To Front");
        int resMTF[] = decodeMTF( resHuff);
                
        //BWT decode
        //System.out.println("Dekodowanie BWT");
        int resBWT[] = decodeBWT( resMTF);
        byte res[] = new byte[resBWT.length];
        for(int i=0; i<resBWT.length; i++)
            res[i]=Byte.parseByte(""+resBWT[i]);
        
        //System.out.println("Zakończyłem dekodowanie Bzip2");
        return res;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public byte[] kompresuj(byte[] dane) {
        progress=0;
        lengthOfDict=0;
        Dict = new ArrayList<>();
        ArrayList<Byte> result = new ArrayList<Byte>();
        
        
        //System.out.println("Rozpoczynam kodowanie Bzip2");
        //Transformata Burrowsa-Wheelera
        //System.out.println("Transformata Burrowsa-Wheelera");
        int[] resBWT = codeBTW(dane);

        //Move To Front
        //System.out.println("Move To Front");
        int[] resMTF = codeMTF(resBWT);
        
        //Kodowanie Huffmana
        //System.out.println("Kodowanie Huffmana");
        ArrayList<Byte> resHuff = codeHUFF(resMTF);
        
        int tmp = primaryIndex;
        int k=0;
        while(tmp>0){
            k++;
            tmp/=128;
        }
        result.add(Byte.valueOf(""+k));
        while(primaryIndex>0){
            result.add(Byte.valueOf(""+primaryIndex%128));
            primaryIndex/=128;
        }
        result.add(Byte.valueOf(""+lengthOfDict));
        result.addAll(Dict);
        result.addAll(resHuff);
        
        byte[] r = new byte[result.size()];
        for(int i=0; i<r.length; i++)
            r[i]=result.get(i);
        
        //System.out.println("Zakończyłem kodowanie Bzip2");
        return r;
    }

}
