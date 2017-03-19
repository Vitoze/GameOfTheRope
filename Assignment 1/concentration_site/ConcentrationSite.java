/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concentration_site;

import entities.Master;
import entities.Thieves;
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
    private int counter = 0;
    private final LinkedList<Thieves> thieves;
    private final Master master;

    public ConcentrationSite(LinkedList<Thieves> thieves, Master master) {
        this.thieves = thieves;
        this.master = master;
    }
    
    @Override
    public synchronized void prepareAssaultParty() {
        this.callAssault = true;
        this.orders = 0;
        notifyAll();
        this.callAssault = false;
    }

    /**
     * The thieves are sleeping in this method waiting for the master inform
     * the next assault.
     * @return order
     */
    @Override
    public synchronized int amINeeded() {
        this.callAssault = false;
        
        while(!this.callAssault && this.counter>=3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.orders;
    }

    @Override
    public synchronized void prepareExcursion(int id) {
        this.counter++;
        this.thieves.get(id-1).setSituation('P');
        if(counter == 3){
            this.counter = 0;
            this.thievesReady = true;
            notifyAll();
        }
    }

    @Override
    public synchronized void waitForPrepareExcursion() {
        this.thievesReady = false;
        while(!this.thievesReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
