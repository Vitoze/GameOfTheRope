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
public enum MasterState {
    PLANNING_THE_HEIST{
        @Override
        public String toString(){
            return "PLTH";
        }
    },
    
    DECIDING_WHAT_TO_DO{
        @Override
        public String toString(){
            return "DWTD";
        }
    },
    
    ASSEMBLING_A_GROUP{
        @Override
        public String toString(){
            return "ASAG";
        }
    },
    
    WAINTING_FOR_GROUP_ARRIVAL{
        @Override
        public String toString(){
            return "WFGA";
        }
    },
    
    PRESENTING_THE_REPORT{
        @Override
        public String toString(){
            return "PRTR";
        }
    }
}
