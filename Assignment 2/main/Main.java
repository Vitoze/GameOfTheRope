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
    
    public static void main(String[] args) {
        /* Start the entities */
        int nThieves = Heist.init.getN_thieves();
        
        Museum museum = new Museum();
        ConcentrationSite concentration = new ConcentrationSite();
        ControlCollectionSite control = new ControlCollectionSite();
        AssaultParty party1 = new AssaultParty();
        AssaultParty party2 = new AssaultParty();
        Log log = Log.getInstance();
        
        
        Master master = new Master((control_collect_site.IMaster) control, (concentration_site.IMaster) concentration, (assault_party.IMaster) party1, (assault_party.IMaster) party2);
        
        Thieves [] thieves = new Thieves[nThieves];
        for(int i = 0; i<nThieves; i++){
            Random rand = new Random();
            int md = rand.nextInt(Heist.init.getThief_max_md()+1-Heist.init.getThief_min_md()) + Heist.init.getThief_min_md();
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
