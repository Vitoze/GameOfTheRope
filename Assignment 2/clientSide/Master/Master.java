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
        // PLANNING_THE_HEIST
        startOperations();
        while(!heistOver){    
            // DECIDING_WHAT_TO_DO
            decision = appraiseSit();
            if(decision[0]==1){
                prepareAssaultParty(decision[1]);
                // ASSEMBLING_A_GROUP
                waitForPrepareExcursion();
                if(decision[1]==1){
                    sendAssaultParty(1);
                    sendAssaultParty2(2);
                }else{
                    sendAssaultParty(1);
                }
                // WAITING_FOR_GROUP_ARRIVAL
                takeARest();
                // DECIDING_WHAT_TO_DO
            }else{
                sumUpResults();
                // PRESENTING_THE_REPORT
                heistOver = true;
            }  
        }
    }

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
    }

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
    
}
