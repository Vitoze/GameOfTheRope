/*
 * Distributed Systems
 */
package entities;

import general_info_repo.Log;
/**
 * Master instance.
 * @author João Brito, 68137
 */
public class Master extends Thread{
    private final control_collect_site.IMaster control;
    private final concentration_site.IMaster concentration;
    private final assault_party.IMaster party1;
    private final assault_party.IMaster party2;
    private final MasterState state;
    private final Log log;
    
    /**
     * It will be passed to the Master the methods it has access.
     * @param control Instance that implements Control site Master methods.
     * @param concentration Instance that implements Concentration site Master methods.
     * @param party1 Instance that implements Assault party#1 Master methods.
     * @param party2 Instance that implements Assault party#2 Master methods.
     */
    public Master(control_collect_site.IMaster control, concentration_site.IMaster concentration, assault_party.IMaster party1, assault_party.IMaster party2){
        this.control = control;
        this.concentration = concentration;
        this.party1 = party1;
        this.party2 = party2;
        this.log = Log.getInstance();
        
        this.setName("Master");
        state = MasterState.PLANNING_THE_HEIST;
        log.initMasterState(state);
    }
    
    /**
     * This function represents the life cycle of Master
     */
    @Override
    public void run(){
        boolean heistOver = false;
        int[] decision;
        // PLANNING_THE_HEIST
        control.startOperations();
        while(!heistOver){    
            // DECIDING_WHAT_TO_DO
            decision = control.appraiseSit();
            if(decision[0]==1){
                concentration.prepareAssaultParty(decision[1]);
                // ASSEMBLING_A_GROUP
                concentration.waitForPrepareExcursion();
                if(decision[1]==1){
                    party1.sendAssaultParty(1);
                    party2.sendAssaultParty(2);
                }else{
                    party1.sendAssaultParty(1);
                }
                // WAITING_FOR_GROUP_ARRIVAL
                control.takeARest();
                // DECIDING_WHAT_TO_DO
            }else{
                concentration.sumUpResults();
                // PRESENTING_THE_REPORT
                heistOver = true;
            }  
        }
    }
}
