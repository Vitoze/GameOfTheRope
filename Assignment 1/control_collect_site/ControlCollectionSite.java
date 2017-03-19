/*
 *  Distributed Systems
 */
package control_collect_site;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Control and Collection Site instance.
 *  @author João Brito
 */
public class ControlCollectionSite implements IMaster {
    private boolean collectCanvas = true;
    private boolean firstAssault = true;
    private int roomCounter = 0;
    
    /**
     * In Master life cycle, transition between "Planning the heist" and "Deciding what to do",
     * initiates a heist.
     */
    @Override
    public void startOperations() {
        
    }
    
    /**
     * The Master decides what to do next
     * @return 1 to prepare new assault or 2 to end heist 
     */
    @Override
    public int appraiseSit() {
        if(firstAssault){
            roomCounter++;
            firstAssault = false;
            return 1;
        }
        if(collectCanvas){
            return 1;
        }else{
            if(roomCounter==5){
                return 2;
            }else{
                roomCounter++;
                return 1;
            }
        }
    }

    @Override
    public boolean sumUpResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateMaster(String stat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendAssaultParty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void collectCanvas() {
        collectCanvas=!collectCanvas;
    }

    @Override
    public void takeARest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void waitForPrepareExcursion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
       
}
