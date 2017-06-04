package serverSide.assault_party;

import interfaces.AssaultPartyInterface;
import interfaces.LogInterface;
import structures.enumerates.MasterState;
import structures.enumerates.ThievesState;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.vectorClock.VectorTimestamp;

/**
 * Assault party instance.
 * @author João Brito, 68137
 */
public class AssaultParty implements AssaultPartyInterface{
    private boolean partyReady = false;
    private int party_number = 0;
    private int counterToCrawlBack = 0;
    private int room_id;
    private int nElemParty = 0;
    private int roomDistance = 0;
    private final LinkedList<Integer> nextElem;
    private VectorTimestamp clocks;
    private final LogInterface log;
    
    /**
     * Init the assault party#1.
     */
    public AssaultParty(LogInterface log){
        nextElem = new LinkedList<>();
        this.log = log;
        this.clocks = new VectorTimestamp(7, 0);
    }

    @Override
    public synchronized VectorTimestamp sendAssaultParty(int aid, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        party_number = aid;
        partyReady = false;
        if(party_number == 1){
            room_id = getAssaultParty1RoomId();
        }else{
            room_id = getAssaultParty2RoomId();
        }
        roomDistance = getRoomDistance(room_id);
        while(this.nElemParty<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        partyReady = true;
        while(!nextElem.isEmpty()){
            nextElem.remove();
        }
        if(party_number == 1){
            nextElem.add(getAssaultPartyElemId(1, 1));
            nextElem.add(getAssaultPartyElemId(1, 2));
            nextElem.add(getAssaultPartyElemId(1, 3));
        }else{
            nextElem.add(getAssaultPartyElemId(2, 1));
            nextElem.add(getAssaultPartyElemId(2, 2));
            nextElem.add(getAssaultPartyElemId(2, 3));
        }
        setMasterState(MasterState.WAITING_FOR_GROUP_ARRIVAL, clocks.clone());
        notifyAll();
        return clocks.clone();
    }

    @Override
    public synchronized VectorTimestamp crawlIn(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        int nextPosition;
        int nextElemToCrawlPosition = getAssaultPartyElemPosition(id);
        int nextElemToCrawlMaxDispl = getThiefMaxDisplacement(id);
        int lastElemToCrawlPosition = getAssaultPartyElemPosition(nextElem.getLast());
        
        if(nextElemToCrawlPosition+nextElemToCrawlMaxDispl>=lastElemToCrawlPosition+3){
            nextPosition = lastElemToCrawlPosition+3;
            if(nextPosition>=roomDistance){
                nextPosition = roomDistance;
            }
            nextPosition = checkPosition(nextPosition,id);
            setNextElemToCrawl();
        }else{
            nextPosition = nextElemToCrawlPosition+nextElemToCrawlMaxDispl;
            nextPosition = checkPosition(nextPosition,id);
            if(nextPosition >= roomDistance){
                nextPosition = roomDistance;
                setNextElemToCrawl();
            }
        }
        updateAssautPartyElemPosition(id, nextPosition, clocks.clone());
        notifyAll();
        return clocks.clone();
    }
    
    /**
     * Set the next element to crawl.
     */
    private synchronized void setNextElemToCrawl(){
        int tmp = nextElem.getFirst();
        nextElem.pop();
        nextElem.add(tmp);
        notifyAll();
    }
    
    /**
     * Checks a thief position after crawl movement. Avoids collision.
     * @param pos thief position.
     * @param id thief id.
     * @return thief position.
     */
    private int checkPosition(int pos, int id){
        int elem_id;
        for(int i=1;i<=3;i++){
            elem_id=getAssaultPartyElemId(party_number, i);
            if(elem_id!=id){
                if(getAssaultPartyElemPosition(elem_id)==pos && getAssaultPartyElemPosition(elem_id)!=roomDistance){
                    pos -= 1;
                    break;
                }
            }
        }
        return pos;
    }

    @Override
    public synchronized VectorTimestamp crawlOut(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        int nextPosition;
        int nextElemToCrawlPosition = getAssaultPartyElemPosition(id);
        int nextElemToCrawlMaxDispl = getThiefMaxDisplacement(id);
        int lastElemToCrawlPosition = getAssaultPartyElemPosition(nextElem.getLast());
        
        if(nextElemToCrawlPosition-nextElemToCrawlMaxDispl<=lastElemToCrawlPosition-3){
            nextPosition = lastElemToCrawlPosition-3;
            if(nextPosition<=0){
                nextPosition = 0;
            }
            nextPosition = checkPositionBack(nextPosition,id);
            setNextElemToCrawl();
        }else{
            nextPosition = nextElemToCrawlPosition-nextElemToCrawlMaxDispl;
            nextPosition = checkPositionBack(nextPosition,id);
            if(nextPosition <= 0){
                nextPosition = 0;
                setNextElemToCrawl();
            }
        }
        updateAssautPartyElemPosition(id, nextPosition, clocks.clone());
        return clocks.clone();
    }
    
    /**
     * Checks a thief position after crawl movement. Avoids collision.
     * @param pos thief position.
     * @param id thief id.
     * @return thief position.
     */
    private int checkPositionBack(int pos, int id){
        int elem_id;
        for(int i=1;i<=3;i++){
            elem_id=getAssaultPartyElemId(1, i);
            if(elem_id!=id && elem_id!=0){
                if(getAssaultPartyElemPosition(elem_id)==pos && getAssaultPartyElemPosition(elem_id)!=0){
                    pos += 1;
                    break;
                }
            }
        }
        return pos;
    }

    @Override
    public synchronized int waitForSendAssaultParty(int id) throws RemoteException {
        partyReady=false;
        if(nElemParty==3){
            nElemParty=0;
            counterToCrawlBack = 0;
            notifyAll();
        }
        nElemParty++;
        notifyAll();
        while(!partyReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return room_id;
    }

    @Override
    public synchronized VectorTimestamp waitForMember(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        while(id!=nextElem.getFirst()){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return clocks.clone();
    }

    @Override
    public synchronized boolean atMuseum(int id) throws RemoteException {
        if(getAssaultPartyElemPosition(id) == roomDistance){
            setThiefState(ThievesState.AT_A_ROOM, id, clocks.clone());
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean atConcentration(int id) throws RemoteException {
        if(getAssaultPartyElemPosition(id) == 0){
            setThiefState(ThievesState.OUTSIDE, id, clocks.clone());
            return true;
        }
        return false;
    }

    @Override
    public synchronized VectorTimestamp waitForReverseDirection(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        counterToCrawlBack++;
        setThiefState(ThievesState.CRAWLING_OUTWARDS, id, clocks.clone());
        notifyAll();
        while(counterToCrawlBack<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
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