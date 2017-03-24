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
    private boolean endHeist = false;
    private int orders = -1;
    private int counter1 = 0;
    private int counter2 = 0;
    private final LinkedList<Integer> thieves;
    private final Log log;

    public ConcentrationSite() {
        log = Log.getInstance();
        thieves = new LinkedList<>();
    }
    
    @Override
    public synchronized void prepareAssaultParty(int last) {
        thievesReady = false;
        if(last==0){
            lastAssault = true;
        }
        System.out.println("Master1 "+counter1+" "+counter2);
        while(counter1!=0 || counter2!=0){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Master2");
        callAssault = true;
        orders = 0;
        notifyAll();
        
    }
    
    /**
     * The thieves are sleeping in this method waiting for the master inform
     * the next assault.
     * @param id
     * @return order
     */
    @Override
    public synchronized int amINeeded(int id) {
        this.callAssault = false;
        //System.out.println(id);
        thieves.add(id);
        if(counter1>0){
            counter1--;
            notifyAll();
        }
        if(counter2>0){
            counter2--;
            notifyAll();
        }
        while((!callAssault || thieves.getFirst()!=id) && (!endHeist)){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return orders;
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
        System.out.println("ThievesReady!");
    }
    
    @Override
    public synchronized int prepareExcursion(int id) {
        thieves.pop();
        if(lastAssault && thieves.size()==3){
            callAssault = false;
        }
        notifyAll();
        int party;
        thievesReady = false;
        if(lastAssault){
            counter1++;
            party = 1;
            this.log.setAssaultPartyMember(party, counter1, id);
            if(counter1==3){
                thievesReady = true;
                notify();
            }
            this.log.updateThiefSituation(id, 'P');
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
            System.out.println("counter: "+counter2);
            if(counter2 == 3){
                thievesReady = true;
                notifyAll();
            }
        }
        this.log.updateThiefSituation(id, 'P');
        return party;    
    }
    
    @Override
    public synchronized void sumUpResults() {
        this.orders = 1;
        this.endHeist = true;
        notifyAll();
    }
    
}
