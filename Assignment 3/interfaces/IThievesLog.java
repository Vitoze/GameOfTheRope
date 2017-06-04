/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.RemoteException;
import structures.enumerates.ThievesState;

/**
 *
 * @author João Brito
 */
public interface IThievesLog {
    
    public void initThieves(ThievesState state, int id, char s, int md) throws RemoteException;
            
}
