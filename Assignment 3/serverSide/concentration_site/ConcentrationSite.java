package serverSide.concentration_site;

import interfaces.IMasterConcentration;
import interfaces.IThievesConcentration;
import structures.enumerates.MasterState;
import structures.enumerates.ThievesState;
import communication.ClientCom;
import communication.SimulConfig;
import communication.message.Message;
import communication.message.MessageType;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Concentration Site instance.
 * @author João Brito, 68137
 */
public class ConcentrationSite implements IMasterConcentration, IThievesConcentration {
    private boolean callAssault = false;
    private boolean thievesReady = false;
    private boolean lastAssault = false;
    private boolean endHeist = false;
    private int orders = -1;
    private int counter1 = 0;
    private int counter2 = 0;
    private final LinkedList<Integer> thieves;

    /**
     * Init the concentration site.
     */
    public ConcentrationSite() {
        thieves = new LinkedList<>();
    }
    
    /**
     * The master will order to the thieves to begin to prepare the assault party.
     * @param last '0' last assault, '1' if not. Master method.
     */
    @Override
    public synchronized void prepareAssaultParty(int last) {
        thievesReady = false;
        notifyAll();
        if(last==0){
            lastAssault = true;
        }
        while(counter1!=0 || counter2!=0){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setMasterState(MasterState.ASSEMBLING_A_GROUP);
        callAssault = true;
        orders = 0;
        notifyAll();
        
    }
    
    /**
     * The thieves are sleeping in this method waiting for the master inform
     * the next assault. Thieves method.
     * @param id thief id.
     * @return order.
     */
    @Override
    public synchronized int amINeeded(int id) {
        this.callAssault = false;
        thieves.add(id);
        if(counter1>0){
            counter1--;
            notifyAll();
        }
        if(counter2>0){
            counter2--;
            notifyAll();
        }
        while((!callAssault || thieves.getFirst()!=id) && (!endHeist)){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        thieves.pop();
        if(lastAssault && thieves.size()==3){
            callAssault = false;
            notifyAll();
        }
        notifyAll();
        return orders;
    }
    
    /**
     * The master will wait until the thieves are ready to go. Master method.
     */
    @Override
    public synchronized void waitForPrepareExcursion() {
        while(!this.thievesReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * The thieves will begin to prepare the excursion to the assault party. Thieves method.
     * @param id thief id.
     * @return party number.
     */
    @Override
    public synchronized int prepareExcursion(int id) {
        int party;
        thievesReady = false;
        if(lastAssault){
            counter1++;
            party = 1;
            setAssaultPartyMember(party, counter1, id);
            if(counter1==3){
                thievesReady = true;
                notifyAll();
            }
            updateThiefSituation(id, 'P');
            return party;
        }
        
        if(counter1<3){
            counter1++;
            party = 1;
            setAssaultPartyMember(party, counter1, id);
        }else{
            counter2++;
            party = 2;
            setAssaultPartyMember(party, counter2, id);
            if(counter2 == 3){
                thievesReady = true;
                notifyAll();
            }
        }
        updateThiefSituation(id, 'P');
        setThiefState(ThievesState.CRAWLING_INWARDS, id);
        return party;    
    }
    
    /**
     * The master will present the result of the heist. Master method.
     */
    @Override
    public synchronized void sumUpResults() {
        this.orders = 1;
        this.endHeist = true;
        setMasterState(MasterState.PRESENTING_THE_REPORT);
        printResults();
        notifyAll();
    }
    
    /* CONCENTRATION SITE AS A CLIENT */

    /**
     * ServerCom, set master state.
     * @param masterState master state
     */
    private void setMasterState(MasterState masterState) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SET_MASTER_STATE, masterState);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Concentration: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * ServerCom, set party member
     * @param party party id
     * @param counter1 element number
     * @param id thief id
     */
    private void setAssaultPartyMember(int party, int counter1, int id) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SET_PARTY_MEMBER, party, counter1, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Concentration: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * ServerCom, update thief situation
     * @param id thief id
     * @param c thief situation
     */
    private void updateThiefSituation(int id, char c) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.UPDATE_THIEF_SITUATION, id, c);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Concentration: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * ServerCom, update thief state.
     * @param thievesState thief state
     * @param id thief id
     */
    private void setThiefState(ThievesState thievesState, int id) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SET_THIEF_STATE, thievesState, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Concentration: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * ServerCom, print heist results
     */
    private void printResults() {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.PRINT_RESULTS);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Concentration: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }
    
}
