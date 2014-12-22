/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package czytnik.logow;

import bzip2.bzip2;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Wojciech
 */
public class CzytnikLogow {

    /**
     * @param args the command line arguments
     */
    public static void testBzip2() throws IOException{
        byte[] daneWej = Files.readAllBytes(Paths.get("plik.txt"));
        int wagaPocz = daneWej.length;
        int wagaKon;
        
        AlgorytmKompresji k = new bzip2();
        
        byte[] daneWyj = k.kompresuj(daneWej);
        wagaKon = daneWyj.length;
        
        Path path = Paths.get("plik.wojb");
        Files.write(path, daneWyj);
        
        byte[] daneWej2 = Files.readAllBytes(Paths.get("plik.wojb"));
        
        byte[] daneWyj2 = k.dekompresuj(daneWej2);
        
        //Wypisanie danych wyjścjiowych po dekompresji
        /*for(int i=0; i<daneWyj2.length; i++)
        System.out.print( ((char)daneWyj2[i]) );
        System.out.println();*/
        System.out.println("----------Bzip2---------");
        System.out.println("Waga danych wejściowych: "+wagaPocz+" b");
        System.out.println("Waga danych wyjściowych: "+wagaKon+" b");
        if(wagaPocz < wagaKon)
            System.out.println("Kompresja nie zaszła");
        else
            System.out.println("Kompresja miała miejsce");
        System.out.println("------------------------");
    }
    
    
    public static void main(String[] args) throws IOException{
        testBzip2();
    }
    
    
}
