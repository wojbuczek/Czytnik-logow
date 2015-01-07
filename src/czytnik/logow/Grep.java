/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package czytnik.logow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author Adam
 */
public class Grep {

    final byte[] dane;

    public Grep(byte[] dane) {
        this.dane = dane;
    }

    public void initGrep() {
        System.out.print("Enter your regular expression: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String inputText = null;

        try {
            inputText = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO error trying to read your regex!");
            System.exit(1);
        }
        processRegularExpression(inputText);
    }

    public void processRegularExpression(String regex) {

        Pattern p = Pattern.compile(regex);
        Matcher m;
        String[] tab = (new String(dane)).split("\n");
        int total = 0;

        for (String value : tab) {

            m = p.matcher(value);

            int count = 0;
            while (m.find()) {
                if (count == 0) {
                    System.out.println(value);
                }
                count++;
                total++;
                /*
                System.out.println("Match number: "
                        + count);
                System.out.println("Start: "
                        + m.start());
                System.out.println("End: "
                        + m.end());
                */
            }
        }
        if (total > 0) {
            System.out.println();
            if (total == 1) {
                System.out.println("Found " + total + " matching patern in total.");
            } else {
                System.out.println("Found " + total + " matching paterns in total.");
            }
            System.out.println();
        } else {
            System.out.println();
            System.out.println("Not found matching patterns!");
            System.out.println();
        }

    }
}
