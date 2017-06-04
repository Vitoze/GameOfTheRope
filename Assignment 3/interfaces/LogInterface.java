package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import structures.enumerates.MasterState;
import structures.enumerates.ThievesState;
import structures.vectorClock.VectorTimestamp;

/**
 * Log Interface.
 * @author João Brito
 */
public interface LogInterface extends Remote, IThievesLog{
    
    public void newHeist(VectorTimestamp vt) throws RemoteException;
    
    public void setMasterState(MasterState state, VectorTimestamp vt) throws RemoteException;
    
    public void initAssaultPartyElemId();
    
    public void setAssaultPartyAction(int rid1, int rid2);
    
    public void updateThiefSituation(int id, char s);
    
    public void updateAssaultPartyElemCv(int id, int cv, VectorTimestamp vt) throws RemoteException;
    
    public void setAssaultParty1RoomId(int rid);
    
    public void setAssaultParty2RoomId(int rid);
    
    public void updateAssaultPartyElemId(int party, int id);
    
    public void printResults();
    
    public void setAssaultPartyMember(int party, int i, int id);
    
    public void setThiefState(ThievesState state, int id, VectorTimestamp vt) throws RemoteException;
    
    public int getAssaultParty1RoomId();
    
    public int getAssaultParty2RoomId();
    
    public int getRoomDistance(int roomId);
    
    public int getAssaultPartyElemId(int party, int i);
    
    public int getAssaultPartyElemPosition(int id);
    
    public int getThiefMaxDisplacement(int id);
    
    public void updateAssautPartyElemPosition(int id, int pos, VectorTimestamp vt) throws RemoteException;
    
    public void initMuseum(int id, int dt, int np);
    
    public int getMuseumPaintings(int rid);
    
    public void updateMuseum(int rid, int np);
    
}
