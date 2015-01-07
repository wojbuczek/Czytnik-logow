/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lzw;

import czytnik.logow.AlgorytmKompresji;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author Krzysztof
 */
public class LZW implements AlgorytmKompresji {

    @Override
    public byte[] kompresuj(byte[] dane) {
        int size = 0;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        //
        String line = new String(dane);
        String temp;
        ArrayList<String> dic = new ArrayList<String>();
        int index = 0;
        for (; index <= 127; index++) {
            dic.add(String.valueOf((char) index));
        }
        ArrayList<Integer> values = new ArrayList<Integer>();

        int i = 0;
        while (i < line.length()) {
            temp = line.substring(i, i + 1);
            int j = i + 1;
            for (; dic.contains(temp) && j < line.length(); j++) {
                temp = line.substring(i, j + 1);
            }

            // put in the dic from i to j+1
            dic.add(temp);

            // get from the dic from i to j
            temp = line.substring(i, j - 1);
            values.add(dic.indexOf(temp));
            size = Math.max(size, dic.indexOf(temp));
            i = j - 1;
            if (dic.contains(line.substring(i, line.length()))) {
                values.add(dic.indexOf(line.substring(i, line.length())));
                size = Math.max(size, dic.indexOf(dic.indexOf(line.substring(i, line.length()))));
                break;
            }

        }
        
        BitOutputStream bos = new BitOutputStream(bout);
        ListIterator<Integer> iter = values.listIterator();

        String binary = Integer.toBinaryString(size);
        
        size = binary.length();
        bos.write(5, size);
        while (iter.hasNext()) {
            bos.write(size, iter.next());
        }

        bos.close();
        
        return bout.toByteArray();
    }

    @Override
    public byte[] dekompresuj(byte[] daneSkompresowane) {
        int size;
        InputStream bin = new ByteArrayInputStream(daneSkompresowane);
        //ByteArrayOutputStream bout = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder();
        
        ArrayList<Integer> values = new ArrayList<Integer>();
        try {
            BitInputStream bis = new BitInputStream(bin);
            size = bis.readBits(5);
            int temp;
            while ((temp = bis.readBits(size)) != -1) {
                values.add(temp);
            }
            bis.close();
        } catch (IOException e) {
        }
        
        ListIterator<Integer> it = values.listIterator();
        String temp;
        int tempVal;

        ArrayList<String> dic = new ArrayList<>();
        int index = 0;
        for (; index <= 127; index++) {
            dic.add(String.valueOf((char) index));
        }

        String prev = dic.get(it.next());
        temp = "";
        temp += prev;
        while (it.hasNext()) {
            tempVal = it.next();
            if (tempVal < dic.size()) {
                dic.add(prev + dic.get(tempVal).charAt(0));
                prev = dic.get(tempVal);
                temp += prev;
            } else {
                prev = prev + prev.charAt(0);
                dic.add(prev);
                temp += prev;
            }
        }
        sb.append(temp);

        return sb.toString().getBytes();
    }
}
