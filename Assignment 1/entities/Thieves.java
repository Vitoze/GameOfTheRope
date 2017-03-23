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
    private final assault_party1.IThieves party1;
    private final assault_party2.IThieves party2;
    private final Log log;
    
    private final int id;
    private ThievesState state;
    private char s;
    private final int md;
    
    public Thieves(int id, int md, museum.IThieves museum, assault_party1.IThieves party1, assault_party2.IThieves party2, concentration_site.IThieves concentration, control_collect_site.IThieves collect){
        this.id = id;
        this.museum = museum;
        this.concentration = concentration;
        this.collect = collect;
        this.party1 = party1;
        this.party2 = party2;
        this.log = Log.getInstance();
        this.s = 'W';
        this.md = md;
        this.setName("Thief"+this.id);
        state = ThievesState.OUTSIDE;
        
        this.log.initThieves(this.state,this.id, this.s, this.md);
    }
    
    /**
     * This function represents the life cycle of Thieves
     */         
    @Override
    public void run(){
        boolean heistOver = false;
        boolean hasCanvas = false;
        int canvas = 0;
        int party_room = 0;
        int party = 0;
        while(!heistOver){
            switch(this.state){
                case OUTSIDE:
                    if(hasCanvas){
                        this.collect.handACanvas(this.id, party_room, canvas);
                        hasCanvas = false;
                    }
                    switch(this.concentration.amINeeded(this.id, party)){
                        case -1:
                            break;
                        case 0:
                            party = this.concentration.prepareExcursion(this.id);
                            this.log.updateThiefSituation(this.id, 'P');
                            this.state = ThievesState.CRAWLING_INWARDS;
                            break;
                        case 1:
                            heistOver = true;
                            break;
                    }
                    break;
                case CRAWLING_INWARDS:
                    switch(party){
                        case 1:
                            party_room=this.party1.waitForSendAssaultParty(this.id, this.md);
                            while(!party1.atMuseum(this.id)){
                                this.party1.waitForMember(this.id);
                                this.party1.crawlIn(this.id);
                            }
                            break;
                        case 2:
                            party_room=this.party2.waitForSendAssaultParty(this.id, this.md);
                            while(!party2.atMuseum(this.id)){
                                this.party2.waitForMember(this.id);
                                this.party2.crawlIn(this.id);
                            }
                            break;
                    }
                    this.state = ThievesState.AT_A_ROOM;
                    break;
                case AT_A_ROOM:
                    switch(party){
                        case 1:
                            canvas = this.museum.rollACanvas(this.id,party_room);
                            break;
                        case 2:
                            canvas = this.museum.rollACanvas(this.id,party_room);
                            break;
                    }
                    this.log.updateAssaultPartyElemCv(this.id, canvas);
                    this.state = ThievesState.CRAWLING_OUTWARDS;
                    break;
                case CRAWLING_OUTWARDS:
                    switch(party){
                        case 1:
                            this.party1.waitForReverseDirection();
                            while(!party1.atConcentration(this.id)){
                                this.party1.waitForMember(this.id);
                                this.party1.crawlOut(this.id);
                            }
                            break;
                        case 2:
                            this.party2.waitForReverseDirection();
                            while(!party2.atConcentration(this.id)){
                                this.party2.waitForMember(this.id);
                                this.party2.crawlOut(this.id);
                            }
                            break;
                    }
                    hasCanvas = true;
                    this.log.updateThiefSituation(this.id, 'W');
                    this.state = ThievesState.OUTSIDE;
                    break;
            }
            this.log.setThiefState(state,this.id);
        }
    }
}
