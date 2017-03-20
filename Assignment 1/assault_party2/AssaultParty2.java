/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault_party2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Brito
 */
public class AssaultParty2 implements IMaster, IThieves{
    private boolean partyReady = false;
    
    public AssaultParty2(){
        
    }
    
    @Override
    public synchronized void waitForSendAssaultParty(int id, int md) {
        partyReady = false;
        while(!this.partyReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public synchronized void sendAssaultParty(int RId, int dt) {
        partyReady = true;
        notifyAll();
    }

    @Override
    public void crawlIn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void crawlOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void waitForMember() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean atMuseum(int id) {
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
