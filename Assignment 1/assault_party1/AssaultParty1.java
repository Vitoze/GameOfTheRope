/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    private boolean first = true;
    private int lastElemToCrawl = 0;
    private int nextElemToCrawl = 0;
    private int roomId;
    private int nElemParty = 0;
    private int roomDistance = 0;
    private final int[] partyElemId; 
    private final HashMap<Integer,Integer> partyElemPos;
    private final HashMap<Integer,Integer> partyElem;
    
    public AssaultParty1(){
        partyElemPos = new HashMap<>();
        partyElem = new HashMap<>();
        partyElemId = new int[3];
    }
    
    @Override
    public synchronized int waitForSendAssaultParty(int id, int md) {
        if(nElemParty==3){
            nElemParty = 0;
            partyElemId[nElemParty] = id;
            nextElemToCrawl = partyElemId[0];
            nElemParty++;
        }else{
            partyElemId[nElemParty] = id;
            nextElemToCrawl = partyElemId[0];
            nElemParty++;
        }
        if(this.partyElem.containsKey(id)){
            this.partyElem.replace(id, md);
        }else{
            this.partyElem.put(id, md);
        }
        if(this.partyElemPos.containsKey(id)){
            this.partyElemPos.replace(id, 0);
        }else{
            this.partyElemPos.put(id, 0);
        }
        
        partyReady = false;
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
        notifyAll();
    }
    
    @Override
    public synchronized boolean atMuseum(int id) {
        return partyElemPos.get(id) == roomDistance;
    }

    @Override
    public synchronized void crawlIn(int id) {
        if(first){
            if(this.partyElem.get(id)>=3){
                this.partyElemPos.replace(id,3);
            }else{
                this.partyElemPos.replace(id,this.partyElem.get(id));
            }
            this.lastElemToCrawl = id;
            setNextElemToCrawl(id);
            first = false;
        }else{
            if(this.partyElemPos.get(id)+this.partyElem.get(id)>this.partyElemPos.get(this.lastElemToCrawl)+3){
                if(this.partyElemPos.get(id)+this.partyElem.get(id)>roomDistance){
                    this.partyElemPos.replace(id, roomDistance-this.partyElemPos.get(id));
                }else{
                    this.partyElemPos.replace(id,this.partyElemPos.get(this.lastElemToCrawl)+3); 
                }
                setNextElemToCrawl_LastElem();
            }else{
                if(this.partyElemPos.get(id)+this.partyElem.get(id)==this.partyElemPos.get(this.lastElemToCrawl)+3){
                    if(this.partyElemPos.get(id)+this.partyElem.get(id)==roomDistance){
                        this.partyElemPos.replace(id,this.partyElem.get(id));
                        setNextElemToCrawl_LastElem();
                    }else{
                        this.partyElemPos.replace(id,this.partyElem.get(id)-1);
                        this.nextElemToCrawl = id;
                    }
                }else{
                    this.partyElemPos.replace(id,this.partyElem.get(id));
                    this.nextElemToCrawl = id;
                }
            }
        }
        notifyAll();
    }
    
    private void setNextElemToCrawl_LastElem(){
        int min = 50;
        for(int i=0; i<3; i++){
            if(this.partyElemPos.get(this.partyElemId[i]) < min){
                this.nextElemToCrawl = this.partyElemId[i];
            }
        }
        
    }
    
    private void setNextElemToCrawl(int id){
        for(int i=0; i<3; i++){
            if(this.partyElemId[i]==id){
                if(i<2){
                    this.nextElemToCrawl = this.partyElemId[i++];
                }else{
                    this.nextElemToCrawl = this.partyElemId[0];
                }
                break;
            }
        }
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
    public void crawlOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean atConcentration() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void waitForReverseDirection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
