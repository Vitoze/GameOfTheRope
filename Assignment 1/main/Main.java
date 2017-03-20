/*
 * Distributed Systems
 */
package main;

import entities.Master;
import entities.Thieves;
import concentration_site.ConcentrationSite;
import control_collect_site.ControlCollectionSite;
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
    private static Museum museum;
    private static Master master;
    private static Log log;
    private static AssaultParty1 party1;
    private static AssaultParty2 party2;
    private static Thieves [] thieves;
    
    
    public static void main(String[] args) {
        /* Start the entities */
        int nThieves = Heist.N_THIEVES;
        
        museum = new Museum();
        concentration = new ConcentrationSite();
        control = new ControlCollectionSite();
        party1 = new AssaultParty1();
        party2 = new AssaultParty2();
        log = Log.getInstance();
        
        master = new Master((control_collect_site.IMaster) control, (concentration_site.IMaster) concentration, (assault_party1.IMaster) party1, (assault_party2.IMaster) party2);
        
        thieves = new Thieves[nThieves];
        for(int i = 0; i<nThieves; i++){
            Random rand = new Random();
            int md = rand.nextInt(6) + 2;
            thieves[i] = new Thieves(i+1,md,(museum.IThieves) museum, (assault_party1.IThieves) party1, (assault_party2.IThieves) party2, (concentration_site.IThieves) concentration, (control_collect_site.IThieves) control);
        }
        
        /* now start */
        master.start();
        
        for(int i=0; i<nThieves; i++){
            thieves[i].start();
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
                thieves[i].join();
                System.err.println("Thief Died: " + i);
            }catch(InterruptedException ex){
                //Escrever para o log
            }
        }
        
        log.writeEnd();
    }
    
}
