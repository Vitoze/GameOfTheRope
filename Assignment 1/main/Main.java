/*
 * Distributed Systems
 */
package main;

import entities.Master;
import entities.Thieves;
import concentration_site.ConcentrationSite;
import control_collect_site.ControlCollectionSite;
import museum.Room;
import museum.Museum;
import assault_party1.AssaultParty1;
import assault_party2.AssaultParty2;
import general_info_repo.Log;
import general_info_repo.Heist;
import java.util.*;

/**
 * Heist to the Museum main!
 * 
 * @author João Brito
 */
public class Main {
    private static ControlCollectionSite control;
    private static ConcentrationSite concentration;
    private static Room[] museum_rooms;
    private static Museum museum;
    private static Master master;
    private static Log log;
    private static AssaultParty1 party1;
    private static AssaultParty2 party2;
    private static LinkedList<Thieves> thieves;
    
    
    public static void main(String[] args) {
        /* Start the entities */
        master = new Master((control_collect_site.IMaster) control, (concentration_site.IMaster) concentration, (assault_party1.IMaster) party1, (assault_party2.IMaster) party2);
        thieves = new LinkedList<>();
        for(int i=1; i<=Heist.N_THIEVES; i++){
            Random rand = new Random();
            int md = rand.nextInt(6) + 2;
            thieves.add(new Thieves(i,md,(museum.IThieves) museum, (assault_party1.IThieves) party1, (assault_party2.IThieves) party2, (concentration_site.IThieves) concentration, (control_collect_site.IThieves) control));
        }
        museum = new Museum();
        museum_rooms = new Room[Heist.N_ROOMS];
        for(int i=0; i<Heist.N_ROOMS; i++){
            Random rand = new Random();
            int dt = rand.nextInt(30) + 15;
            int np = rand.nextInt(16) + 8;
            museum_rooms[i] = new Room(i+1,dt,np);
        }
        concentration = new ConcentrationSite(thieves, master);
        control = new ControlCollectionSite();
        log = Log.getInstance();
        log.initMuseum(museum_rooms);
        
        /* now start */
        master.start();
        
        for(int i=0; i<Heist.N_THIEVES; i++){
            thieves.get(i).start();
        }
        
        /* now join */
        
        try{
            master.join();
            System.err.println("Master Died");
        }catch(InterruptedException ex){
            //Escrever para o log
        }
        
        for(int i=0; i<Heist.N_THIEVES; i++){
            try{
                thieves.get(i).join();
                System.err.println("Thief Died: " + i);
            }catch(InterruptedException ex){
                //Escrever para o log
            }
        }
        
        log.writeEnd();
    }
    
}
