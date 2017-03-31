/*
 * Distributed Systems
 */
package entities;

import general_info_repo.Log;
/**
 * Thieves instance.
 * @author João Brito, 68137
 */
public class Thieves extends Thread {
    private final museum.IThieves museum;
    private final concentration_site.IThieves concentration;
    private final control_collect_site.IThieves collect;
    private final assault_party.IThieves party1;
    private final assault_party.IThieves party2;
    private final Log log;
    
    private final int id;
    private final ThievesState state;
    private final char s;
    private final int md;
    
    /**
     * It will be passed to the thief the methods that it has access.
     * @param id Thief identification.
     * @param md Thief maximum displacement.
     * @param museum Instance that implements Museum Thieves methods.
     * @param party1 Instance that implements Assault party#1 Thieves methods.
     * @param party2 Instance that implements Assault party#2 Thieves methods.
     * @param concentration Instance that implements Concentration site Thieves methods.
     * @param collect Instance that implements Collection site Thieves methods.
     */
    public Thieves(int id, int md, museum.IThieves museum, assault_party.IThieves party1, assault_party.IThieves party2, concentration_site.IThieves concentration, control_collect_site.IThieves collect){
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
        int canvas;
        int party_room;
        int party;
        while(!heistOver){
            // OUTSIDE
            if(this.concentration.amINeeded(this.id)==0){
                party = this.concentration.prepareExcursion(this.id);
                // CRAWLING_INWARDS
                if(party==1){
                    party_room=this.party1.waitForSendAssaultParty(this.id, this.md);
                    while(!party1.atMuseum(this.id)){
                        this.party1.waitForMember(this.id);
                        this.party1.crawlIn(this.id);
                    }
                }else{
                    party_room=this.party2.waitForSendAssaultParty(this.id, this.md);
                    while(!party2.atMuseum(this.id)){
                        this.party2.waitForMember(this.id);
                        this.party2.crawlIn(this.id);
                    }
                }
                // AT_A_ROOM
                canvas = this.museum.rollACanvas(this.id,party_room);
                // CRAWLING_OUTWARDS
                if(party==1){
                    this.party1.waitForReverseDirection(this.id);
                    while(!party1.atConcentration(this.id)){
                        this.party1.waitForMember(this.id);
                        this.party1.crawlOut(this.id);
                    }
                }else{
                    this.party2.waitForReverseDirection(this.id);
                    while(!party2.atConcentration(this.id)){
                        this.party2.waitForMember(this.id);
                        this.party2.crawlOut(this.id);
                    }
                }
                this.collect.handACanvas(this.id, party, party_room, canvas);
            }else{
                heistOver = true;
            }
        }
    }
}
