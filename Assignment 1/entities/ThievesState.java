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
public enum ThievesState {
    OUTSIDE{
        @Override
        public String toString(){
            return "OUTS";
        }
    },
    
    CRAWLING_OUTWARDS{
        @Override
        public String toString(){
            return "CROT";
        }
    },
    
    AT_A_ROOM{
        @Override
        public String toString(){
            return "ATAR";
        }
    },
    
    CRAWLING_INWARDS{
        @Override
        public String toString(){
            return "CRIN";
        }
    }
}
