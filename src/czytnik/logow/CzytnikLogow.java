/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package czytnik.logow;

import bzip2.bzip2;
import static czytnik.logow.TestyAlgorytmowKompresji.testBzip2;
import static czytnik.logow.TestyAlgorytmowKompresji.testLZW;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lzw.LZW;

/**
 *
 * @author Wojciech
 */
public class CzytnikLogow {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException{
        
        //testBzip2();
        //testLZW();
        
        String help = "Czytnik logów systemowych\n"
                + "Poprawne wywołanie programu powinno wyglądać następująco\n"
                + "./czytnik [typ kompresji] [ścieżka do pliku]\n"
                + "[typ kompresji] = -bzip2 / -lzw\n";
        
        if(args.length<2){
            System.err.println(help);
        }else{
            AlgorytmKompresji ak = null;
            if( args[0].equals("-bzip2") && 
                    args[1].substring(args[1].length()-4, args[1].length()).equals("wojb")){
                ak = new bzip2();
            }else if( args[0].equals("-lzw") && 
                    args[1].substring(args[1].length()-4, args[1].length()).equals("wrob")){
                ak = new LZW();
            }else{
                System.err.println("Złe rozszerzenie pliku\n"+help);
                System.exit(1);
            }
            byte[] daneWej = Files.readAllBytes(Paths.get(args[1]));
        
            byte[] daneWyj = ak.dekompresuj(daneWej);
            Files.write(Paths.get(args[1]+".decode"), daneWyj);
            
            Grep grep = new Grep(daneWyj);
        
            grep.initGrep();
        }
        
    }
    
    
}
