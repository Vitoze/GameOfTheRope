package clientSide.Thieves;

import structures.constants.SimulParam;
import java.util.Random;

/**
 * Thieves's main class.
 * @author João Brito
 */
public class ThievesMain {
 
    public static void main(String[] args){
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println("******************************************************************\nEntity thieves has started!");
        System.out.println("******************************************************************");
        
        int nThieves = SimulParam.N_THIEVES;
        Thieves[] thieves = new Thieves[nThieves];
        for(int i = 0; i<nThieves; i++){
            Random rand = new Random();
            int md = rand.nextInt(SimulParam.THIEF_MAX_MD+1-SimulParam.THIEF_MIN_MD) + SimulParam.THIEF_MIN_MD;
            thieves[i] = new Thieves(i+1,md);
        }
        
        for(int i=0; i<nThieves; i++){
            thieves[i].start();
        }
        
        for(int i=0; i<nThieves; i++){
            try{
                thieves[i].join();
                System.err.println("Thief Died: " + i);
            }catch(InterruptedException ex){}
        }
        
    }
}
