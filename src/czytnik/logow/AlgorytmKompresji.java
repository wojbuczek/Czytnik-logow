/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package czytnik.logow;

/**
 *
 * @author Wojciech
 */
public interface AlgorytmKompresji {
    public byte[] kompresuj(byte[] dane);
    public byte[] dekompresuj(byte[] daneSkompresowane);
}
