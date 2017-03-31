/*
 * Distributed Systems
 */
package museum;

import main.SimulParam;
import general_info_repo.Log;
import java.util.Random;
/**
 * Museum instance.
 * @author João Brito, 68137
 */
public class Museum implements IThieves{
    private final Log log;
    
    /**
     * Init the Museum instance.
     */
    public Museum(){
        this.log = Log.getInstance();
        for(int i=1; i<=SimulParam.N_ROOMS; i++){
            Random rand = new Random();
            //random.nextInt(max + 1 - min) + min
            int dt = rand.nextInt(SimulParam.N_MAX_DISTANCE+1-SimulParam.N_MIN_DISTANCE) + SimulParam.N_MIN_DISTANCE;
            int np = rand.nextInt(SimulParam.N_MAX_PAINTINGS+1-SimulParam.N_MIN_PAINTINGS) + SimulParam.N_MIN_PAINTINGS;
            log.initMuseum(i,dt,np);
            //System.out.println(i+" "+np);
        }
    }
    
    /**
     * Theft of a canvas from the museum. Thieves method.
     * @param id thief id.
     * @param rid room number.
     * @return '0' if there isn't more canvas.
     */
    @Override
    public synchronized int rollACanvas(int id, int rid) {
        if(log.getMuseumPaintings(rid)>0){
            log.updateMuseum(rid, log.getMuseumPaintings(rid)-1);
            log.updateAssaultPartyElemCv(id, 1);
            //System.out.println("Here");
            return 1;
        }else{
            return 0;
        }
    }
}

