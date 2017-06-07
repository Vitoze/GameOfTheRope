package interfaces;

import java.rmi.RemoteException;
import structures.vectorClock.VectorTimestamp;

/**
 * Thieves interface of Control Collection Site instance.
 * @author João Brito, 68137
 */
public interface IThievesControl {
    
    /**
     * Represents the action of handing a canvas and move to the concentration site.
     * @param id thief id.
     * @param party party number.
     * @param rid room number.
     * @param cv has canvas? 0 or 1.
     * @param vt current timestamp
     * @return new timestamp
     * @throws java.rmi.RemoteException rmi exception
     */
    public VectorTimestamp handACanvas(int id, int party, int rid, int cv, VectorTimestamp vt) throws RemoteException;

}
