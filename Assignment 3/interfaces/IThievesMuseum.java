package interfaces;

import java.rmi.RemoteException;

/**
 * Thieves interface of Museum instance.
 * @author João Brito, 68137
 */
public interface IThievesMuseum {
    
    /**
     * This represents the theft of a canvas.
     * @param id thief identification.
     * @param RId room number.
     * @return '0' if there isn't any canvas left, '1' if there is.
     * @throws java.rmi.RemoteException rmi exception
     */
    public int rollACanvas(int id, int RId) throws RemoteException;
}
