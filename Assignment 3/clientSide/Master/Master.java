package clientSide.Master;

import structures.enumerates.MasterState;
import interfaces.*;
import structures.vectorClock.VectorTimestamp;
import java.rmi.RemoteException;

/**
 * Master instance.
 * @author João Brito, 68137
 */
public class Master extends Thread{
    
    private final MasterState state;
    
    private final ControlCollectionSiteInterface control;
    private final ConcentrationSiteInterface concentration;
    private final AssaultPartyInterface party1;
    private final AssaultPartyInterface party2;
    private final LogInterface log;
    
    private final VectorTimestamp myClock;
    private VectorTimestamp receivedClock;
    
    /**
     * It will be passed to the Master the methods it has access.
     * @param control
     * @param concentration
     * @param party1
     * @param party2
     * @param log
     */
    public Master(ControlCollectionSiteInterface control, ConcentrationSiteInterface concentration, AssaultPartyInterface party1, AssaultPartyInterface party2, LogInterface log){
        this.setName("Master");
        this.control = control;
        this.concentration = concentration;
        this.party1 = party1;
        this.party2 = party2;
        this.log = log;
        state = MasterState.PLANNING_THE_HEIST;
        
        myClock = new VectorTimestamp(1,0);
    }
    
    /**
     * This function represents the life cycle of Master
     */
    @Override
    public void run(){
        try{
            boolean heistOver = false;
            int[] decision;
            System.out.println("PLTH");
            // PLANNING_THE_HEIST
            myClock.increment();
            receivedClock = startOperations(myClock.clone());
            myClock.update(receivedClock);
            while(!heistOver){    
                // DECIDING_WHAT_TO_DO
            System.out.println("DWTD");
                decision = appraiseSit();
                if(decision[0]==1){
                    myClock.increment();
                    receivedClock = prepareAssaultParty(decision[1], myClock.clone());
                    myClock.update(receivedClock);
            System.out.println("ASAG");
                    // ASSEMBLING_A_GROUP
                    myClock.increment();
                    receivedClock = waitForPrepareExcursion(myClock.clone());
                    myClock.update(receivedClock);
                    if(decision[1]==1){
                        myClock.increment();
                        receivedClock = sendAssaultParty(1, myClock.clone());
                        myClock.update(receivedClock);
                        myClock.increment();
                        receivedClock = sendAssaultParty2(2, myClock.clone());
                        myClock.update(receivedClock);
                    }else{
                        myClock.increment();
                        receivedClock = sendAssaultParty(1, myClock.clone());
                        myClock.update(receivedClock);
                    }
            System.out.println("WFGA");
                    // WAITING_FOR_GROUP_ARRIVAL
                    myClock.increment();
                    receivedClock = takeARest(myClock.clone());
                    myClock.update(receivedClock);
                    // DECIDING_WHAT_TO_DO
                }else{
            System.out.println("PRTR");
                    myClock.increment();
                    receivedClock = sumUpResults(myClock.clone());
                    myClock.update(receivedClock);
                    // PRESENTING_THE_REPORT
                    heistOver = true;
                }  
            }
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    /**
     * Starts the Heist.
     */
    private VectorTimestamp startOperations(VectorTimestamp vt) throws RemoteException {
        return control.startOperations(vt);
    }

    /**
     * Decides what to do next.
     * @return the decision
     */
    private int[] appraiseSit() throws RemoteException {
        return control.appraiseSit();
    }

    /**
     * Informs the thieves to get ready.
     * @param lastAssault if it is the last assault or not
     */
    private VectorTimestamp prepareAssaultParty(int lastAssault, VectorTimestamp vt) throws RemoteException {
        return concentration.prepareAssaultParty(lastAssault, vt);
    }

    /**
     * Awaits for the prepareExcursion of the thieves
     */
    private VectorTimestamp waitForPrepareExcursion(VectorTimestamp vt) throws RemoteException {
        return concentration.waitForPrepareExcursion(vt);
    }

    /**
     * Sends the assault party.
     * @param idParty party identification
     */
    private VectorTimestamp sendAssaultParty(int idParty, VectorTimestamp vt) throws RemoteException {
        return party1.sendAssaultParty(idParty, vt);
    }

    /**
     * Awaits for the returns of the assault parties.
     */
    private VectorTimestamp takeARest(VectorTimestamp vt) throws RemoteException {
        return control.takeARest(vt);
    }

    /**
     * Informs the heist results.
     */
    private VectorTimestamp sumUpResults(VectorTimestamp vt) throws RemoteException {
        return concentration.sumUpResults(vt);
    }

    /**
     * Send assault party2.
     * @param idParty party identification
     */
    private VectorTimestamp sendAssaultParty2(int idParty, VectorTimestamp vt) throws RemoteException {
        return party2.sendAssaultParty(idParty, vt);
    }
    
}
