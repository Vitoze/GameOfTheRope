/*
 * Distributed Systems
 */
package entities;

import general_info_repo.Log;

/**
 * Master instance.
 * @author João Brito
 */
public class Master extends Thread{
    private final control_collect_site.IMaster control;
    private final concentration_site.IMaster concentration;
    private final assault_party1.IMaster party1;
    private final assault_party2.IMaster party2;
    private final Log log;
    private MasterState state;
    
    /*
        It will be passed to the Master the methods of the control_collection_site
        that the master have access.
        @param control Instance that implements control_collection_site methods
    */
    public Master(control_collect_site.IMaster control, concentration_site.IMaster concentration, assault_party1.IMaster party1, assault_party2.IMaster party2){
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
        int[] rooms = {0,0,0,0};
        while(!heistOver){
            switch(this.state){
                case PLANNING_THE_HEIST:
                    this.control.startOperations();
                    this.log.newHeist();
                    this.state = MasterState.DECIDING_WHAT_TO_DO;
                    break;
                case DECIDING_WHAT_TO_DO:
                    rooms = this.control.appraiseSit();
                    switch(rooms[0]){
                        case 1:
                            this.concentration.prepareAssaultParty(rooms[3]);
                            this.log.setAssaultPartyAction(rooms[1],rooms[2]);
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
                    if(rooms[3]==1){
                        this.party1.sendAssaultParty(rooms[1], this.log.getRoomDistance(rooms[1]));
                        this.party2.sendAssaultParty(rooms[2], this.log.getRoomDistance(rooms[2]));
                    }else{
                        this.party1.sendAssaultParty(rooms[1], this.log.getRoomDistance(rooms[1]));
                    }
                    this.state = MasterState.WAINTING_FOR_GROUP_ARRIVAL;
                    break;
                case WAINTING_FOR_GROUP_ARRIVAL:
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
