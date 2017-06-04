package serverSide.assault_party;

import clientSide.Master.MasterState;
import clientSide.Thieves.ThievesState;
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
 * Assault party instance.
 * @author João Brito, 68137
 */
public class AssaultParty implements IMaster, IThieves{
    private boolean partyReady = false;
    private int party_number = 0;
    private int counterToCrawlBack = 0;
    private int room_id;
    private int nElemParty = 0;
    private int roomDistance = 0;
    private final LinkedList<Integer> nextElem;
    
    /**
     * Initiate the assault party#1.
     */
    public AssaultParty(){
        nextElem = new LinkedList<>();
    }
    
    /**
     * The thieves will wait for the Master to send the assault party. Thieves method.
     * @param id thief id.
     * @return room number to assault.
     */
    @Override
    public synchronized int waitForSendAssaultParty(int id) {
        partyReady=false;
        if(nElemParty==3){
            nElemParty=0;
            counterToCrawlBack = 0;
            notifyAll();
        }
        nElemParty++;
        notifyAll();
        while(!partyReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return room_id;
    }
    
    /**
     * The Master will send the assault party. Master method.
     */
    @Override
    public synchronized void sendAssaultParty(int aid) {
        party_number = aid;
        partyReady = false;
        if(party_number == 1){
            room_id = getAssaultParty1RoomId();
        }else{
            room_id = getAssaultParty2RoomId();
        }
        roomDistance = getRoomDistance(room_id);
        while(this.nElemParty<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        partyReady = true;
        while(!nextElem.isEmpty()){
            nextElem.remove();
        }
        if(party_number == 1){
            nextElem.add(getAssaultPartyElemId(1, 1));
            nextElem.add(getAssaultPartyElemId(1, 2));
            nextElem.add(getAssaultPartyElemId(1, 3));
        }else{
            nextElem.add(getAssaultPartyElemId(2, 1));
            nextElem.add(getAssaultPartyElemId(2, 2));
            nextElem.add(getAssaultPartyElemId(2, 3));
        }
        setMasterState(MasterState.WAITING_FOR_GROUP_ARRIVAL);
        notifyAll();
    }
    
    /**
     * Checks if thief is at the museum. Thieves method.
     * @param id thief id.
     * @return true or false.
     */
    @Override
    public synchronized boolean atMuseum(int id) {
        if(getAssaultPartyElemPosition(id) == roomDistance){
            setThiefState(ThievesState.AT_A_ROOM, id);
            return true;
        }
        return false;
    }

    /**
     * The thieves wait for another thief to crawl. Thieves method.
     * @param id thief id.
     */
    @Override
    public synchronized void waitForMember(int id) {
        while(id!=nextElem.getFirst()){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Implements the crawl in movement. Thieves method.
     * @param id thief id.
     */
    @Override
    public synchronized void crawlIn(int id) {
        int nextPosition;
        int nextElemToCrawlPosition = getAssaultPartyElemPosition(id);
        int nextElemToCrawlMaxDispl = getThiefMaxDisplacement(id);
        int lastElemToCrawlPosition = getAssaultPartyElemPosition(nextElem.getLast());
        
        if(nextElemToCrawlPosition+nextElemToCrawlMaxDispl>=lastElemToCrawlPosition+3){
            nextPosition = lastElemToCrawlPosition+3;
            if(nextPosition>=roomDistance){
                nextPosition = roomDistance;
            }
            nextPosition = checkPosition(nextPosition,id);
            setNextElemToCrawl();
        }else{
            nextPosition = nextElemToCrawlPosition+nextElemToCrawlMaxDispl;
            nextPosition = checkPosition(nextPosition,id);
            if(nextPosition >= roomDistance){
                nextPosition = roomDistance;
                setNextElemToCrawl();
            }
        }
        updateAssautPartyElemPosition(id, nextPosition);
        notifyAll();
    }
    
    /**
     * Set the next element to crawl.
     */
    private synchronized void setNextElemToCrawl(){
        int tmp = nextElem.getFirst();
        nextElem.pop();
        nextElem.add(tmp);
        notifyAll();
    }
    
    /**
     * Checks a thief position after crawl movement. Avoids collision.
     * @param pos thief position.
     * @param id thief id.
     * @return thief position.
     */
    private int checkPosition(int pos, int id){
        int elem_id;
        for(int i=1;i<=3;i++){
            elem_id=getAssaultPartyElemId(party_number, i);
            if(elem_id!=id){
                if(getAssaultPartyElemPosition(elem_id)==pos && getAssaultPartyElemPosition(elem_id)!=roomDistance){
                    pos -= 1;
                    break;
                }
            }
        }
        return pos;
    }

    /**
     * The thieves will await for all to be prepare to crawl out. Thieves method.
     * @param id thief identification
     */
    @Override
    public synchronized void waitForReverseDirection(int id) {
        counterToCrawlBack++;
        setThiefState(ThievesState.CRAWLING_OUTWARDS, id);
        notifyAll();
        while(counterToCrawlBack<3){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Checks if a thief is at the concentration site. Thieves method.
     * @param id thief id.
     * @return true or false.
     */
    @Override
    public synchronized boolean atConcentration(int id) {
        if(getAssaultPartyElemPosition(id) == 0){
            setThiefState(ThievesState.OUTSIDE, id);
            return true;
        }
        return false;
    }
    
    /**
     * Simulates the crawl movement back to the concentration site.
     * @param id thief id.
     */
    @Override
    public synchronized void crawlOut(int id) {
        int nextPosition;
        int nextElemToCrawlPosition = getAssaultPartyElemPosition(id);
        int nextElemToCrawlMaxDispl = getThiefMaxDisplacement(id);
        int lastElemToCrawlPosition = getAssaultPartyElemPosition(nextElem.getLast());
        
        if(nextElemToCrawlPosition-nextElemToCrawlMaxDispl<=lastElemToCrawlPosition-3){
            nextPosition = lastElemToCrawlPosition-3;
            if(nextPosition<=0){
                nextPosition = 0;
            }
            nextPosition = checkPositionBack(nextPosition,id);
            setNextElemToCrawl();
        }else{
            nextPosition = nextElemToCrawlPosition-nextElemToCrawlMaxDispl;
            nextPosition = checkPositionBack(nextPosition,id);
            if(nextPosition <= 0){
                nextPosition = 0;
                setNextElemToCrawl();
            }
        }
        updateAssautPartyElemPosition(id, nextPosition);
    }
    
    /**
     * Checks a thief position after crawl movement. Avoids collision.
     * @param pos thief position.
     * @param id thief id.
     * @return thief position.
     */
    private int checkPositionBack(int pos, int id){
        int elem_id;
        for(int i=1;i<=3;i++){
            elem_id=getAssaultPartyElemId(1, i);
            if(elem_id!=id && elem_id!=0){
                if(getAssaultPartyElemPosition(elem_id)==pos && getAssaultPartyElemPosition(elem_id)!=0){
                    pos += 1;
                    break;
                }
            }
        }
        return pos;
    }

    /* ASSAULT_PARTY AS A CLIENT */
    
    /**
     * Get Assault Party1 server com.
     * @return room id
     */
    private int getAssaultParty1RoomId() {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.GET_PARTY1_ROOM_ID);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.RESPONSE_INTEGER){
            System.out.println("Party: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * ServerCom, Get assault party 2 room id.
     * @return room id
     */
    private int getAssaultParty2RoomId() {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.GET_PARTY2_ROOM_ID);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.RESPONSE_INTEGER){
            System.out.println("Party: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * ServerCom, get room distance.
     * @param room_id room number
     * @return distance to room
     */
    private int getRoomDistance(int room_id) {
    ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.GET_DISTANCE, room_id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.RESPONSE_INTEGER){
            System.out.println("Party: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * ServerCom, get assault party element id
     * @param party party number
     * @param i element number
     * @return thief id
     */
    private int getAssaultPartyElemId(int party, int i) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.GET_PARTY_ELEM_ID, party, i);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.RESPONSE_INTEGER){
            System.out.println("Party: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * ServerCom, set master state
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
            System.out.println("Party: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * ServerCom, get party element position
     * @param id thief id
     * @return position
     */
    private int getAssaultPartyElemPosition(int id) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.GET_PARTY_ELEM_POS, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.RESPONSE_INTEGER){
            System.out.println("Party: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * ServerCom, set thief state
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
            System.out.println("Party: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * ServerCom, get thief max displacement
     * @param id thief id
     * @return max displacement
     */
    private int getThiefMaxDisplacement(int id) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.GET_MAX_DISPLACEMENT, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.RESPONSE_INTEGER){
            System.out.println("Party: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    /**
     * ServerCom, update party element position
     * @param id thief id
     * @param nextPosition next position 
     */
    private void updateAssautPartyElemPosition(int id, int nextPosition) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.UPDATE_PARTY_ELEM_POS, id, nextPosition);
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