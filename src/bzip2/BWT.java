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

    public static int[] codeBTW(byte[] daneByte) {
        //Transformata Burrowsa-Wheelera
        Integer[] ind = new Integer[daneByte.length];
        int[] resBWT = new int[daneByte.length];
        int dane[] = new int[daneByte.length];

        for (int i = 0; i < ind.length; i++) {
            ind[i] = i;
            dane[i]=daneByte[i]+128;
        }

        //Sortowanie leksykalne indeksów
        sortLex(dane, ind);

        //Przepisywanie znaków wejściowych w nowej kolejności do tablicy wyjściowej
        for (int i = 0; i < ind.length; i++) {
            int Index = (ind[i] + dane.length - 1) % dane.length;
            resBWT[i] = dane[Index];
        }

        //Znajdywanie indeksu pierwszego znaku danych wejściowych w danych wyjściwoych
        for (int i = 0; i < ind.length; i++) {
            if (ind[i] == 1) {
                primaryIndex=i;
                break;
            }
        }
        
        return resBWT;
    }

    public static int[] decodeBWT(int[] BWT) {
        int[] liczniki = new int[ByteSize];
        int[] clone = new int[BWT.length];
        int[] BWT2 = new int[BWT.length];
        int[] inds = new int[BWT.length];
        for (int i = 0; i < liczniki.length; i++) {
            liczniki[i] = 0;
        }
        
        for (int i = 0; i < BWT.length; i++) {
            liczniki[BWT[i]]++;
            clone[i] = BWT[i];
            BWT2[i] = BWT[i];
        }
        int x = 0;
        for (int i = 0; i < liczniki.length; i++) {
            if (liczniki[i] > 0) {
                while (liczniki[i]-- > 0) {
                    BWT[x++] =  i;
                }
            }
        }
        for (int i = 0; i < BWT.length; i++) {
            for (int j = 0; j < BWT.length; j++) {
                if (clone[j] != -1 && BWT[i] == clone[j]) {
                    clone[j] = -1;
                    inds[i] = j;
                    break;
                }
            }
        }

        int i = 0;
        int k = primaryIndex;
        while (i < clone.length) {
            clone[i++] = BWT2[k];
            k = inds[k];
        }
        
        for (i = 0; i < clone.length; i++) {
            clone[i]=clone[i]-128;
        }
        return clone;
    }

    private static void sortLex(int[] dane, Integer[] res) {

        final int[] tmp = dane;
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
