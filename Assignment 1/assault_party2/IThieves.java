/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault_party2;

/**
 *
 * @author João Brito
 */
public interface IThieves {
    public void crawlIn();
    
    public void crawlOut();
    
    public void waitForSendAssaultParty();
    
    public void waitForMember();
    
    public boolean atMuseum();
    
    public boolean atConcentration();
    
    public void waitForReverseDirection();
}
