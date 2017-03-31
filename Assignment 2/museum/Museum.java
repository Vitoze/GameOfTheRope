/*
 * Distributed Systems
 */
package museum;

import general_info_repo.Heist;
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
        for(int i=1; i<=Heist.init.getN_rooms(); i++){
            Random rand = new Random();
            //random.nextInt(max + 1 - min) + min
            int dt = rand.nextInt(Heist.init.getN_max_distance()+1-Heist.init.getN_min_distance()) + Heist.init.getN_min_distance();
            int np = rand.nextInt(Heist.init.getN_max_paintings()+1-Heist.init.getN_min_paintings()) + Heist.init.getN_max_paintings();
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
            //System.out.println("Here");
            return 1;
        }else{
            return 0;
        }
    }
}

