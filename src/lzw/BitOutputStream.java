/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lzw;

/**
 *
 * @author Krzysztof
 */

import java.io.*;

public class BitOutputStream extends OutputStream
{
    

    private OutputStream  myOutput;
    private int           myBuffer;
    private int           myBitsToGo;
    
    private static final int bmask[] = {
        0x00, 0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff,
        0x1ff,0x3ff,0x7ff,0xfff,0x1fff,0x3fff,0x7fff,0xffff,
        0x1ffff,0x3ffff,0x7ffff,0xfffff,0x1fffff,0x3fffff,
        0x7fffff,0xffffff,0x1ffffff,0x3ffffff,0x7ffffff,
        0xfffffff,0x1fffffff,0x3fffffff,0x7fffffff,0xffffffff
    };

    private static final int BITS_PER_BYTE = 8;
    
    public void write(int b) throws IOException {
        myOutput.write(b);
    }
    
    public BitOutputStream(OutputStream out){
        myOutput = out;
        initialize();
    }
    
    private void initialize(){
        myBuffer = 0;
        myBitsToGo = BITS_PER_BYTE;
    }
    
    public BitOutputStream(String filename)
    {
        try{
            myOutput = new BufferedOutputStream(new FileOutputStream(filename)); 
        }
        catch (FileNotFoundException fnf){
            throw new RuntimeException("could not create " + filename + " " + fnf);
        }
        catch(SecurityException se){
            throw new RuntimeException("security exception on write " + se);
        }
        initialize();
    }
    
    public void flush()
    {
        if (myBitsToGo != BITS_PER_BYTE) {
            try{
                write( (myBuffer << myBitsToGo) );
            }
            catch (java.io.IOException ioe){
                throw new RuntimeException("error writing bits on flush " + ioe);
            }
            myBuffer = 0;
            myBitsToGo = BITS_PER_BYTE;
        }
                
        try{
            myOutput.flush();    
        }
        catch (java.io.IOException ioe){
            throw new RuntimeException("error on flush " + ioe);
        }
    }
    
    public void close()
    {
        flush();
        try{
            myOutput.close();
        }
        catch (IOException ioe){
            throw new RuntimeException("error closing BitOutputStream " + ioe);
        }
    }
    
    public void write(int howManyBits, int value)
    {
        value &= bmask[howManyBits];  // only right most bits valid

        while (howManyBits >= myBitsToGo){
            myBuffer = (myBuffer << myBitsToGo) |
                       (value >> (howManyBits - myBitsToGo));
            try{
                write(myBuffer);    
            }
            catch (java.io.IOException ioe){
                throw new RuntimeException("error writing bits " + ioe);
            }

            value &= bmask[howManyBits - myBitsToGo];
            howManyBits -= myBitsToGo;
            myBitsToGo = BITS_PER_BYTE;
            myBuffer = 0;
        }
        
        if (howManyBits > 0) {
            myBuffer = (myBuffer << howManyBits) | value;
            myBitsToGo -= howManyBits;
        }
    }
}
