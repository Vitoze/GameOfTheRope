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
            return "OUT";
        }
    },
    
    CRAWLING_OUTWARDS{
        @Override
        public String toString(){
            return "CRO";
        }
    },
    
    AT_A_ROOM{
        @Override
        public String toString(){
            return "ATR";
        }
    },
    
    CRAWLING_INWARDS{
        @Override
        public String toString(){
            return "CRI";
        }
    }
}
