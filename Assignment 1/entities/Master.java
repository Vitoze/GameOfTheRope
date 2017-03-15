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
    private MasterState state;
    
    public Master(){
        state = MasterState.PLANNING_THE_HEIST;
    }
    
    // This function represents the life cycle of Master
    @Override
    public void run(){
        boolean heistOver = false;
        while(!heistOver){
            switch(this.state){
                case PLANNING_THE_HEIST:
                    this.state = MasterState.DECIDING_WHAT_TO_DO;
                    break;
                case DECIDING_WHAT_TO_DO:
                    break;
                case ASSEMBLING_A_GROUP:
                    break;
                case WAINTING_FOR_GROUP_ARRIVAL:
                    break;
                case PRESENTING_THE_REPORT:
                    heistOver = true;
                    break;
            }
        }
    }
}
