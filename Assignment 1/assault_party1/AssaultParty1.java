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
    private int roomId;
    private int nElemParty = 0;
    private int roomDistance = 0;
    private final Log log;
    
    public AssaultParty1(){
        this.log = Log.getInstance();
    }
    
    @Override
    public synchronized int waitForSendAssaultParty(int id, int md) {
        if(nElemParty==3){
            partyReady=false;
            first = true;
            nElemParty=0;
            counterToCrawlBack = 0;
            notifyAll();
        }
        nElemParty++;
        notifyAll();
        while(!this.partyReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.println(id);
        return roomId;
    }
    
    @Override
    public synchronized void sendAssaultParty(int rid, int dt) {
        partyReady = true;
        roomId = rid;
        roomDistance = dt;
        while(this.nElemParty<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.nextElemToCrawl = this.log.getAssaultPartyElemId(1, 1);
        //System.out.println("Primeiro elemento " + nextElemToCrawl);
        this.lastElemToCrawl = this.log.getAssaultPartyElemId(1, 3);
        notifyAll();
    }
    
    @Override
    public synchronized boolean atMuseum(int id) {
        return this.log.getAssaultPartyElemPosition(id) == roomDistance;
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
        //System.out.println(id+" "+nextElemToCrawl);
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
        if(nextElemToCrawlPosition+nextElemToCrawlMaxDispl>lastElemToCrawlPosition+3){
            nextPosition = lastElemToCrawlPosition+3;
            // If distance > roomPos
            if(nextPosition > roomDistance){
                nextPosition = roomDistance;
            }
            this.lastElemToCrawl = id;
            setNextElemToCrawl(id);
            //notifyAll();
        }else{
            if(nextElemToCrawlPosition+nextElemToCrawlMaxDispl==lastElemToCrawlPosition){
                if(lastElemToCrawlPosition == roomDistance){
                    nextPosition = roomDistance;
                    this.lastElemToCrawl = id;
                    setNextElemToCrawl(id);
                }else{
                    nextPosition = lastElemToCrawlPosition-1;
                }
            }else{
                nextPosition = nextElemToCrawlPosition+nextElemToCrawlMaxDispl;
                nextPosition = checkPosition(nextPosition,id);
                if(nextPosition >= roomDistance){
                    nextPosition = roomDistance;
                    this.lastElemToCrawl = id;
                    setNextElemToCrawl(id);
                }
            }
        }
        //System.out.println(id + " " + nextPosition + " " + nextElemToCrawl);
        this.log.updateAssautPartyElemPosition(id, nextPosition);
        //notifyAll();
    }
    
    private synchronized void setNextElemToCrawl(int id){
        int lastToCrawl = this.log.getAssaultPartyElemNumber(id);
        if(lastToCrawl==3){
            lastToCrawl = 1;
        }else{
            lastToCrawl++;
        }
        this.nextElemToCrawl = this.log.getAssaultPartyElemId(1,lastToCrawl);
        notifyAll();
        //System.out.println("here"+nextElemToCrawl);
    }
    
    private int checkPosition(int pos, int id){
        int elem_id;
        for(int i=1;i<=3;i++){
            elem_id=this.log.getAssaultPartyElemId(1, i);
            if(elem_id!=id && elem_id!=this.lastElemToCrawl){
                if(this.log.getAssaultPartyElemPosition(elem_id)==pos){
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
        //System.out.println("Waiting");
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
        // If pos(id)+md>pos(id-1)+3
        if(first){
            this.log.printALine();
            first = false;
        }
        if(nextElemToCrawlPosition-nextElemToCrawlMaxDispl<lastElemToCrawlPosition-3){
            nextPosition = lastElemToCrawlPosition-3;
            // If distance > roomPos
            if(nextPosition < 0){
                nextPosition = 0;
            }
            this.lastElemToCrawl = id;
            setNextElemToCrawl(id);
            //notifyAll();
        }else{
            if(nextElemToCrawlPosition-nextElemToCrawlMaxDispl==lastElemToCrawlPosition){
                if(lastElemToCrawlPosition == 0){
                    nextPosition = 0;
                    this.lastElemToCrawl = id;
                    setNextElemToCrawl(id);
                }else{
                    nextPosition = lastElemToCrawlPosition+1;
                }
            }else{
                nextPosition = nextElemToCrawlPosition-nextElemToCrawlMaxDispl;
                nextPosition = checkPositionBack(nextPosition,id);
                if(nextPosition <= 0){
                    nextPosition = 0;
                    this.lastElemToCrawl = id;
                    setNextElemToCrawl(id);
                }
            }
        }
        //System.out.println(id + " " + nextPosition + " " + nextElemToCrawl);
        this.log.updateAssautPartyElemPosition(id, nextPosition);
        //notifyAll();
    }
    
    private int checkPositionBack(int pos, int id){
        int elem_id;
        for(int i=1;i<=3;i++){
            elem_id=this.log.getAssaultPartyElemId(1, i);
            if(elem_id!=id && elem_id!=this.lastElemToCrawl){
                if(this.log.getAssaultPartyElemPosition(elem_id)==pos){
                    pos += 1;
                    break;
                }
            }
        }
        return pos;
    }

}
