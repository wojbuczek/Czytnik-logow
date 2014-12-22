/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bzip2;

import static bzip2.bzip2.ByteSize;
import static bzip2.bzip2.Dict;
import static bzip2.bzip2.lengthOfDict;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wojciech
 */
public class Huffman {
    
    private static slownik []sl;
    
    
    public static ArrayList<Byte> codeHUFF(int[] resMTF) {
        ArrayList<Byte> resHuff = new ArrayList<Byte>();
        ArrayList<Byte> lastResult = new ArrayList<Byte>();
        //System.out.println(Arrays.toString(resMTF));
        
        int[] liczniki = new int[ByteSize];
        for (int i = 0; i < liczniki.length; i++) {
            liczniki[i] = 0;
        }

        for (int i = 0; i < resMTF.length; i++) {
            liczniki[resMTF[i]]++;
        }

        Tree HT = new Tree();
        Node root = HT.createHuffTree(liczniki);

        Node x =root;
        sl = new slownik[ByteSize];
        for (int i = 0; i < sl.length; i++) 
            sl[i] = new slownik();
        ArrayList<Byte> kod = new ArrayList<>();
        
        while (x!=null && x.l != null) {
            kod.add((byte)1);
            bzip2.Dict.add(Byte.valueOf(""+x.r.k));
            sl[x.r.k].kod = (ArrayList<Byte>) kod.clone();
            kod.remove(kod.size()-1);
            kod.add((byte)0);
            x = x.l;
            lengthOfDict++;
        }
        
        if(x!=null){
            kod.add((byte)0);
            sl[x.k].kod = (ArrayList<Byte>) kod.clone();
            lengthOfDict++;
            bzip2.Dict.add(Byte.valueOf(""+x.k));
        }
        
        for (int i = 0; i < resMTF.length; i++) {
            for(int j=0; j<sl[resMTF[i]].kod.size(); j++)
                resHuff.add(sl[resMTF[i]].kod.get(j));
        }
        //System.out.println("Max is "+Byte.MAX_VALUE+" "+resHuff.size()+"  "+resHuff);
        int i;
        for(i=0; i+7<resHuff.size(); i+=7){
            List<Byte> slowo=(resHuff.subList(i, i+7));
            //System.out.println(i+" - "+(i+7)+"  "+resHuff.subList(i, i+7));
            int wynik=0;
            for(int j=6; j>=0; j--){
                wynik+=(slowo.get(j) *Math.pow(2,6-j));
            }
            lastResult.add( Byte.valueOf(""+wynik) );
        }
            List<Byte> slowo=(resHuff.subList(i, resHuff.size()));
            //System.out.println(i+" - "+(i+7)+"  "+resHuff.subList(i, resHuff.size()));
            int wynik=0;
            for(int j=resHuff.size()-i-1; j>=0; j--){
                wynik+=(slowo.get(j)*Math.pow(2, 6-j));
            }
            
            lastResult.add( Byte.valueOf(""+wynik) );
        
        return lastResult;
    }

    public static int [] decodeHuff(byte[] daneSkompresowane){
        ArrayList<Integer> res = new ArrayList<Integer>();
        int index=lengthOfDict+2;
        int inDict=0;
        //System.out.print("[");
        for(int j=index; j<daneSkompresowane.length; j++){
            int x = daneSkompresowane[j];
            inDict--;
            int wsk = ByteSize;
            do{
                if(x<wsk){
                    inDict++;
                    if(inDict+1==lengthOfDict){
                        res.add(Dict.get(inDict).intValue());
                        //System.out.print(Dict.get(inDict).intValue()+", ");
                        inDict=-1;
                    }
                }else{
                    res.add(Dict.get(inDict).intValue());
                    //System.out.print(Dict.get(inDict).intValue()+", ");
                    inDict=0;
                    x-=wsk;
                }
                wsk/=2;
            }while(wsk!=0);
        }
        //System.out.println("]");
        
        int result[] = new int[res.size()];
        for(int j=0; j<result.length; j++)
            result[j]=res.get(j);
        return result;
    }
    
}
