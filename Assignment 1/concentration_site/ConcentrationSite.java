/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concentration_site;

import general_info_repo.Log;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Brito
 */
public class ConcentrationSite implements IMaster, IThieves {
    private boolean callAssault = false;
    private boolean thievesReady = false;
    private boolean lastAssault = false;
    private int orders = -1;
    private int counter1 = 0;
    private int counter2 = 0;
    private final LinkedList<Integer> thieves;
    private final Log log;

    public ConcentrationSite() {
        log = Log.getInstance();
        thieves = new LinkedList<>();
    }
    
    /**
     * The thieves are sleeping in this method waiting for the master inform
     * the next assault.
     * @param id
     * @param party
     * @return order
     */
    @Override
    public synchronized int amINeeded(int id, int party) {
        this.callAssault = false;
        thieves.add(id);
        //System.out.println(id);
        if(party==1){
            counter1--;
            notifyAll();
        }else if(party==2){
            counter2--;
            notifyAll();
        }
        while(!this.callAssault || thieves.getFirst()!=id){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return this.orders;
        
    }
    
    @Override
    public synchronized void prepareAssaultParty(int last) {
        System.out.println("Master2");
        if(last==0){
            this.lastAssault = true;
        }else{
            this.lastAssault = false;
        }
        this.callAssault = false;
        this.thievesReady = false;
        //System.out.println("Master1");
        while(counter1!=0 || counter2!=0){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.callAssault = true;
        this.orders = 0;
        notifyAll();
        
    }

    @Override
    public synchronized int prepareExcursion(int id) {
        thieves.pop();
        notifyAll();
        int party;
        //System.out.println("Gone");
        this.thievesReady = false;
        
        if(this.lastAssault){
            counter1++;
            party = 1;
            this.log.setAssaultPartyMember(party, counter1, id);
            return party;
        }
        
        if(counter1<3){
            counter1++;
            party = 1;
            this.log.setAssaultPartyMember(party, counter1, id);
        }else{
            counter2++;
            party = 2;
            this.log.setAssaultPartyMember(party, counter2, id);
            if(counter2 == 3){
                this.thievesReady = true;
                notify();
            }
        }
        return party;    
    }

    @Override
    public synchronized void waitForPrepareExcursion() {
        while(!this.thievesReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public synchronized void sumUpResults() {
        this.orders = 1;
        this.callAssault = true;
        notifyAll();
    }
    
}
