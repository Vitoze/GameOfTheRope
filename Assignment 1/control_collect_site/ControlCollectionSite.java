/*
 *  Distributed Systems
 */
package control_collect_site;

import entities.MasterState;
import java.util.HashMap;
import general_info_repo.Log;
import main.SimulParam;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Control and Collection Site instance.
 *  @author João Brito, 68137
 */
public class ControlCollectionSite implements IMaster, IThieves {
    private boolean canvasCollected = false;
    private final HashMap<Integer, Boolean> museum;
    private int nElemToWait = 0;
    private int elemParty1 = 3;
    private int elemParty2 = 3;
    private final Log log;
    
    /**
     * Init the Control Site instance.
     */
    public ControlCollectionSite(){
        log = Log.getInstance();
        museum = new HashMap<>();
    }
    
    /**
     * In Master life cycle, transition between "Planning the heist" and "Deciding what to do",
     * initiates a heist. Master method.
     */
    @Override
    public void startOperations() {
        for(int rid=1; rid<=SimulParam.N_ROOMS; rid++){
            museum.put(rid, true);
        }
        log.newHeist();
        log.setMasterState(MasterState.DECIDING_WHAT_TO_DO);
    }
    
    /**
     * The Master decides what to do next
     * @return 1 to prepare new assault or 2 to end heist. Master method.
     */
    @Override
    public synchronized int[] appraiseSit() {
        nElemToWait = 6;
        elemParty1 = 3;
        elemParty2 = 3;
        canvasCollected = false;
        log.initAssaultPartyElemId();
        int assault_party1_rid = 0;
        int assault_party2_rid = 0;
        int decision[] = new int[2];
        decision[0] = 1;
        for(int rid=1; rid<=SimulParam.N_ROOMS; rid++){
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
        if(assault_party1_rid==SimulParam.N_ROOMS){
            assault_party2_rid = decision[1] = 0;
            nElemToWait = 3;
            log.setAssaultPartyAction(assault_party1_rid, assault_party2_rid);
            return decision;
        }else{
            for(int rid=assault_party1_rid+1; rid<=SimulParam.N_ROOMS; rid++){
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
    
    /**
     * The master will wait for the assault party arrival. Master method.
     */
    @Override
    public synchronized void takeARest() {
        while(!canvasCollected){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        log.setMasterState(MasterState.DECIDING_WHAT_TO_DO);
    }
    
    /**
     * The thief will hand a canvas. Thieves method.
     * @param id thief id.
     * @param rid room number.
     * @param cv has canvas? 0 or 1.
     */
    @Override
    public synchronized void handACanvas(int id, int party, int rid, int cv) {
        if(cv==0){
            museum.replace(rid, false);
        }
        nElemToWait--;
        if(nElemToWait==0){ 
            canvasCollected = true;
            notify();
        }
        log.updateThiefSituation(id, 'W');
        log.updateAssaultPartyElemCv(id, 0);
        log.updateAssaultPartyElemId(party, id);
        if(party==1){
            elemParty1--;
            if(elemParty1==0){
                log.setAssaultParty1RoomId(0);
            }
        }else{
            elemParty2--;
            if(elemParty2==0){
                log.setAssaultParty2RoomId(0);
            }
        }
    }
       
}
