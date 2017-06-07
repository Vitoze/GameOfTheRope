package interfaces;

import java.rmi.RemoteException;
import structures.vectorClock.VectorTimestamp;

/**
 * Master interface of AssaultParty#2 instance.
 * @author João Brito, 68137
 */
public interface IMasterParty {
    
    /**
     * The Master will send the assault party out.
     * @param aid assault party id
     * @param vt current timestamp
     * @return updated timestamp
     * @throws java.rmi.RemoteException excepetion rmi
     */
    public VectorTimestamp sendAssaultParty(int aid, VectorTimestamp vt) throws RemoteException;
}