package serverSide.museum;

import interfaces.LogInterface;
import interfaces.MuseumInterface;
import java.rmi.RemoteException;
import structures.constants.SimulParam;
import java.util.Random;
import structures.enumerates.MasterState;
import structures.enumerates.ThievesState;
import structures.vectorClock.VectorTimestamp;
/**
 * Museum instance.
 * @author João Brito, 68137
 */
public class Museum implements MuseumInterface{
    
    private VectorTimestamp clocks;
    private final LogInterface log;
    
    /**
     * Init the Museum instance.
     * @param log repo site
     */
    public Museum(LogInterface log){
        this.log = log;
        this.clocks = new VectorTimestamp(7, 0);
        
        for(int i=1; i<=SimulParam.N_ROOMS; i++){
            Random rand = new Random();
            //random.nextInt(max + 1 - min) + min
            int dt = rand.nextInt(SimulParam.N_MAX_DISTANCE+1-SimulParam.N_MIN_DISTANCE) + SimulParam.N_MIN_DISTANCE;
            int np = rand.nextInt(SimulParam.N_MAX_PAINTINGS+1-SimulParam.N_MIN_PAINTINGS) + SimulParam.N_MIN_PAINTINGS;
            initMuseum(i,dt,np);
            //System.out.println(i+" "+np);
        }
    }
    
    /**
     * Theft of a canvas from the museum. Thieves method.
     * @param id thief id.
     * @param rid room number.
     * @return '0' if there isn't more canvas.
     */
    @Override
    public synchronized int rollACanvas(int id, int rid) throws RemoteException {
        if(getMuseumPaintings(rid)>0){
            updateMuseum(rid, getMuseumPaintings(rid)-1);
            updateAssaultPartyElemCv(id, 1, clocks.clone());
            //System.out.println("Here");
            return 1;
        }else{
            return 0;
        }
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

