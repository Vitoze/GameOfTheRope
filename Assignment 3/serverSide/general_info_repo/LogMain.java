package serverSide.general_info_repo;

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
 * Log Main.
 * @author João Brito
 */
public class LogMain {
    
    public static void main(String[] args) throws SocketException, IOException{
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        String rmiRegHostName;
        int rmiRegPortNumb;
        
        RegistryConfig rc = new RegistryConfig();
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();
        
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        
        
        Log log = Log.getInstance();
        LogInterface logInt = null;
        
        try{
            logInt = (LogInterface) UnicastRemoteObject.exportObject((Remote)log, rc.repositoryPort());
        }catch(RemoteException ex){
            System.out.println("Excepção na geração do stub para o log: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o log foi gerado!");
        
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
            reg.bind(nameEntryObject, logInt);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do log: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O log já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O log foi registado!");
        
    }
}
