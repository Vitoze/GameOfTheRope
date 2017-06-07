package interfaces;

import java.rmi.RemoteException;

/**
 * Thieves interface of Concentration Site instance.
 * @author João Brito, 68137
 */
public interface IThievesConcentration {
    
    /**
     * The thieves will await for further instructions.
     * @param id thief id.
     * @return instructions.
     * @throws java.rmi.RemoteException rmi exception
     */
    public int amINeeded(int id) throws RemoteException;
    
    /**
     * The thieves will move to an assault party.
     * @param id thief id.
     * @return assault party number.
     * @throws java.rmi.RemoteException rmi exception
     */
    public int prepareExcursion(int id) throws RemoteException;
    
}
