/*
 *  Distributed Systems
 */
package control_collect_site;

import java.util.HashMap;
import general_info_repo.Log;
import general_info_repo.Heist;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Control and Collection Site instance.
 *  @author João Brito
 */
public class ControlCollectionSite implements IMaster, IThieves {
    private boolean canvasCollected = false;
    private HashMap<Integer, Boolean> museum;
    private int nElemToWait = 0;
    private final Log log;
    
    public ControlCollectionSite(){
        log = Log.getInstance();
        museum = new HashMap<>();
    }
    
    /**
     * In Master life cycle, transition between "Planning the heist" and "Deciding what to do",
     * initiates a heist.
     */
    @Override
    public void startOperations() {
        for(int rid=1; rid<=Heist.N_ROOMS; rid++){
            museum.put(rid, true);
        }
    }
    
    /**
     * The Master decides what to do next
     * @return 1 to prepare new assault or 2 to end heist 
     */
    @Override
    public synchronized int[] appraiseSit() {
        nElemToWait = 6;
        canvasCollected = false;
        int assault_party1_rid = 0;
        int assault_party2_rid = 0;
        int decision[] = new int[2];
        decision[0] = 1;
        for(int rid=1; rid<=Heist.N_ROOMS; rid++){
            if(museum.get(rid)){
                assault_party1_rid = rid;
                break;
            }
            assault_party1_rid = 0;
        }
        if(assault_party1_rid==0){
            decision[0] = 2;
            return decision;
        }
        if(assault_party1_rid==Heist.N_ROOMS){
            assault_party2_rid = decision[1] = 0;
            nElemToWait = 3;
            log.setAssaultPartyAction(assault_party1_rid, assault_party2_rid);
            return decision;
        }else{
            for(int rid=assault_party1_rid+1; rid<=Heist.N_ROOMS; rid++){
                if(museum.get(rid)){
                    assault_party2_rid = rid;
                    break;
                }
            }
        }
        log.setAssaultPartyAction(assault_party1_rid, assault_party2_rid);
        decision[1] = 1;
        return decision;
    }
    
    @Override
    public synchronized void takeARest() {
        while(!canvasCollected){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public synchronized void handACanvas(int id, int rid, int cv) {
        if(cv==0){
            museum.replace(rid, false);
        }
        nElemToWait--;
        if(nElemToWait==0){ 
            canvasCollected = true;
            notify();
        }
        log.updateAssaultPartyElemCv(id, 0);
    }
       
}
