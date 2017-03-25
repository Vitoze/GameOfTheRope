/*
 * Distributed Systems
 */
package assault_party1;

import general_info_repo.Log;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Brito
 */
public class AssaultParty1 implements IMaster, IThieves{
    private boolean partyReady = false;
    private boolean first = true;
    private int counterToCrawlBack = 0;
    private int lastElemToCrawl = 0;
    private int nextElemToCrawl = 0;
    private int room_id;
    private int nElemParty = 0;
    private int roomDistance = 0;
    private final Log log;
    
    public AssaultParty1(){
        this.log = Log.getInstance();
    }
    
    @Override
    public synchronized int waitForSendAssaultParty(int id, int md) {
        System.out.println(partyReady);
        partyReady=false;
        if(nElemParty==3){
            first = true;
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
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return room_id;
    }
    
    @Override
    public synchronized void sendAssaultParty() {
        partyReady = false;
        room_id = log.getAssaultParty1RoomId();
        roomDistance = log.getRoomDistance(room_id);
        System.out.println("Master " +nElemParty);
        while(this.nElemParty<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        partyReady = true;
        this.nextElemToCrawl = this.log.getAssaultPartyElemId(1, 1);
        this.lastElemToCrawl = this.log.getAssaultPartyElemId(1, 2);
        notifyAll();
    }
    
    @Override
    public synchronized boolean atMuseum(int id) {
        System.out.println("AP1-Musuem?");
        return true;
        //return this.log.getAssaultPartyElemPosition(id) == roomDistance;
    }

    @Override
    public synchronized void waitForMember(int id) {
        while(id!=this.nextElemToCrawl){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public synchronized void crawlIn(int id) {
        int nextPosition;
        int nextElemToCrawlPosition = this.log.getAssaultPartyElemPosition(id);
        int nextElemToCrawlMaxDispl = this.log.getThiefMaxDisplacement(id);
        int lastElemToCrawlPosition = this.log.getAssaultPartyElemPosition(this.lastElemToCrawl);
        // If pos(id)+md>pos(id-1)+3
        if(first){
            this.log.printALine();
            first = false;
        }
        if(nextElemToCrawlPosition+nextElemToCrawlMaxDispl>=lastElemToCrawlPosition+3){
            nextPosition = lastElemToCrawlPosition+3;
            if(nextPosition>=roomDistance){
                nextPosition = roomDistance;
            }
            nextPosition = checkPosition(nextPosition,id);
            setNextElemToCrawl(id);
        }else{
            nextPosition = nextElemToCrawlPosition+nextElemToCrawlMaxDispl;
            nextPosition = checkPosition(nextPosition,id);
            if(nextPosition >= roomDistance){
                nextPosition = roomDistance;
                setNextElemToCrawl(id);
            }
            nextElemToCrawl=id;
        }
        this.log.updateAssautPartyElemPosition(id, nextPosition);
        notifyAll();
    }
    
    private synchronized void setNextElemToCrawl(int id){
        int lastToCrawl = this.log.getAssaultPartyElemNumber(id);
        if(lastToCrawl==3){
            lastToCrawl = 1;
        }else{
            lastToCrawl++;
        }
        this.lastElemToCrawl = id;
        this.nextElemToCrawl = this.log.getAssaultPartyElemId(1,lastToCrawl);
        System.out.println("AP1: "+lastElemToCrawl+" "+nextElemToCrawl);
        notifyAll();
    }
    
    private int checkPosition(int pos, int id){
        int elem_id;
        for(int i=1;i<=3;i++){
            elem_id=this.log.getAssaultPartyElemId(1, i);
            if(elem_id!=id){
                if(this.log.getAssaultPartyElemPosition(elem_id)==pos && this.log.getAssaultPartyElemPosition(elem_id)!=roomDistance){
                    pos -= 1;
                    break;
                }
            }
        }
        return pos;
    }

    @Override
    public synchronized void waitForReverseDirection() {
        this.nextElemToCrawl = this.log.getAssaultPartyElemId(1, 1);
        this.lastElemToCrawl = this.log.getAssaultPartyElemId(1, 3);
        counterToCrawlBack++;
        notifyAll();
        while(counterToCrawlBack<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public synchronized boolean atConcentration(int id) {
        return this.log.getAssaultPartyElemPosition(id) == 0;
    }
    
    @Override
    public synchronized void crawlOut(int id) {
        int nextPosition;
        int nextElemToCrawlPosition = this.log.getAssaultPartyElemPosition(id);
        int nextElemToCrawlMaxDispl = this.log.getThiefMaxDisplacement(id);
        int lastElemToCrawlPosition = this.log.getAssaultPartyElemPosition(this.lastElemToCrawl);
        
        if(first){
            this.log.printALine();
            first = false;
        }
        if(nextElemToCrawlPosition-nextElemToCrawlMaxDispl<=lastElemToCrawlPosition-3){
            nextPosition = lastElemToCrawlPosition-3;
            if(nextPosition<=0){
                nextPosition = 0;
            }
            nextPosition = checkPositionBack(nextPosition,id);
            setNextElemToCrawl(id);
        }else{
            nextPosition = nextElemToCrawlPosition-nextElemToCrawlMaxDispl;
            nextPosition = checkPositionBack(nextPosition,id);
            if(nextPosition <= 0){
                nextPosition = 0;
                setNextElemToCrawl(id);
            }
        }
        this.log.updateAssautPartyElemPosition(id, nextPosition);
        notifyAll();
    }
    
    private int checkPositionBack(int pos, int id){
        int elem_id;
        for(int i=1;i<=3;i++){
            elem_id=this.log.getAssaultPartyElemId(1, i);
            if(elem_id!=id){
                if(this.log.getAssaultPartyElemPosition(elem_id)==pos && this.log.getAssaultPartyElemPosition(elem_id)!=0){
                    pos += 1;
                    break;
                }
            }
        }
        return pos;
    }

}
