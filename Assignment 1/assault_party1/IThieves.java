/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault_party1;

/**
 *
 * @author João Brito
 */
public interface IThieves {
    public void crawlIn(int id);
    
    public void crawlOut();
    
    public int waitForSendAssaultParty(int id, int md);
    
    public void waitForMember(int id);
    
    public boolean atMuseum(int id);
    
    public boolean atConcentration();
    
    public void waitForReverseDirection();
}
