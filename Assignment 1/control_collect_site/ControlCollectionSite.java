/*
 *  Distributed Systems
 */
package control_collect_site;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Control and Collection Site instance.
 *  @author João Brito
 */
public class ControlCollectionSite implements IMaster, IThieves {
    private boolean collectCanvas = false;
    private LinkedList<Integer> rooms;
    
    /**
     * In Master life cycle, transition between "Planning the heist" and "Deciding what to do",
     * initiates a heist.
     */
    @Override
    public void startOperations() {
        rooms = new LinkedList<>();
        rooms.add(1);
        rooms.add(2);
        rooms.add(3);
        rooms.add(4);
        rooms.add(5);
    }
    
    /**
     * The Master decides what to do next
     * @return 1 to prepare new assault or 2 to end heist 
     */
    @Override
    public int[] appraiseSit() {
        int [] decision = new int[5];
        if(rooms.isEmpty()){
            decision[0] = 2;
            decision[1] = 0;
            decision[2] = 0;
            decision[3] = 0;
        }
        if(rooms.size() < 2){
            decision[0] = 1;
            decision[1] = rooms.getFirst();
            decision[2] = 0;
            decision[3] = 0;
        }else{
            decision[0] = 1;
            decision[1] = rooms.get(0);
            decision[2] = rooms.get(1);
            decision[3] = 1;
        }
        return decision;
    }
    
    @Override
    public synchronized void takeARest() {
        collectCanvas = false;
        while(!collectCanvas){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
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
    public void waitForPrepareExcursion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handACanvas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
       
}
