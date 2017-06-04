package serverSide.concentration_site;

import interfaces.ConcentrationSiteInterface;
import interfaces.LogInterface;
import structures.enumerates.MasterState;
import structures.enumerates.ThievesState;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.vectorClock.VectorTimestamp;

/**
 * Concentration Site instance.
 * @author João Brito, 68137
 */
public class ConcentrationSite implements ConcentrationSiteInterface {
    private boolean callAssault = false;
    private boolean thievesReady = false;
    private boolean lastAssault = false;
    private boolean endHeist = false;
    private int orders = -1;
    private int counter1 = 0;
    private int counter2 = 0;
    private final LinkedList<Integer> thieves;
    private VectorTimestamp clocks;
    private final LogInterface log;

    /**
     * Init the concentration site.
     * @param log
     */
    public ConcentrationSite(LogInterface log) {
        thieves = new LinkedList<>();
        this.log = log;
        this.clocks = new VectorTimestamp(7, 0);
    }

    @Override
    public synchronized VectorTimestamp prepareAssaultParty(int last, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
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
        setMasterState(MasterState.ASSEMBLING_A_GROUP, clocks.clone());
        callAssault = true;
        orders = 0;
        notifyAll();
        return clocks.clone();
    }

    @Override
    public synchronized VectorTimestamp waitForPrepareExcursion(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        while(!this.thievesReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return clocks.clone();
    }

    @Override
    public synchronized VectorTimestamp sumUpResults(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        this.orders = 1;
        this.endHeist = true;
        setMasterState(MasterState.PRESENTING_THE_REPORT, clocks.clone());
        printResults();
        notifyAll();
        return clocks.clone();
    }

    @Override
    public synchronized int amINeeded(int id) throws RemoteException {
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

    @Override
    public synchronized int prepareExcursion(int id) throws RemoteException {
        int party;
        thievesReady = false;
        if(lastAssault){
            counter1++;
            party = 1;
            setAssaultPartyMember(party, counter1, id);
            if(counter1==3){
                thievesReady = true;
                notifyAll();
            }
            updateThiefSituation(id, 'P');
            return party;
        }
        
        if(counter1<3){
            counter1++;
            party = 1;
            setAssaultPartyMember(party, counter1, id);
        }else{
            counter2++;
            party = 2;
            setAssaultPartyMember(party, counter2, id);
            if(counter2 == 3){
                thievesReady = true;
                notifyAll();
            }
        }
        updateThiefSituation(id, 'P');
        setThiefState(ThievesState.CRAWLING_INWARDS, id, clocks.clone());
        return party;   
    }

    @Override
    public void newHeist(VectorTimestamp vt) throws RemoteException {
        log.newHeist(vt);
    }

    @Override
    public void setMasterState(MasterState state, VectorTimestamp vt) throws RemoteException {
        log.setMasterState(state,vt);
    }

    @Override
    public void initAssaultPartyElemId() {
        log.initAssaultPartyElemId();
    }

    @Override
    public void setAssaultPartyAction(int rid1, int rid2) {
        log.setAssaultPartyAction(rid1, rid2);
    }

    @Override
    public void updateThiefSituation(int id, char s) {
        log.updateThiefSituation(id, s);
    }

    @Override
    public void updateAssaultPartyElemCv(int id, int cv, VectorTimestamp vt) throws RemoteException {
        log.updateAssaultPartyElemCv(id, cv, vt);
    }

    @Override
    public void setAssaultParty1RoomId(int rid) {
        log.setAssaultParty1RoomId(rid);
    }

    @Override
    public void setAssaultParty2RoomId(int rid) {
        log.setAssaultParty2RoomId(rid);
    }

    @Override
    public void updateAssaultPartyElemId(int party, int id) {
        log.updateAssaultPartyElemId(party, id);
    }

    @Override
    public void initThieves(ThievesState state, int id, char s, int md) throws RemoteException {
        log.initThieves(state, id, s, md);
    }

    @Override
    public void printResults() {
        log.printResults();
    }

    @Override
    public void setAssaultPartyMember(int party, int i, int id) {
        log.setAssaultPartyMember(party, i, id);
    }

    @Override
    public void setThiefState(ThievesState state, int id, VectorTimestamp vt) throws RemoteException {
        log.setThiefState(state, id, vt);
    }

    @Override
    public int getAssaultParty1RoomId() {
        return log.getAssaultParty1RoomId();
    }

    @Override
    public int getAssaultParty2RoomId() {
        return log.getAssaultParty2RoomId();
    }

    @Override
    public int getRoomDistance(int roomId) {
        return log.getRoomDistance(roomId);
    }

    @Override
    public int getAssaultPartyElemId(int party, int i) {
        return log.getAssaultPartyElemId(party, i);
    }

    @Override
    public int getAssaultPartyElemPosition(int id) {
        return log.getAssaultPartyElemPosition(id);
    }

    @Override
    public int getThiefMaxDisplacement(int id) {
        return log.getThiefMaxDisplacement(id);
    }

    @Override
    public void updateAssautPartyElemPosition(int id, int pos, VectorTimestamp vt) throws RemoteException {
        log.updateAssautPartyElemPosition(id, pos, vt);
    }

    @Override
    public void initMuseum(int id, int dt, int np) {
        log.initMuseum(id, dt, np);
    }

    @Override
    public int getMuseumPaintings(int rid) {
        return log.getMuseumPaintings(rid);
    }

    @Override
    public void updateMuseum(int rid, int np) {
        log.updateMuseum(rid, np);
    }
    
    
    
}
