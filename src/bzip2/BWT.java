/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzip2;

import static bzip2.bzip2.ByteSize;
import static bzip2.bzip2.primaryIndex;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Wojciech
 */
public class BWT {

    public static char[] codeBTW(byte[] dane) {
        //Transformata Burrowsa-Wheelera
        Integer[] ind = new Integer[dane.length];
        char[] resBWT = new char[dane.length];

        for (int i = 0; i < ind.length; i++) {
            ind[i] = i;
        }

        //Sortowanie leksykalne indeksów
        sortLex(dane, ind);

        //Przepisywanie znaków wejściowych w nowej kolejności do tablicy wyjściowej
        for (int i = 0; i < ind.length; i++) {
            int Index = (ind[i] + dane.length - 1) % dane.length;
            resBWT[i] = (char) dane[Index];
        }

        //Znajdywanie indeksu pierwszego znaku danych wejściowych w danych wyjściwoych
        for (int i = 0; i < ind.length; i++) {
            if (ind[i] == 1) {
                primaryIndex = i;
                break;
            }
        }
        return resBWT;
    }

    public static byte[] decodeBWT(char[] BWT) {
        int[] liczniki = new int[ByteSize];
        byte[] clone = new byte[BWT.length];
        char[] BWT2 = new char[BWT.length];
        int[] inds = new int[BWT.length];
        for (int i = 0; i < liczniki.length; i++) {
            liczniki[i] = 0;
        }

        for (int i = 0; i < BWT.length; i++) {
            liczniki[BWT[i]]++;
            clone[i] = (byte) BWT[i];
        }
        
        for (int i = 0, x=0; i < liczniki.length; i++) {
            for(int j=0; j<liczniki[i]; j++)
                    BWT2[x++] = (char) i;
        }
        
        
        for (int i = 0, j=0; i < liczniki.length; i++) 
            {
                while(j<BWT.length && i>BWT2[j])
                    j++;
                liczniki[i]=j;
            }
        for(int i=0; i<inds.length; i++)
            inds[liczniki[BWT[i]]++] = i;
        
        int i = 0;
        int k = primaryIndex;
        while (i < clone.length) {
            clone[i++] = (byte) BWT2[k];
            k = inds[k];
        }
        return clone;
    }

    private static void sortLex(byte[] dane, Integer[] res) {

        final byte[] tmp = dane;
        Comparator<Integer> lexComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer t, Integer t1) {
                int ac = tmp.length;
                while (tmp[t] == tmp[t1]) {
                    if (++t == tmp.length) {
                        t = 0;
                    }
                    if (++t1 == tmp.length) {
                        t1 = 0;
                    }
                    if (--ac == 0) {
                        return 0;
                    }
                }
                return tmp[t] - tmp[t1];
            }
        };
        Arrays.sort(res, lexComparator);
    }

}