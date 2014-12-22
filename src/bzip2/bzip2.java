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

    public final static int ByteSize = 128;
    
    public static int primaryIndex;
    public static int lengthOfDict;
    public static ArrayList<Byte> Dict;
    
    @Override
    public byte[] dekompresuj(byte[] daneSkompresowane) {
        Dict.clear();
        primaryIndex = daneSkompresowane[0];
        lengthOfDict = daneSkompresowane[1];
        
        for(int i=2; i<lengthOfDict+2; i++){
           Dict.add( daneSkompresowane[i] );
        }
        
        //System.out.println(Arrays.toString(daneSkompresowane));
        //System.out.println(Dict.toString());
        //System.out.println(2+lengthOfDict+" "+daneSkompresowane[2+lengthOfDict]);
        //Huffman decode
        int []resHuff = decodeHuff(daneSkompresowane);
        
        //Move To Front decode
        char resMTF[] = decodeMTF( resHuff);
                
        //BWT decode && result
        return decodeBWT( resMTF);
    }

    @Override
    @SuppressWarnings("empty-statement")
    public byte[] kompresuj(byte[] dane) {
        lengthOfDict=0;
        Dict = new ArrayList<>();
        ArrayList<Byte> result = new ArrayList<Byte>();
        
        //Transformata Burrowsa-Wheelera
        char[] resBWT = codeBTW(dane);

        
        //Move To Front
        int[] resMTF = codeMTF(resBWT);
        //System.out.println(Arrays.toString(resMTF));
        //Kodowanie Huffmana
        ArrayList<Byte> resHuff = codeHUFF(resMTF);
        
        result.add(Byte.valueOf(""+primaryIndex));
        result.add(Byte.valueOf(""+lengthOfDict));
        result.addAll(Dict);
        result.addAll(resHuff);
        
        byte[] r = new byte[result.size()];
        for(int i=0; i<r.length; i++)
            r[i]=result.get(i);
        
        return r;
    }

}
