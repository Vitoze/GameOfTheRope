package clientSide.Thieves;

import communication.ClientCom;
import communication.SimulConfig;
import communication.message.Message;
import communication.message.MessageType;
import java.util.Arrays;

/**
 * Thieves instance.
 * @author João Brito, 68137
 */
public class Thieves extends Thread {
    
    private final int id;
    private final ThievesState state;
    private final char s;
    private final int md;
    
    /**
     * It will be passed to the thief the methods that it has access.
     * @param id Thief identification.
     * @param md Thief maximum displacement.
     */
    public Thieves(int id, int md){
        this.id = id;
        this.s = 'W';
        this.md = md;
        this.setName("Thief"+this.id);
        state = ThievesState.OUTSIDE;
        initThieves(this.state,this.id, this.s, this.md);
        
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
        while(!heistOver){
            // OUTSIDE
        System.out.println("OUTS");
            if(amINeeded(this.id)==0){
                party = prepareExcursion(this.id);
                // CRAWLING_INWARDS
                if(party==1){
                    party_room=waitForSendAssaultParty(this.id);
                    while(!atMuseum(this.id)){
                        waitForMember(this.id);
                        crawlIn(this.id);
                    }
                }else{
                    party_room=waitForSendAssaultParty2(this.id);
                    while(!atMuseum2(this.id)){
                        waitForMember2(this.id);
                        crawlIn2(this.id);
                    }
                }
                // AT_A_ROOM
        System.out.println("ATAR");
                canvas = rollACanvas(this.id,party_room);
                // CRAWLING_OUTWARDS
                if(party==1){
                    waitForReverseDirection(this.id);
                    while(!atConcentration(this.id)){
                        waitForMember(this.id);
                        crawlOut(this.id);
                    }
                }else{
                    waitForReverseDirection2(this.id);
                    while(!atConcentration2(this.id)){
                        waitForMember2(this.id);
                        crawlOut2(this.id);
                    }
                }
                handACanvas(this.id, party, party_room, canvas);
            }else{
                heistOver = true;
            }
        }
    }

    /**
     * Create a new thief in log.
     * @param state thief state
     * @param id thief id
     * @param s thief situation
     * @param md thief max displacement
     */
    private void initThieves(ThievesState state, int id, char s, int md) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.INIT_THIEF, state, id, s, md);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        System.out.println("InitThievesSucess");
    }

    /**
     * Thief wait for to be needed
     * @param id thief id
     * @return what to do next
     */
    private int amINeeded(int id) {
        ClientCom con = new ClientCom(SimulConfig.concentrationServerName, SimulConfig.concentrationServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.AM_I_NEEDED, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.RESPONSE_INTEGER ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
            int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * Thief get ready to go.
     * @param id thief id
     * @return 
     */
    private int prepareExcursion(int id) {
        ClientCom con = new ClientCom(SimulConfig.concentrationServerName, SimulConfig.concentrationServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.PREPARE_EXCURSION, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.RESPONSE_INTEGER) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * Waits for Master's sendAssaultParty
     * @param id thief id
     * @return room id
     */
    private int waitForSendAssaultParty(int id) {
        ClientCom con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WAIT_FOR_SEND_ASSAULT_PARTY, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.RESPONSE_INTEGER) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * Checks if it is at Museum already
     * @param id thief id
     * @return true or false
     */
    private boolean atMuseum(int id) {
        ClientCom con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.AT_MUSEUM, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        boolean value = false;
        if(type == MessageType.POSITIVE){
            value = true;
        }else if(type == MessageType.NEGATIVE){
            value = false;
        }
        else{
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return value;
    }

    /**
     * Waits to be the next one to move.
     * @param id thief id
     */
    private void waitForMember(int id) {
        ClientCom con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WAIT_FOR_MEMBER, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Crawls towards the museum
     * @param id thief id
     */
    private void crawlIn(int id) {
        ClientCom con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.CRAWL_IN, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Crawls to the concentration site
     * @param id thief id
     */
    private void crawlOut(int id) {
        ClientCom con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.CRAWL_OUT, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Thief hand a canvas to Master
     * @param id thief id
     * @param party party id
     * @param party_room room id
     * @param canvas has Canvas?
     */
    private void handACanvas(int id, int party, int party_room, int canvas) {
        ClientCom con = new ClientCom(SimulConfig.controlServerName, SimulConfig.controlServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.HAND_A_CANVAS, id, party, party_room, canvas);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Get a canvas from the museum
     * @param id thief id
     * @param party_room room id
     * @return has Canvas ? 
     */
    private int rollACanvas(int id, int party_room) {
        ClientCom con = new ClientCom(SimulConfig.museumServerName, SimulConfig.museumServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.ROLL_A_CANVAS, id, party_room);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.RESPONSE_INTEGER) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * Verify if it is at concentration site.
     * @param id thief id
     * @return true or false
     */
    private boolean atConcentration(int id) {
        ClientCom con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.AT_CONCENTRATION, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        boolean value = false;
        if(type == MessageType.POSITIVE){
            value = true;
        }else if(type == MessageType.NEGATIVE){
            value = false;
        }
        else{
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return value;
    }

    /**
     * Wait for all the thieves are ready to go back to concentration site.
     * @param id thief id
     */
    private void waitForReverseDirection(int id) {
        ClientCom con = new ClientCom(SimulConfig.partyServerName, SimulConfig.partyServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WAIT_FOR_REVERSE_DIRECTION, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Waits for Master's sendAssaultParty
     * @param id thief id
     * @return room id
     */
    private int waitForSendAssaultParty2(int id) {
        ClientCom con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WAIT_FOR_SEND_ASSAULT_PARTY, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.RESPONSE_INTEGER) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * Checks if it is at Museum already
     * @param id thief id
     * @return true or false
     */
    private boolean atMuseum2(int id) {
        ClientCom con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.AT_MUSEUM, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        boolean value = false;
        if(type == MessageType.POSITIVE){
            value = true;
        }else if(type == MessageType.NEGATIVE){
            value = false;
        }
        else{
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return value;
    }

    /**
     * Waits to be the next one to move.
     * @param id thief id
     */
    private void waitForMember2(int id) {
        ClientCom con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WAIT_FOR_MEMBER, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Crawls towards the museum
     * @param id thief id
     */
    private void crawlIn2(int id) {
        ClientCom con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.CRAWL_IN, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Wait for all the thieves are ready to go back to concentration site.
     * @param id thief id
     */
    private void waitForReverseDirection2(int id) {
        ClientCom con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WAIT_FOR_REVERSE_DIRECTION, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * Verify if it is at concentration site.
     * @param id thief id
     * @return true or false
     */
    private boolean atConcentration2(int id) {
        ClientCom con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.AT_CONCENTRATION, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        boolean value = false;
        if(type == MessageType.POSITIVE){
            value = true;
        }else if(type == MessageType.NEGATIVE){
            value = false;
        }
        else{
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return value;
    }

    /**
     * Crawls to the concentration site
     * @param id thief id
     */
    private void crawlOut2(int id) {
        ClientCom con = new ClientCom(SimulConfig.party2ServerName, SimulConfig.party2ServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.CRAWL_OUT, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK ) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

}
