/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museum;

import general_info_repo.Heist;
import general_info_repo.Log;
import java.util.Random;
/**
 *
 * @author João Brito
 */
public class Museum implements IThieves{
    private final Room[] museum; 
    private final Log log;
    
    public Museum(){
        this.log = Log.getInstance();
        museum = new Room[5];
        for(int i=0; i<Heist.N_ROOMS; i++){
            Random rand = new Random();
            int dt = rand.nextInt(30) + 15;
            int np = rand.nextInt(16) + 8;
            museum[i] = new Room(i+1,dt,np);
            log.initMuseum(i+1,dt);
        }
    }

    @Override
    public void rollACanvas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

