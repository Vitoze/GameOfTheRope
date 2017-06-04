package serverSide.control_collect_site;

import interfaces.ControlCollectionSiteInterface;
import interfaces.LogInterface;
import structures.enumerates.MasterState;
import java.util.HashMap;
import structures.constants.SimulParam;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.enumerates.ThievesState;
import structures.vectorClock.VectorTimestamp;

/**
 *  Control and Collection Site instance.
 *  @author João Brito, 68137
 */
public class ControlCollectionSite implements ControlCollectionSiteInterface {
    /**
     * The Boolean variable represents whether the canvas was collected or not.
     */
    private boolean canvasCollected = false;
    /**
     * Hash table represents the museum.
     */
    private final HashMap<Integer, Boolean> museum;
    /**
     * Number of the element to wait.
     */
    private int nElemToWait = 0;
    
    private int elemParty1 = 3;
    private int elemParty2 = 3;
    private VectorTimestamp clocks;
    private final LogInterface log;
    
    /**
     * Init the Control Site instance.
     * @param log
     */
    public ControlCollectionSite(LogInterface log){
        museum = new HashMap<>();
        this.log = log;
        this.clocks = new VectorTimestamp(7, 0);
    }

    /**
     * In Master life cycle, transition between "Planning the heist" and "Deciding what to do",
     * initiates a heist. Master method.
     */
    @Override
    public synchronized VectorTimestamp startOperations(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        for(int rid=1; rid<=SimulParam.N_ROOMS; rid++){
            museum.put(rid, true);
        }
        
        newHeist(clocks.clone());
        setMasterState(MasterState.DECIDING_WHAT_TO_DO, clocks.clone());
        return clocks.clone();
    }

    /**
     * The master will wait for the assault party arrival. Master method.
     */
    @Override
    public synchronized VectorTimestamp takeARest(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        while(!canvasCollected){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setMasterState(MasterState.DECIDING_WHAT_TO_DO, clocks.clone());
        return clocks.clone();
    }

    /**
     * The Master decides what to do next
     * @return 1 to prepare new assault or 2 to end heist. Master method.
     */
    @Override
    public synchronized int[] appraiseSit() throws RemoteException {
        nElemToWait = 6;
        elemParty1 = 3;
        elemParty2 = 3;
        canvasCollected = false;
        initAssaultPartyElemId();
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
            setAssaultPartyAction(assault_party1_rid, assault_party2_rid);
            return decision;
        }else{
            for(int rid=assault_party1_rid+1; rid<=SimulParam.N_ROOMS; rid++){
                if(museum.get(rid)){
                    assault_party2_rid = rid;
                    break;
                }
            }
        }
        setAssaultPartyAction(assault_party1_rid, assault_party2_rid);
        decision[1] = 1;
        return decision;
    }

    /**
     * The thief will hand a canvas. Thieves method.
     * @param id thief id.
     * @param rid room number.
     * @param cv has canvas? 0 or 1.
     */
    @Override
    public synchronized VectorTimestamp handACanvas(int id, int party, int rid, int cv, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        if(cv==0){
            museum.replace(rid, false);
        }
        nElemToWait--;
        if(nElemToWait==0){ 
            canvasCollected = true;
            notify();
        }
        updateThiefSituation(id, 'W');
        updateAssaultPartyElemCv(id, 0, clocks.clone());
        updateAssaultPartyElemId(party, id);
        if(party==1){
            elemParty1--;
            if(elemParty1==0){
                setAssaultParty1RoomId(0);
            }
        }else{
            elemParty2--;
            if(elemParty2==0){
                setAssaultParty2RoomId(0);
            }
        }
        return clocks.clone();
    }

    @Override
    public void newHeist(VectorTimestamp vt) throws RemoteException {
        log.newHeist(vt);
    }

    @Override
    public void setMasterState(MasterState state, VectorTimestamp vt) throws RemoteException {
        log.setMasterState(state,vt);
    }

    @Override
    public void initAssaultPartyElemId() {
        log.initAssaultPartyElemId();
    }

    @Override
    public void setAssaultPartyAction(int rid1, int rid2) {
        log.setAssaultPartyAction(rid1, rid2);
    }

    @Override
    public void updateThiefSituation(int id, char s) {
        log.updateThiefSituation(id, s);
    }

    @Override
    public void updateAssaultPartyElemCv(int id, int cv, VectorTimestamp vt) throws RemoteException {
        log.updateAssaultPartyElemCv(id, cv, vt);
    }

    @Override
    public void setAssaultParty1RoomId(int rid) {
        log.setAssaultParty1RoomId(rid);
    }

    @Override
    public void setAssaultParty2RoomId(int rid) {
        log.setAssaultParty2RoomId(rid);
    }

    @Override
    public void updateAssaultPartyElemId(int party, int id) {
        log.updateAssaultPartyElemId(party, id);
    }

    @Override
    public void initThieves(ThievesState state, int id, char s, int md) throws RemoteException {
        log.initThieves(state, id, s, md);
    }

    @Override
    public void printResults() {
        log.printResults();
    }

    @Override
    public void setAssaultPartyMember(int party, int i, int id) {
        log.setAssaultPartyMember(party, i, id);
    }

    @Override
    public void setThiefState(ThievesState state, int id, VectorTimestamp vt) throws RemoteException {
        log.setThiefState(state, id, vt);
    }

    @Override
    public int getAssaultParty1RoomId() {
        return log.getAssaultParty1RoomId();
    }

    @Override
    public int getAssaultParty2RoomId() {
        return log.getAssaultParty2RoomId();
    }

    @Override
    public int getRoomDistance(int roomId) {
        return log.getRoomDistance(roomId);
    }

    @Override
    public int getAssaultPartyElemId(int party, int i) {
        return log.getAssaultPartyElemId(party, i);
    }

    @Override
    public int getAssaultPartyElemPosition(int id) {
        return log.getAssaultPartyElemPosition(id);
    }

    @Override
    public int getThiefMaxDisplacement(int id) {
        return log.getThiefMaxDisplacement(id);
    }

    @Override
    public void updateAssautPartyElemPosition(int id, int pos, VectorTimestamp vt) throws RemoteException {
        log.updateAssautPartyElemPosition(id, pos, vt);
    }

    @Override
    public void initMuseum(int id, int dt, int np) {
        log.initMuseum(id, dt, np);
    }

    @Override
    public int getMuseumPaintings(int rid) {
        return log.getMuseumPaintings(rid);
    }

    @Override
    public void updateMuseum(int rid, int np) {
        log.updateMuseum(rid, np);
    }
    
         
}
