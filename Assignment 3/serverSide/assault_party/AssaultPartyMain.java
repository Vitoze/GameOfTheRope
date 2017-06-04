package serverSide.assault_party;

import interfaces.AssaultPartyInterface;
import interfaces.LogInterface;
import interfaces.Register;
import java.io.IOException;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import structures.constants.RegistryConfig;

/**
 * Assault Party #1 Main
 * @author João Brito
 */
public class AssaultPartyMain {
    
    public static void main(String[] args) throws SocketException, IOException{
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        String rmiRegHostName;
        int rmiRegPortNumb;
        
        RegistryConfig rc = new RegistryConfig();
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();
        
        LogInterface logInt = null;
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            logInt = (LogInterface) registry.lookup (RegistryConfig.repNameEntry);
        }
        catch (RemoteException e)
        { 
            System.out.println("Excepção na localização do log: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Log não está registado: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit(1);
        }
        
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        
        
        AssaultParty party = null;
        AssaultPartyInterface partyInt = null;
        party = new AssaultParty(logInt);
        try{
            partyInt = (AssaultPartyInterface) UnicastRemoteObject.exportObject((Remote)party, rc.partyPort());
        }catch(RemoteException ex){
            System.out.println("Excepção na geração do stub para o party1: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o party1 foi gerado!");
        
        String nameEntryBase = RegistryConfig.registerHandler;
        String nameEntryObject = RegistryConfig.controlNameEntry;
        Registry registry = null;
        Register reg = null;
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O registo RMI foi criado!");
        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, partyInt);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do party: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O party já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O party foi registado!");
        
    }
    
}
