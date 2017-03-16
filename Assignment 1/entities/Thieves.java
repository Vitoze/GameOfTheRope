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
    private final assault_party.IThieves party;
    private final Log log;
    
    private int id;
    private ThievesState state;
    private char s;
    private int md;
    
    public Thieves(int id, int md, museum.IThieves museum, assault_party.IThieves party, concentration_site.IThieves concentration, control_collect_site.IThieves collect){
        this.id = id;
        this.museum = museum;
        this.concentration = concentration;
        this.collect = collect;
        this.party = party;
        this.log = Log.getInstance();
        state= ThievesState.OUTSIDE;
        s = 'w';
        this.md = md;
    }
    
    // This function represents the life cycle of Thieves
    @Override
    public void run(){
        boolean heistOver = false;
        boolean hasCanvas = false;
        while(!heistOver){
            switch(this.state){
                case OUTSIDE:
                    if(hasCanvas){
                        this.collect.handACanvas();
                        hasCanvas = false;
                    }
                    while(!this.concentration.amINeeded()){
                        this.concentration.waitForOrders();
                    }
                    switch(this.concentration.Orders()){
                        case -1:
                            break;
                        case 0:
                            this.concentration.prepareExcursion();
                            this.state = ThievesState.CRAWLING_INWARDS;
                            break;
                        case 1:
                            heistOver = true;
                    }
                    break;
                case CRAWLING_INWARDS:
                    this.party.waitForSendAssaultParty();
                    while(!party.atMuseum()){
                        this.party.crawlIn();
                        this.party.waitForMember();
                    }
                    this.state = ThievesState.AT_A_ROOM;
                    break;
                case AT_A_ROOM:
                    this.museum.rollACanvas();
                    this.state = ThievesState.CRAWLING_OUTWARDS;
                    break;
                case CRAWLING_OUTWARDS:
                    this.party.waitForReverseDirection();
                    while(!party.atConcentration()){
                        this.party.crawlOut();
                        this.party.waitForMember();
                    }
                    hasCanvas = true;
                    this.state = ThievesState.OUTSIDE;
                    break;
            }
        }
    }
}
