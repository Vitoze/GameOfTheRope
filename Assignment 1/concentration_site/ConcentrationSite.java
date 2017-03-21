/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concentration_site;

import general_info_repo.Heist;
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
    private int orders = -1;
    private int counter1 = 0;
    private int counter2 = 0;
    private final LinkedList<Integer> thieves;

    public ConcentrationSite() {
        thieves = new LinkedList<>();
    }
    
    @Override
    public synchronized void prepareAssaultParty() {
        this.callAssault = false;
        this.thievesReady = false;
        while(thieves.size()<3 && counter1!=0 && counter2!=0){
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

    /**
     * The thieves are sleeping in this method waiting for the master inform
     * the next assault.
     * @param id
     * @param party
     * @return order
     */
    @Override
    public synchronized int amINeeded(int id, int party) {
        thieves.add(id);
        if(party==1){
            counter1--;
            notifyAll();
        }else if(party==2){
            counter2--;
            notifyAll();
        }
        while(!this.callAssault && thieves.getFirst()!=id){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.orders;
    }

    @Override
    public synchronized int prepareExcursion() {
        thieves.pop();
        int party;
        this.thievesReady = false;
        if(counter1<3){
            counter1++;
            party = 1;
        }else{
            counter2++;
            party = 2;
            if(counter2 == 3){
                this.thievesReady = true;
                notifyAll();
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
    
}
