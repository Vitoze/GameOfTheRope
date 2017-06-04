package clientSide.Thieves;

import interfaces.AssaultPartyInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.ControlCollectionSiteInterface;
import interfaces.LogInterface;
import interfaces.MuseumInterface;
import java.rmi.RemoteException;
import structures.constants.SimulParam;
import structures.enumerates.ThievesState;
import structures.vectorClock.VectorTimestamp;

/**
 * Thieves instance.
 * @author João Brito, 68137
 */
public class Thieves extends Thread {
    
    private final int id;
    private final ThievesState state;
    private final char s;
    private final int md;
    
    private final ControlCollectionSiteInterface control;
    private final ConcentrationSiteInterface concentration;
    private final AssaultPartyInterface party1;
    private final AssaultPartyInterface party2;
    private final LogInterface log;
    private final MuseumInterface museum;
    
    private final VectorTimestamp myClock;
    private VectorTimestamp receivedClock;
    
    /**
     * It will be passed to the thief the methods that it has access.
     * @param id Thief identification.
     * @param md Thief maximum displacement.
     * @param control
     * @param concentration
     * @param party1
     * @param party2
     * @param museum
     * @param log
     */
    public Thieves(int id, int md, ControlCollectionSiteInterface control, ConcentrationSiteInterface concentration, AssaultPartyInterface party1, AssaultPartyInterface party2, MuseumInterface museum, LogInterface log){
        this.id = id;
        this.s = 'W';
        this.md = md;
        this.setName("Thief"+this.id);
        state = ThievesState.OUTSIDE;
        
        this.control = control;
        this.concentration = concentration;
        this.party1 = party1;
        this.party2 = party2;
        this.museum = museum;
        this.log = log;
        myClock = new VectorTimestamp(SimulParam.N_THIEVES, id-1);
        
        try{
            initThieves(this.state,this.id, this.s, this.md);
        } catch(RemoteException ex){
            ex.printStackTrace();
        }
        
        System.out.println("New Thief");
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
        try{
            while(!heistOver){
                // OUTSIDE
            System.out.println("OUTS");
                if(amINeeded(this.id)==0){
                    party = prepareExcursion(this.id);
                    // CRAWLING_INWARDS
                    if(party==1){
                        party_room=waitForSendAssaultParty(this.id);
                        while(!atMuseum(this.id)){
                            myClock.increment();
                            receivedClock = waitForMember(this.id, myClock.clone());
                            myClock.update(receivedClock);
                            myClock.increment();
                            receivedClock = crawlIn(this.id, myClock.clone());
                            myClock.update(receivedClock);
                        }
                    }else{
                        party_room=waitForSendAssaultParty2(this.id);
                        while(!atMuseum2(this.id)){
                            myClock.increment();
                            receivedClock = waitForMember2(this.id, myClock.clone());
                            myClock.update(receivedClock);
                            myClock.increment();
                            receivedClock = crawlIn2(this.id, myClock.clone());
                            myClock.update(receivedClock);
                        }
                    }
                    // AT_A_ROOM
            System.out.println("ATAR");
                    canvas = rollACanvas(this.id,party_room);
                    // CRAWLING_OUTWARDS
                    if(party==1){
                        myClock.increment();
                        receivedClock = waitForReverseDirection(this.id, myClock.clone());
                        myClock.update(receivedClock);
                        while(!atConcentration(this.id)){
                            myClock.increment();
                            receivedClock = waitForMember(this.id, myClock.clone());
                            myClock.update(receivedClock);
                            myClock.increment();
                            receivedClock = crawlOut(this.id, myClock.clone());
                            myClock.update(receivedClock);
                        }
                    }else{
                        myClock.increment();
                        receivedClock = waitForReverseDirection2(this.id, myClock.clone());
                        myClock.update(receivedClock);
                        while(!atConcentration2(this.id)){
                            myClock.increment();
                            receivedClock = waitForMember2(this.id, myClock.clone());
                            myClock.update(receivedClock);
                            myClock.increment();
                            receivedClock = crawlOut2(this.id, myClock.clone());
                            myClock.update(receivedClock);
                        }
                    }
                    myClock.increment();
                    receivedClock = handACanvas(this.id, party, party_room, canvas, myClock.clone());
                    myClock.update(receivedClock);
                }else{
                    heistOver = true;
                }
            }
        }catch(RemoteException e){
            e.printStackTrace();
        } 
    }

