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
    private final Log log;
    private MasterState state;
    
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
        
        this.log.initMasterState(state);
    }
    
    /**
     * This function represents the life cycle of Master
     */
    @Override
    public void run(){
        boolean heistOver = false;
        int[] decision = {0,0};
        while(!heistOver){
            switch(this.state){
                case PLANNING_THE_HEIST:
                    this.control.startOperations();
                    this.log.newHeist();
                    this.state = MasterState.DECIDING_WHAT_TO_DO;
                    break;
                case DECIDING_WHAT_TO_DO:
                    decision = this.control.appraiseSit();
                    switch(decision[0]){
                        case 1:
                            this.concentration.prepareAssaultParty(decision[1]);
                            this.state = MasterState.ASSEMBLING_A_GROUP;
                            break;
                        case 2:
                            this.concentration.sumUpResults();
                            this.state = MasterState.PRESENTING_THE_REPORT;
                            break;
                    }
                    break;
                case ASSEMBLING_A_GROUP:
                    this.concentration.waitForPrepareExcursion();
                    if(decision[1]==1){
                        this.party1.sendAssaultParty(1);
                        this.party2.sendAssaultParty(2);
                    }else{
                        this.party1.sendAssaultParty(1);
                    }
                    this.state = MasterState.WAITING_FOR_GROUP_ARRIVAL;
                    break;
                case WAITING_FOR_GROUP_ARRIVAL:
                    this.control.takeARest();
                    //this.control.collectCanvas(rooms[3]);
                    this.state = MasterState.DECIDING_WHAT_TO_DO;
                    break;
                case PRESENTING_THE_REPORT:
                    heistOver = true;
                    break;
            }
            this.log.setMasterState(state);
        }
    }
}
