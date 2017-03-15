/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import general_info_repo.Log;
/**
 *
 * @author João Brito
 */
public class Thieves extends Thread {
    private final museum.IThieves museum;
    private final concentration_site.IThieves concentration;
    private final control_collect_site.IThieves collect;
    private final Log log;
    
    private int id;
    private ThievesState state;
    private char s;
    private int md;
    
    public Thieves(int id, int md, museum.IThieves museum, concentration_site.IThieves concentration, control_collect_site.IThieves collect){
        this.id = id;
        this.museum = museum;
        this.concentration = concentration;
        this.collect = collect;
        this.log = Log.getInstance();
        state= ThievesState.OUTSIDE;
        s = 'w';
        this.md = md;
    }
    
    // This function represents the life cycle of Thieves
    @Override
    public void run(){
        boolean heistOver = false;
        boolean holdCanvas = false;
        while(!heistOver){
            switch(this.state){
                case OUTSIDE:
                    if(holdCanvas){
                        collect.handACanvas();
                        holdCanvas = false;
                    }else{
                        while(!concentration.amINeeded()){
                            concentration.waitForOrders();
                        }
                        state = ThievesState.CRAWLING_INWARDS;
                    }
                    break;
                case CRAWLING_INWARDS:
                    break;
                case AT_A_ROOM:
                    break;
                case CRAWLING_OUTWARDS:
                    break;
            }
        }
    }
}
