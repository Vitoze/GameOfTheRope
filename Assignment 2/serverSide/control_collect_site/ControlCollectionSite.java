/*
 *  Distributed Systems
 */
package serverSide.control_collect_site;

import clientSide.Master.MasterState;
import communication.ClientCom;
import communication.SimulConfig;
import java.util.HashMap;
import communication.SimulParam;
import communication.message.Message;
import communication.message.MessageType;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Control and Collection Site instance.
 *  @author João Brito, 68137
 */
public class ControlCollectionSite implements IMaster, IThieves {
    private boolean canvasCollected = false;
    private final HashMap<Integer, Boolean> museum;
    private int nElemToWait = 0;
    private int elemParty1 = 3;
    private int elemParty2 = 3;
    
    /**
     * Init the Control Site instance.
     */
    public ControlCollectionSite(){
        museum = new HashMap<>();
    }
    
    /**
     * In Master life cycle, transition between "Planning the heist" and "Deciding what to do",
     * initiates a heist. Master method.
     */
    @Override
    public void startOperations() {
        for(int rid=1; rid<=SimulParam.N_ROOMS; rid++){
            museum.put(rid, true);
        }
        newHeist();
        setMasterState(MasterState.DECIDING_WHAT_TO_DO);
    }
    
    /**
     * The Master decides what to do next
     * @return 1 to prepare new assault or 2 to end heist. Master method.
     */
    @Override
    public synchronized int[] appraiseSit() {
        nElemToWait = 6;
        elemParty1 = 3;
        elemParty2 = 3;
        canvasCollected = false;
        initAssaultPartyElemId();
        int assault_party1_rid = 0;
        int assault_party2_rid = 0;
        int decision[] = new int[2];
        decision[0] = 1;
        for(int rid=1; rid<=SimulParam.N_ROOMS; rid++){
            if(museum.get(rid)){
                assault_party1_rid = rid;
                break;
            }
            assault_party1_rid = 0;
        }
        if(assault_party1_rid==0){
            decision[0] = 2;
            return decision;
        }
        if(assault_party1_rid==SimulParam.N_ROOMS){
            assault_party2_rid = decision[1] = 0;
            nElemToWait = 3;
            setAssaultPartyAction(assault_party1_rid, assault_party2_rid);
            return decision;
        }else{
            for(int rid=assault_party1_rid+1; rid<=SimulParam.N_ROOMS; rid++){
                if(museum.get(rid)){
                    assault_party2_rid = rid;
                    break;
                }
            }
        }
        setAssaultPartyAction(assault_party1_rid, assault_party2_rid);
        decision[1] = 1;
        return decision;
    }
    
    /**
     * The master will wait for the assault party arrival. Master method.
     */
    @Override
    public synchronized void takeARest() {
        while(!canvasCollected){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setMasterState(MasterState.DECIDING_WHAT_TO_DO);
    }
    
    /**
     * The thief will hand a canvas. Thieves method.
     * @param id thief id.
     * @param rid room number.
     * @param cv has canvas? 0 or 1.
     */
    @Override
    public synchronized void handACanvas(int id, int party, int rid, int cv) {
        if(cv==0){
            museum.replace(rid, false);
        }
        nElemToWait--;
        if(nElemToWait==0){ 
            canvasCollected = true;
            notify();
        }
        updateThiefSituation(id, 'W');
        updateAssaultPartyElemCv(id, 0);
        updateAssaultPartyElemId(party, id);
        if(party==1){
            elemParty1--;
            if(elemParty1==0){
                setAssaultParty1RoomId(0);
            }
        }else{
            elemParty2--;
            if(elemParty2==0){
                setAssaultParty2RoomId(0);
            }
        }
    }
    
    /* CONTROL & COLLECTION SITE AS A CLIENT */

    private void newHeist() {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.NEW_HEIST);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

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
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    private void initAssaultPartyElemId() {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.INIT_PARTY_ELEM_ID);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    private void setAssaultPartyAction(int assault_party1_rid, int assault_party2_rid) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SET_PARTY_ACTION, assault_party1_rid, assault_party2_rid);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

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
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    private void updateAssaultPartyElemCv(int id, int i) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.UPDATE_PARTY_ELEM_CV, id, i);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    private void updateAssaultPartyElemId(int party, int id) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.UPDATE_PARTY_ELEM_ID, party, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    private void setAssaultParty1RoomId(int i) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SET_PARTY1_ROOM_ID, i);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    private void setAssaultParty2RoomId(int i) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.SET_PARTY2_ROOM_ID, i);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Control: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }
       
}
