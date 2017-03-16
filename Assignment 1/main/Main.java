/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import entities.Master;
import entities.Thieves;
import concentration_site.ConcentrationSite;
import control_collect_site.ControlCollectionSite;
import museum.Museum;
import assault_party.AssaultParty;
import general_info_repo.Log;
import java.util.*;

/**
 *
 * @author João Brito
 */
public class Main {
    private static final int N_THIEVES = 6;
    private static ControlCollectionSite control;
    private static ConcentrationSite concentration;
    private static Museum museum;
    private static Master master;
    private static Log log;
    private static AssaultParty[] assaultParty;
    private static LinkedList<Thieves> thieves;
    
    
    public static void main(String[] args) {
        /* Start the entities */
        thieves = new LinkedList<>();
        for(int i=0; i<N_THIEVES; i++){
            thieves.add(new Thieves());
        }
        assaultParty = new AssaultParty[2];
        museum = new Museum();
        concentration = new ConcentrationSite();
        control = new ControlCollectionSite();
        
        /* now start */
        master.start();
        
        for(int i=0; i<N_THIEVES; i++){
            thieves.get(i).start();
        }
        
        try{
            master.join();
        }catch(InterruptedException ex){
            //Escrever para o log
        }
        
        for(int i=0; i<N_THIEVES; i++){
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
