/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package czytnik.logow;

import static czytnik.logow.TestyAlgorytmowKompresji.testBzip2;
import static czytnik.logow.TestyAlgorytmowKompresji.testLZW;
import java.io.IOException;

/**
 *
 * @author Wojciech
 */
public class CzytnikLogow {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException{
        
        testBzip2();
        testLZW();
    }
    
    
}
