/*
 * Distributed Systems
 */
package assault_party2;

import general_info_repo.Log;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Brito
 */
public class AssaultParty2 implements IMaster, IThieves{
    private boolean partyReady = false;
    private boolean partyBack = false;
    private boolean first = true;
    private int lastElemToCrawl = 0;
    private int nextElemToCrawl = 0;
    private int roomId;
    private int nElemParty = 0;
    private int roomDistance = 0;
    private final Log log;
    private final int[] partyElemId; 
    private final HashMap<Integer,Integer> partyElemPos;
    private final HashMap<Integer,Integer> partyElem;
    
    public AssaultParty2(){
        this.log = Log.getInstance();
        partyElemPos = new HashMap<>();
        partyElem = new HashMap<>();
        partyElemId = new int[3];
    }
    
    @Override
    public synchronized int waitForSendAssaultParty(int id, int md) {
        if(nElemParty==3){
            nElemParty = 0;
            partyElemId[nElemParty] = id;
            partyReady=false;
            first = true;
            nElemParty++;
        }else{
            partyElemId[nElemParty] = id;
            nElemParty++;
        }
        if(!this.partyElem.containsKey(id)){
            this.partyElem.put(id, md);
        }
        if(!this.partyElemPos.containsKey(id)){
            this.partyElemPos.put(id, 0);
        }
        
        while(!this.partyReady && nElemParty<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return roomId;
    }
    
    @Override
    public synchronized void sendAssaultParty(int RId, int dt) {
        partyReady = true;
        roomId = RId;
        roomDistance = dt;
        this.nextElemToCrawl = this.partyElemId[0];
        this.lastElemToCrawl = this.partyElemId[2];
        notifyAll();
    }
    
    @Override
    public synchronized boolean atMuseum(int id) {
        return partyElemPos.get(id) == roomDistance;
    }
    
    @Override
    public synchronized void waitForMember(int id) {
        while(nElemParty!=3){
            while(id!=this.nextElemToCrawl){
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(AssaultParty2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public synchronized void crawlIn(int id) {
        // If pos(id)+md>pos(id-1)+3
        int nextPosition;
        if(first){
            this.log.printALine();
            first = false;
        }
        if(this.partyElemPos.get(id)+this.partyElem.get(id)>this.partyElemPos.get(this.lastElemToCrawl)+3){
            nextPosition = this.partyElemPos.get(this.lastElemToCrawl)+3;
            // If distance > roomPos
            if(nextPosition > roomDistance){
                nextPosition = roomDistance;
            }
            setNextElemToCrawl(id);
        }else{
            if(this.partyElemPos.get(id)+this.partyElem.get(id)==this.partyElemPos.get(this.lastElemToCrawl)+3){
                nextPosition = this.partyElemPos.get(this.lastElemToCrawl)+2;
                if(nextPosition > roomDistance){
                    nextPosition = roomDistance;
                    setNextElemToCrawl(id);
                }
            }else{
                nextPosition = this.partyElemPos.get(id)+this.partyElem.get(id);
            }
        }
        this.partyElemPos.replace(id,nextPosition);
        this.log.updateAssaultParty2MemberState(id, nextPosition, 0);
        notifyAll();
    }
    
    private void setNextElemToCrawl(int id){
        for(int i=0; i<3; i++){
            if(this.partyElemId[i]==id){
                if(i<2){
                    int next = i++;
                    this.nextElemToCrawl = this.partyElemId[next];
                }else{
                    this.nextElemToCrawl = this.partyElemId[0];
                }
                break;
            }
        }
    }

    @Override
    public synchronized void waitForReverseDirection() {
        partyBack = false;
        while(!this.partyBack){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void crawlOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean atConcentration() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
