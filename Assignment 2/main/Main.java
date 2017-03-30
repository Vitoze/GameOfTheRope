/*
 * Distributed Systems
 */
package main;

import entities.Master;
import entities.Thieves;
import concentration_site.ConcentrationSite;
import control_collect_site.ControlCollectionSite;
import museum.Museum;
import assault_party.AssaultParty;
import general_info_repo.Log;
import general_info_repo.Heist;
import java.util.*;

/**
 * Heist to the Museum main!
 * 
 * @author João Brito, 68137
 */
public class Main {
    private static ControlCollectionSite control;
    private static ConcentrationSite concentration;
    private static Museum museum;
    private static Master master;
    private static Log log;
    private static AssaultParty party1;
    private static AssaultParty party2;
    private static Thieves [] thieves;
    
    
    public static void main(String[] args) {
        /* Start the entities */
        int nThieves = Heist.N_THIEVES;
        
        museum = new Museum();
        concentration = new ConcentrationSite();
        control = new ControlCollectionSite();
        party1 = new AssaultParty();
        party2 = new AssaultParty();
        log = Log.getInstance();
        
        master = new Master((control_collect_site.IMaster) control, (concentration_site.IMaster) concentration, (assault_party.IMaster) party1, (assault_party.IMaster) party2);
        
        thieves = new Thieves[nThieves];
        for(int i = 0; i<nThieves; i++){
            Random rand = new Random();
            int md = rand.nextInt(Heist.THIEF_MAX_MD+1-Heist.THIEF_MIN_MD) + Heist.THIEF_MIN_MD;
            thieves[i] = new Thieves(i+1,md,(museum.IThieves) museum, (assault_party.IThieves) party1, (assault_party.IThieves) party2, (concentration_site.IThieves) concentration, (control_collect_site.IThieves) control);
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
        
        for(int i=0; i<nThieves; i++){
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
