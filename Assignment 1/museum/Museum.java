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
    private final Log log;
    
    public Museum(){
        this.log = Log.getInstance();
        for(int i=1; i<=Heist.N_ROOMS; i++){
            Random rand = new Random();
            //random.nextInt(max + 1 - min) + min
            int dt = rand.nextInt(Heist.N_MAX_DISTANCE+1-Heist.N_MIN_DISTANCE) + Heist.N_MIN_DISTANCE;
            int np = rand.nextInt(Heist.N_MAX_PAINTINGS+1-Heist.N_MIN_PAINTINGS) + Heist.N_MIN_PAINTINGS;
            log.initMuseum(i,dt,np);
            //System.out.println(i+" "+np);
        }
    }

    @Override
    public synchronized int rollACanvas(int id, int rid) {
        if(log.getMuseumPaintings(rid)>0){
            log.updateMuseum(rid, log.getMuseumPaintings(rid)-1);
            //System.out.println("Here");
            return 1;
        }else{
            return 0;
        }
    }
}

