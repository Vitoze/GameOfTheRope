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
    private int roomId;
    private int roomDistance = 0;
    private HashMap<Integer,Integer> partyElemPos;
    private HashMap<Integer,Integer> partyElem;
    
    public AssaultParty1(){
        partyElemPos = new HashMap<>();
        partyElem = new HashMap<>();
    }
    
    @Override
    public synchronized void waitForSendAssaultParty(int id, int md) {
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
        return partyElemPos.get(id) >= roomDistance;
    }

    @Override
    public synchronized void crawlIn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void waitForMember() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
