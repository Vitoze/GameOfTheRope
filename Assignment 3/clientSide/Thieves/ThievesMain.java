package clientSide.Thieves;

import interfaces.AssaultPartyInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.ControlCollectionSiteInterface;
import interfaces.LogInterface;
import interfaces.MuseumInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import structures.constants.SimulParam;
import java.util.Random;
import structures.constants.RegistryConfig;

/**
 * Thieves's main class.
 * @author João Brito
 */
public class ThievesMain {
 
    public static void main(String[] args){
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println("******************************************************************\nEntity thieves has started!");
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
        MuseumInterface museumInt = null;
        
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
        
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            museumInt = (MuseumInterface) registry.lookup(RegistryConfig.museumNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating Museum: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        } catch (NotBoundException ex) {
            System.out.println("Museum is not registered: " + ex.getMessage () + "!");
            ex.printStackTrace ();
            System.exit(1);
        }
        
        int nThieves = SimulParam.N_THIEVES;
        Thieves[] thieves = new Thieves[nThieves];
        for(int i = 0; i<nThieves; i++){
            Random rand = new Random();
            int md = rand.nextInt(SimulParam.THIEF_MAX_MD+1-SimulParam.THIEF_MIN_MD) + SimulParam.THIEF_MIN_MD;
            thieves[i] = new Thieves(i+1,md,controlInt, concentrationInt, party1Int, party2Int, museumInt, logInt);
        }
        
        for(int i=0; i<nThieves; i++){
            thieves[i].start();
        }
        
        for(int i=0; i<nThieves; i++){
            try{
                thieves[i].join();
                System.err.println("Thief Died: " + i);
            }catch(InterruptedException ex){}
        }
        
    }
}
