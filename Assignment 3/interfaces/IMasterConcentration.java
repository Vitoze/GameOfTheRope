package interfaces;

import structures.vectorClock.VectorTimestamp;
import java.rmi.RemoteException;

/**
 * Master interface of Concentration Site instance.
 * @author João Brito, 68137
 */
public interface IMasterConcentration {

    /**
     * The master will tell the thieves to prepare for a new assault to the museum.
     * @param last if it is the last assault, it will be signalized
     * @param vt current timestamp
     * @return new timestamp
     * @throws java.rmi.RemoteException rmi exception
     */
    public VectorTimestamp prepareAssaultParty(int last, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The master will wait until there is enough thieves to begin a new assault.
     * @param vt current timestamp
     * @return new timestamp
     * @throws java.rmi.RemoteException rmi exception
     */
    public VectorTimestamp waitForPrepareExcursion(VectorTimestamp vt) throws RemoteException;
    
    /**
     * The master will end the heist and present the results.
     * @param vt current timestamp
     * @return new timestamp
     * @throws java.rmi.RemoteException rmi expcetion 
     */
    public VectorTimestamp sumUpResults(VectorTimestamp vt) throws RemoteException;
    
}
