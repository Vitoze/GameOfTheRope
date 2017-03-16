/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;


/**
 *
 * @author João Brito
 */
public class Master extends Thread{
    private final control_collect_site.IMaster control;
    private MasterState state;
    
    public Master(control_collect_site.IMaster control){
        this.control = control;
        state = MasterState.PLANNING_THE_HEIST;
    }
    
    // This function represents the life cycle of Master
    @Override
    public void run(){
        boolean heistOver = false;
        while(!heistOver){
            switch(this.state){
                case PLANNING_THE_HEIST:
                    this.control.startOperations();
                    this.state = MasterState.DECIDING_WHAT_TO_DO;
                    break;
                case DECIDING_WHAT_TO_DO:
                    switch(this.control.appraiseSit()){
                        case 0:
                            break;
                        case 1:
                            this.control.prepareAssaultParty();
                            this.state = MasterState.ASSEMBLING_A_GROUP;
                            break;
                        case 2:
                            this.control.sumUpResults();
                            this.state = MasterState.PRESENTING_THE_REPORT;
                            break;
                    }                    
                    break;
                case ASSEMBLING_A_GROUP:
                    this.control.waitForPrepareExcursion();
                    this.control.sendAssaultParty();
                    this.state = MasterState.WAINTING_FOR_GROUP_ARRIVAL;
                    break;
                case WAINTING_FOR_GROUP_ARRIVAL:
                    this.control.takeARest();
                    this.control.collectCanvas();
                    this.state = MasterState.DECIDING_WHAT_TO_DO;
                    break;
                case PRESENTING_THE_REPORT:
                    heistOver = true;
                    break;
            }
        }
    }
}
