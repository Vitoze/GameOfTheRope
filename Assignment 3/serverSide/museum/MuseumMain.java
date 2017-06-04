package serverSide.museum;

import interfaces.LogInterface;
import interfaces.MuseumInterface;
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
 *
 * @author João Brito
 */
public class MuseumMain {
    
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
        
        
        Museum museum = null;
        MuseumInterface museumInt = null;
        museum = new Museum(logInt);
        try{
            museumInt = (MuseumInterface) UnicastRemoteObject.exportObject((Remote)museum, rc.museumPort());
        }catch(RemoteException ex){
            System.out.println("Excepção na geração do stub para o museum: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o museum foi gerado!");
        
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
            reg.bind(nameEntryObject, museumInt);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do museum: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O museum já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O museum foi registado!");
        
    }
    
}
