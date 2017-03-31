/*
 * Distributed Systems
 */
package assault_party;

import entities.MasterState;
import entities.ThievesState;
import general_info_repo.Log;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Assault party#1 instance.
 * @author João Brito, 68137
 */
public class AssaultParty implements IMaster, IThieves{
    private boolean partyReady = false;
    private int party_number = 0;
    private int counterToCrawlBack = 0;
    private int room_id;
    private int nElemParty = 0;
    private int roomDistance = 0;
    private final LinkedList<Integer> nextElem;
    private final Log log;
    
    /**
     * Init the assault party#1.
     */
    public AssaultParty(){
        this.log = Log.getInstance();
        nextElem = new LinkedList<>();
    }
    
    /**
     * The thieves will wait for the Master to send the assault party. Thieves method.
     * @param id thief id.
     * @param md thied max displacement.
     * @return room number to assault.
     */
    @Override
    public synchronized int waitForSendAssaultParty(int id, int md) {
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
    
    /**
     * The Master will send the assault party. Master method.
     */
    @Override
    public synchronized void sendAssaultParty(int aid) {
        party_number = aid;
        partyReady = false;
        if(party_number == 1){
            room_id = log.getAssaultParty1RoomId();
        }else{
            room_id = log.getAssaultParty2RoomId();
        }
        roomDistance = log.getRoomDistance(room_id);
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
            nextElem.add(this.log.getAssaultPartyElemId(1, 1));
            nextElem.add(this.log.getAssaultPartyElemId(1, 2));
            nextElem.add(this.log.getAssaultPartyElemId(1, 3));
        }else{
            nextElem.add(this.log.getAssaultPartyElemId(2, 1));
            nextElem.add(this.log.getAssaultPartyElemId(2, 2));
            nextElem.add(this.log.getAssaultPartyElemId(2, 3));
        }
        log.setMasterState(MasterState.WAITING_FOR_GROUP_ARRIVAL);
        notifyAll();
    }
    
    /**
     * Checks if thief is at the museum. Thieves method.
     * @param id thief id.
     * @return true or false.
     */
    @Override
    public synchronized boolean atMuseum(int id) {
        if(log.getAssaultPartyElemPosition(id) == roomDistance){
            log.setThiefState(ThievesState.AT_A_ROOM, id);
            return true;
        }
        return false;
    }

    /**
     * The thieves wait for another thief to crawl. Thieves method.
     * @param id thief id.
     */
    @Override
    public synchronized void waitForMember(int id) {
        while(id!=nextElem.getFirst()){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Implements the crawl in movement. Thieves method.
     * @param id thief id.
     */
    @Override
    public synchronized void crawlIn(int id) {
        int nextPosition;
        int nextElemToCrawlPosition = this.log.getAssaultPartyElemPosition(id);
        int nextElemToCrawlMaxDispl = this.log.getThiefMaxDisplacement(id);
        int lastElemToCrawlPosition = this.log.getAssaultPartyElemPosition(nextElem.getLast());
        
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
        this.log.updateAssautPartyElemPosition(id, nextPosition);
        notifyAll();
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
            elem_id=this.log.getAssaultPartyElemId(party_number, i);
            if(elem_id!=id){
                if(this.log.getAssaultPartyElemPosition(elem_id)==pos && this.log.getAssaultPartyElemPosition(elem_id)!=roomDistance){
                    pos -= 1;
                    break;
                }
            }
        }
        return pos;
    }

    /**
     * The thieves will await for all to be prepare to crawl out. Thieves method.
     * @param id thief identification
     */
    @Override
    public synchronized void waitForReverseDirection(int id) {
        counterToCrawlBack++;
        log.setThiefState(ThievesState.CRAWLING_OUTWARDS, id);
        notifyAll();
        while(counterToCrawlBack<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Checks if a thief is at the concentration site. Thieves method.
     * @param id thief id.
     * @return true or false.
     */
    @Override
    public synchronized boolean atConcentration(int id) {
        if(this.log.getAssaultPartyElemPosition(id) == 0){
            log.setThiefState(ThievesState.OUTSIDE, id);
            return true;
        }
        return false;
    }
    
    /**
     * Simulates the crawl movement back to the concentration site.
     * @param id thief id.
     */
    @Override
    public synchronized void crawlOut(int id) {
        int nextPosition;
        int nextElemToCrawlPosition = this.log.getAssaultPartyElemPosition(id);
        int nextElemToCrawlMaxDispl = this.log.getThiefMaxDisplacement(id);
        int lastElemToCrawlPosition = this.log.getAssaultPartyElemPosition(nextElem.getLast());
        
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
        this.log.updateAssautPartyElemPosition(id, nextPosition);
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
            elem_id=this.log.getAssaultPartyElemId(1, i);
            if(elem_id!=id && elem_id!=0){
                if(this.log.getAssaultPartyElemPosition(elem_id)==pos && this.log.getAssaultPartyElemPosition(elem_id)!=0){
                    pos += 1;
                    break;
                }
            }
        }
        return pos;
    }

}