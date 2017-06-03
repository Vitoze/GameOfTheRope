package interfaces;

import java.rmi.RemoteException;
import structures.vectorClock.VectorTimestamp;

/**
 * Master interface of Control Collection Site instance.
 * @author João Brito, 68137
 */
public interface IMasterControl {
    
    /**
     * Initiates the heist operations.
     */
    public VectorTimestamp startOperations(VectorTimestamp vt) throws RemoteException;
    
    /**
     * The master will wait until the arrival of the assault party. 
     */
    public VectorTimestamp takeARest(VectorTimestamp vt) throws RemoteException;
    
    /**
     * The master will decide what to do next.
     * @return decision.
     */
    public int[] appraiseSit() throws RemoteException;
    
}
