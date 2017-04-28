/*
 * Distributed Systems
 */
package serverSide.museum;

import communication.ClientCom;
import communication.SimulConfig;
import communication.SimulParam;
import communication.message.Message;
import communication.message.MessageType;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import java.util.Random;
/**
 * Museum instance.
 * @author João Brito, 68137
 */
public class Museum implements IThieves{
    
    /**
     * Init the Museum instance.
     */
    public Museum(){
        for(int i=1; i<=SimulParam.N_ROOMS; i++){
            Random rand = new Random();
            //random.nextInt(max + 1 - min) + min
            int dt = rand.nextInt(SimulParam.N_MAX_DISTANCE+1-SimulParam.N_MIN_DISTANCE) + SimulParam.N_MIN_DISTANCE;
            int np = rand.nextInt(SimulParam.N_MAX_PAINTINGS+1-SimulParam.N_MIN_PAINTINGS) + SimulParam.N_MIN_PAINTINGS;
            initMuseum(i,dt,np);
            //System.out.println(i+" "+np);
        }
    }
    
    /**
     * Theft of a canvas from the museum. Thieves method.
     * @param id thief id.
     * @param rid room number.
     * @return '0' if there isn't more canvas.
     */
    @Override
    public synchronized int rollACanvas(int id, int rid) {
        if(getMuseumPaintings(rid)>0){
            updateMuseum(rid, getMuseumPaintings(rid)-1);
            updateAssaultPartyElemCv(id, 1);
            //System.out.println("Here");
            return 1;
        }else{
            return 0;
        }
    }

    private void initMuseum(int i, int dt, int np) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.INIT_MUSEUM, i, dt, np);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Museum: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    private int getMuseumPaintings(int rid) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.GET_MUSEUM_PAINTINGS, rid);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.RESPONSE_INTEGER){
            System.out.println("Museum: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        int out = inMessage.getInteger();
        con.close();
        return out;
    }

    private void updateMuseum(int rid, int i) {
        ClientCom con = new ClientCom(SimulConfig.logServerName, SimulConfig.logServerPort);
        Message inMessage, outMessage;
        
        while(!con.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.UPDATE_MUSEUM, rid, i);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if(type != MessageType.ACK){
            System.out.println("Museum: Tipo inválido!");
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
            System.out.println("Museum: Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }
}

