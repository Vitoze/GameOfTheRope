/*
 * Distributed Systems.
 */
package concentration_site;

import general_info_repo.Log;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Concentration Site instance.
 * @author João Brito, 68137
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

    /**
     * Init the concentration site.
     */
    public ConcentrationSite() {
        log = Log.getInstance();
        thieves = new LinkedList<>();
    }
    
    /**
     * The master will order to the thieves to begin to prepare the assault party.
     * @param last '0' last assault, '1' if not. Master method.
     */
    @Override
    public synchronized void prepareAssaultParty(int last) {
        thievesReady = false;
        notifyAll();
        if(last==0){
            lastAssault = true;
        }
        while(counter1!=0 || counter2!=0){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        callAssault = true;
        orders = 0;
        notifyAll();
        
    }
    
    /**
     * The thieves are sleeping in this method waiting for the master inform
     * the next assault. Thieves method.
     * @param id thief id.
     * @return order.
     */
    @Override
    public synchronized int amINeeded(int id) {
        this.callAssault = false;
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
        thieves.pop();
        if(lastAssault && thieves.size()==3){
            callAssault = false;
            notifyAll();
        }
        notifyAll();
        return orders;
    }
    
    /**
     * The master will wait until the thieves are ready to go. Master method.
     */
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
    
    /**
     * The thieves will begin to prepare the excursion to the assault party. Thieves method.
     * @param id thief id.
     * @return party number.
     */
    @Override
    public synchronized int prepareExcursion(int id) {
        int party;
        thievesReady = false;
        if(lastAssault){
            counter1++;
            party = 1;
            this.log.setAssaultPartyMember(party, counter1, id);
            if(counter1==3){
                thievesReady = true;
                notifyAll();
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
            if(counter2 == 3){
                thievesReady = true;
                notifyAll();
            }
        }
        this.log.updateThiefSituation(id, 'P');
        return party;    
    }
    
    /**
     * The master will present the result of the heist. Master method.
     */
    @Override
    public synchronized void sumUpResults() {
        this.orders = 1;
        this.endHeist = true;
        notifyAll();
    }
    
}