    /**
     * Create a new thief in log.
     * @param state thief state
     * @param id thief id
     * @param s thief situation
     * @param md thief max displacement
     */
    private void initThieves(ThievesState state, int id, char s, int md) throws RemoteException {
        log.initThieves(state, id, s, md);
    }

    /**
     * Thief wait for to be needed
     * @param id thief id
     * @return what to do next
     */
    private int amINeeded(int id) throws RemoteException {
        return concentration.amINeeded(id);
    }

    /**
     * Thief get ready to go.
     * @param id thief id
     * @return 
     */
    private int prepareExcursion(int id) throws RemoteException {
        return concentration.prepareExcursion(id);
    }

    /**
     * Waits for Master's sendAssaultParty
     * @param id thief id
     * @return room id
     */
    private int waitForSendAssaultParty(int id) throws RemoteException {
        return party1.waitForSendAssaultParty(id);
    }

    /**
     * Checks if it is at Museum already
     * @param id thief id
     * @return true or false
     */
    private boolean atMuseum(int id) throws RemoteException {
        return party1.atMuseum(id);
    }

    /**
     * Waits to be the next one to move.
     * @param id thief id
     */
    private VectorTimestamp waitForMember(int id, VectorTimestamp vt) throws RemoteException {
        return party1.waitForMember(id, vt);
    }

    /**
     * Crawls towards the museum
     * @param id thief id
     */
    private VectorTimestamp crawlIn(int id, VectorTimestamp vt) throws RemoteException {
        return party1.crawlIn(id, vt);
    }

    /**
     * Crawls to the concentration site
     * @param id thief id
     */
    private VectorTimestamp crawlOut(int id, VectorTimestamp vt) throws RemoteException {
        return party1.crawlOut(id, vt);
    }

    /**
     * Thief hand a canvas to Master
     * @param id thief id
     * @param party party id
     * @param party_room room id
     * @param canvas has Canvas?
     */
    private VectorTimestamp handACanvas(int id, int party, int party_room, int canvas, VectorTimestamp vt) throws RemoteException {
        return control.handACanvas(id, party, id, id, vt);
    }

    /**
     * Get a canvas from the museum
     * @param id thief id
     * @param party_room room id
     * @return has Canvas ? 
     */
    private int rollACanvas(int id, int party_room) throws RemoteException {
        return museum.rollACanvas(id, party_room);
    }

    /**
     * Verify if it is at concentration site.
     * @param id thief id
     * @return true or false
     */
    private boolean atConcentration(int id) throws RemoteException {
        return party1.atConcentration(id);
    }

    /**
     * Wait for all the thieves are ready to go back to concentration site.
     * @param id thief id
     */
    private VectorTimestamp waitForReverseDirection(int id, VectorTimestamp vt) throws RemoteException {
        return party1.waitForReverseDirection(id, vt);
    }

    /**
     * Waits for Master's sendAssaultParty
     * @param id thief id
     * @return room id
     */
    private int waitForSendAssaultParty2(int id) throws RemoteException {
        return party2.waitForSendAssaultParty(id);
    }

    /**
     * Checks if it is at Museum already
     * @param id thief id
     * @return true or false
     */
    private boolean atMuseum2(int id) throws RemoteException {
        return party2.atMuseum(id);
    }

    /**
     * Waits to be the next one to move.
     * @param id thief id
     */
    private VectorTimestamp waitForMember2(int id, VectorTimestamp vt) throws RemoteException {
        return party2.waitForMember(id, vt);
    }

    /**
     * Crawls towards the museum
     * @param id thief id
     */
    private VectorTimestamp crawlIn2(int id, VectorTimestamp vt) throws RemoteException {
        return party2.crawlIn(id, vt);
    }

    /**
     * Wait for all the thieves are ready to go back to concentration site.
     * @param id thief id
     */
    private VectorTimestamp waitForReverseDirection2(int id, VectorTimestamp vt) throws RemoteException {
        return party2.waitForReverseDirection(id, vt);
    }

    /**
     * Verify if it is at concentration site.
     * @param id thief id
     * @return true or false
     */
    private boolean atConcentration2(int id) throws RemoteException {
        return party2.atConcentration(id);
    }
    
    /**
     * Crawls to the concentration site
     * @param id thief id
     */
    private VectorTimestamp crawlOut2(int id, VectorTimestamp vt) throws RemoteException {
        return party2.crawlOut(id, vt);
    }

}
