package clientSide.Master;

import communication.ClientCom;
import communication.SimulConfig;
import communication.message.Message;
import communication.message.MessageType;
import java.util.Arrays;

/**
 * Master instance.
 * @author João Brito, 68137
 */
public class Master extends Thread{
    
    /**
     *  The state of Master.
     *  @serialField state
     */
    private final MasterState state;
    
    /**
     * It will be passed to the Master the methods it has access.
     */
    public Master(){
        this.setName("Master");
        state = MasterState.PLANNING_THE_HEIST;
    }
    
    /**
     * This function represents the life cycle of Master
     */
    @Override
    public void run(){
        boolean heistOver = false;
        int[] decision;
        System.out.println("PLTH");
        // PLANNING_THE_HEIST
        startOperations();
        while(!heistOver){    
            // DECIDING_WHAT_TO_DO
        System.out.println("DWTD");
            decision = appraiseSit();
            if(decision[0]==1){
                prepareAssaultParty(decision[1]);
        System.out.println("ASAG");
                // ASSEMBLING_A_GROUP
                waitForPrepareExcursion();
                if(decision[1]==1){
                    sendAssaultParty(1);
                    sendAssaultParty2(2);
                }else{
                    sendAssaultParty(1);
                }
        System.out.println("WFGA");
                // WAITING_FOR_GROUP_ARRIVAL
                takeARest();
                // DECIDING_WHAT_TO_DO
            }else{
        System.out.println("PRTR");
                sumUpResults();
                // PRESENTING_THE_REPORT
                heistOver = true;
            }  
        }
    }

    /**
     * Starts the Heist.
     */
    private void startOperations() {
        ClientCom con = new ClientCom(SimulConfig.controlServerName, SimulConfig.controlServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.START_OPERATIONS);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        
    }

    /**
     * Decides what to do next.
     * @return the decision
     */
    private int[] appraiseSit() {
        ClientCom con = new ClientCom(SimulConfig.controlServerName, SimulConfig.controlServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.APPRAISE_SIT);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        int[] decision = null;
        if(type != MessageType.RESPONSE_ARRAY){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }else{
            decision = inMessage.getDecision();
        }
        con.close();
        return decision;
    }

    /**
     * Informs the thieves to get ready.
     * @param lastAssault if it is the last assault or not
     */
    private void prepareAssaultParty(int lastAssault) {
        ClientCom con = new ClientCom(SimulConfig.concentrationServerName, SimulConfig.concentrationServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.PREPARE_ASSAULT_PARTY, lastAssault);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Awaits for the prepareExcursion of the thieves
     */
    private void waitForPrepareExcursion() {
        ClientCom con = new ClientCom(SimulConfig.concentrationServerName, SimulConfig.concentrationServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.WAIT_FOR_PREPARE_EXCURSION);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Sends the assault party.
     * @param idParty party identification
     */
    private void sendAssaultParty(int idParty) {
        ClientCom con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SEND_ASSAULT_PARTY, idParty);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Awaits for the returns of the assault parties.
     */
    private void takeARest() {
        ClientCom con = new ClientCom(SimulConfig.controlServerName, SimulConfig.controlServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.TAKE_A_REST);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Informs the heist results.
     */
    private void sumUpResults() {
        ClientCom con = new ClientCom(SimulConfig.concentrationServerName, SimulConfig.concentrationServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SUM_UP_RESULTS);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        shutdown();
    }

    /**
     * Send assault party2.
     * @param idParty party identification
     */
    private void sendAssaultParty2(int idParty) {
        ClientCom con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SEND_ASSAULT_PARTY, idParty);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Closes the servers
     */
    private void shutdown() {
        /* Shutdown Log */
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.TERMINATE);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        
        /* Shutdown Assault Party #1 */
        con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.TERMINATE);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();

        /* Shutdown Assault Party #2 */
        con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.TERMINATE);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();

        /* Shutdown Museum */
        con = new ClientCom(SimulConfig.museumServerName, SimulConfig.museumServerPort);
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.TERMINATE);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();

        /* Shutdown Concentration Site */
        con = new ClientCom(SimulConfig.concentrationServerName, SimulConfig.concentrationServerPort);
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.TERMINATE);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();

        /* Shutdown Control & Collection Site */
        con = new ClientCom(SimulConfig.controlServerName, SimulConfig.controlServerPort);
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.TERMINATE);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message: " + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }
    
}
