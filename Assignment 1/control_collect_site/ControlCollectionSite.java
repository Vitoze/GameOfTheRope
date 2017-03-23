/*
 *  Distributed Systems
 */
package control_collect_site;

import java.util.LinkedList;
import general_info_repo.Log;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Control and Collection Site instance.
 *  @author João Brito
 */
public class ControlCollectionSite implements IMaster, IThieves {
    private boolean collectCanvas = false;
    private LinkedList<Integer> rooms;
    private int nElemToWait = 0;
    private final Log log;
    
    public ControlCollectionSite(){
        log = Log.getInstance();
    }
    
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
    public synchronized int[] appraiseSit() {
        int [] decision = new int[4];
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
        
        if(decision[3]==0){
            this.nElemToWait = 3;
        }else{
            this.nElemToWait = 6;
        }
        System.out.println("Master1");
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
    public synchronized void handACanvas(int id, int rid, int cv) {
        if(cv==0){
            if(rooms.contains(rid)){
                rooms.remove(rid);
            }
        }
        this.log.updateAssaultPartyElemCv(id, cv);
        this.nElemToWait--;
        if(this.nElemToWait==0){ 
            collectCanvas = true;
            notify();
        }
    }
       
}
