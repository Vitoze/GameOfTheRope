/*
 * Distributed Systems
 */
package assault_party1;

import general_info_repo.Log;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Brito
 */
public class AssaultParty1 implements IMaster, IThieves{
    private boolean partyReady = false;
    private boolean partyBack = false;
    private boolean next = true;
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
    
    public AssaultParty1(){
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
            notifyAll();
        }else{
            partyElemId[nElemParty] = id;
        }
        System.out.println("Party "+" "+id+" "+nElemParty);
        this.log.setAssaultParty1MemberState(id, nElemParty);
        nElemParty++;
        notifyAll();
        if(!this.partyElem.containsKey(id)){
            this.partyElem.put(id, md);
        }
        if(!this.partyElemPos.containsKey(id)){
            this.partyElemPos.put(id, 0);
        }else{
            this.partyElemPos.replace(id, 0);
        }
        while(!this.partyReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return roomId;
    }
    
    @Override
    public synchronized void sendAssaultParty(int RId, int dt) {
        partyReady = true;
        roomId = RId;
        roomDistance = dt;
        while(this.nElemParty<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.nextElemToCrawl = this.partyElemId[0];
        notifyAll();
        System.out.println("Primeiro elemento " + nextElemToCrawl);
        this.lastElemToCrawl = this.partyElemId[2];
        notifyAll();
    }
    
    @Override
    public synchronized boolean atMuseum(int id) {
        return partyElemPos.get(id) == roomDistance;
    }

    @Override
    public synchronized void waitForMember(int id) {
        boolean no = true;
        while(id!=this.nextElemToCrawl){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(id+" "+nextElemToCrawl);
        next = false;
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
            this.lastElemToCrawl = id;
            setNextElemToCrawl(id);
            //notifyAll();
        }else{
            if(this.partyElemPos.get(id)+this.partyElem.get(id)==this.partyElemPos.get(this.lastElemToCrawl)){
                if(this.partyElemPos.get(this.lastElemToCrawl) == roomDistance){
                    nextPosition = roomDistance;
                    setNextElemToCrawl(id);
                }else{
                    nextPosition = this.partyElemPos.get(this.lastElemToCrawl)-1;
                }
            }else{
                nextPosition = this.partyElemPos.get(id)+this.partyElem.get(id);
                nextPosition = checkPosition(nextPosition,id);
                if(nextPosition > roomDistance){
                    nextPosition = roomDistance;
                    setNextElemToCrawl(id);
                }
            }
        }
        System.out.println(id + " " + nextPosition + " " + nextElemToCrawl);
        this.partyElemPos.replace(id,nextPosition);
        this.log.updateAssaultParty1MemberState(id, this.partyElemPos.get(id), 0);
        next = true;
        //notifyAll();
    }
    
    private synchronized void setNextElemToCrawl(int id){
        for(int i=0; i<3; i++){
            if(this.partyElemId[i]==id){
                if(i<2){
                    this.nextElemToCrawl = this.partyElemId[++i];
                }else{
                    this.nextElemToCrawl = this.partyElemId[0];
                }
                break;
            }
        }
        notifyAll();
        System.out.println("here"+nextElemToCrawl);
    }
    
    private int checkPosition(int pos, int id){
        int[] tmp = this.partyElemId;
        int checkPos = 0;
        for(int i=0; i<3; i++){
            if(tmp[i]!=id){
                if(tmp[i]!=this.lastElemToCrawl){
                    checkPos = tmp[i];
                    break;
                }
            }
        }
        if(pos==this.partyElemPos.get(checkPos)){
            pos=this.partyElemPos.get(checkPos)-1;
        }
        return pos;
    }

    @Override
    public synchronized void waitForReverseDirection() {
        throw new UnsupportedOperationException("Not supported yet.");
        /*
        partyBack = false;
        while(!this.partyBack){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
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
