package clientSide.Master;

import interfaces.AssaultPartyInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.ControlCollectionSiteInterface;
import interfaces.LogInterface;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import structures.constants.RegistryConfig;

/**
 * Master's main class.
 * @author João Brito
 */
public class MasterMain {
    
    public static void main(String[] args) throws IOException{
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println("******************************************************************\nEntity master has started!");
        System.out.println("******************************************************************");
        
        String rmiRegHostName;
        int rmiRegPortNumb;
        
        RegistryConfig rc = new RegistryConfig();
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();
        
        LogInterface logInt = null;
        ControlCollectionSiteInterface controlInt = null;
        ConcentrationSiteInterface concentrationInt = null;
        AssaultPartyInterface party1Int = null;
        AssaultPartyInterface party2Int = null;
        
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            logInt = (LogInterface) registry.lookup(RegistryConfig.repNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating repository: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        } catch (NotBoundException ex) {
            System.out.println("Repository is not registered: " + ex.getMessage () + "!");
            ex.printStackTrace ();
            System.exit(1);
        }
        
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            controlInt = (ControlCollectionSiteInterface) registry.lookup(RegistryConfig.controlNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating control: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        } catch (NotBoundException ex) {
            System.out.println("Control is not registered: " + ex.getMessage () + "!");
            ex.printStackTrace ();
            System.exit(1);
        }
        
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            concentrationInt = (ConcentrationSiteInterface) registry.lookup(RegistryConfig.concentationNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating concentration: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        } catch (NotBoundException ex) {
            System.out.println("Concentration is not registered: " + ex.getMessage () + "!");
            ex.printStackTrace ();
            System.exit(1);
        }
        
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            party1Int = (AssaultPartyInterface) registry.lookup(RegistryConfig.partyNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating party1: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        } catch (NotBoundException ex) {
            System.out.println("AParty1 is not registered: " + ex.getMessage () + "!");
            ex.printStackTrace ();
            System.exit(1);
        }
        
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            party2Int = (AssaultPartyInterface) registry.lookup(RegistryConfig.party2NameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating party2: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        } catch (NotBoundException ex) {
            System.out.println("AParty2 is not registered: " + ex.getMessage () + "!");
            ex.printStackTrace ();
            System.exit(1);
        }
        
        Master master = new Master(controlInt,concentrationInt,party1Int,party2Int,logInt);
        
        master.start();
        
        try{
            master.join();
            System.err.println("Master Died");
        }catch(InterruptedException ex){}
        
    }
    
}
