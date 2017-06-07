package interfaces;

import java.rmi.RemoteException;
import structures.vectorClock.VectorTimestamp;

/**
 * Tieves interface of AssaultParty#2 instance.
 * @author João Brito, 68137
 */
public interface IThievesParty {
    
    /**
     * The thieves wil crawl to the museum.
     * @param id thief if
     * @param vt current timestamp
     * @return new timestamp
     * @throws java.rmi.RemoteException exception rmi 
     */
    public VectorTimestamp crawlIn(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The thieves will crawl to the concentration site.
     * @param id thief id.
     * @param vt current timstamp
     * @return new timestamp
     * @throws java.rmi.RemoteException exception rmi
     */
    public VectorTimestamp crawlOut(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The thieves will wait for the master to send the party.
     * @param id thief id.
     * @return museum room to assault.
     * @throws java.rmi.RemoteException rmi exception
     */
    public int waitForSendAssaultParty(int id) throws RemoteException;
    
    /**
     * The thieves will wait for other thief to make is crawl movement.
     * @param id thief id.
     * @param vt current timestamp
     * @return new timestamp
     * @throws java.rmi.RemoteException exception rmi
     */
    public VectorTimestamp waitForMember(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * This method will check if a thief is already at the museum.
     * @param id thief id.
     * @return true or false.
     * @throws java.rmi.RemoteException rmi exception
     */
    public boolean atMuseum(int id) throws RemoteException;
    
    /**
     * This method will check if a thief is already at the concentration site.
     * @param id thief id.
     * @return true or false.
     * @throws java.rmi.RemoteException exception rmi
     */
    public boolean atConcentration(int id) throws RemoteException;
    
    /**
     * The thieves will wait for all to be ready to craw out.
     * @param id thief id
     * @param vt current timestamp
     * @return new timestamp
     * @throws java.rmi.RemoteException rmi exception
     */
    public VectorTimestamp waitForReverseDirection(int id, VectorTimestamp vt) throws RemoteException;
}
