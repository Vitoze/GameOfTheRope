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
        public String tostring(){
            return "PTH";
        }
    },
    
    DECIDING_WHAT_TO_DO{
        @Override
        public String toString(){
            return "DWT";
        }
    },
    
    ASSEMBLING_A_GROUP{
        @Override
        public String toString(){
            return "AAG";
        }
    },
    
    WAINTING_FOR_GROUP_ARRIVAL{
        @Override
        public String toString(){
            return "WGA";
        }
    },
    
    PRESENTING_THE_REPORT{
        @Override
        public String toString(){
            return "PTR";
        }
    }
}
