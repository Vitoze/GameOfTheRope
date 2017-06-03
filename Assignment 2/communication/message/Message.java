package communication.message;

import clientSide.Master.MasterState;
import clientSide.Thieves.ThievesState;
import java.io.Serializable;

/**
 * This data type define the messages used by the client and the server.
 * Implements a client-server model, and the communication is based in Message type objects on a TCP channel.
 * @author João Brito
 */
public class Message implements Serializable{
    
    /**
     * Integer error - minimum value integer.
     */
    public static final int ERROR_INT = Integer.MIN_VALUE;
    
    /**
     * Char error.
     */
    public static final char ERROR_CHAR = 0xFFFE;
    
    /**
     * Identification serial version.
     */
    private static final long serialVersionUID = 1001L;
    
    /**
     * Message type.
     */
    private MessageType type;
    
    /**
     * Thief identification.
     */
    private int idThief;
    
    /**
     * Master state.
     */
    private MasterState masterState;
    
    /**
     * Thief state.
     */
    private ThievesState thiefState;
      
    /**
     * Party identification.
     */
    private int idParty;
    
    /**
     * Room identification.
     */
    private int idRoom;
    
    /**
     * Distance.
     */
    private int distance;
    
    /**
     * Number of paintings.
     */
    private int paintings;
    
    /**
     * Canvas.
     */
    private int cv;
    
    /**
     * Position.
     */
    private int pos;
    
    /**
     * Thief situation.
     */
    private char situation;
    
    /**
     * Maximum Displacement.
     */
    private int maxDisplacement;
    
    /**
     * Last Assault.
     */
    private int lastAssault;
    
    private int action1;
    
    private int action2;
    
    /**
     * Array of decisions.
     */
    private int[] decision;
    
    /**
     * Position party.
     */
    private int partyPos;
    
    private int value;
    
    /**
     * Empty constructor fot the message that initializes the default values for the previous variables.
     */
    private Message(){
        idThief = ERROR_INT;
        masterState = null;
        thiefState = null;
    }
        
    /**
     * Constructor for a message class 0.
     * @param type is the type of message
     */
    public Message(MessageType type){
        this();
        this.type = type;
        
        switch(type){
            case ACK:
                break;
            case TERMINATE:
            	break;
            case POSITIVE:
                break;
            case NEGATIVE:
                break;
            case START_OPERATIONS:
                break;
            case APPRAISE_SIT:
                break;
            case WAIT_FOR_PREPARE_EXCURSION:
                break;
            case TAKE_A_REST:
                break;
            case SUM_UP_RESULTS:
                break;
            case NEW_HEIST:
                break;
            case INIT_PARTY_ELEM_ID:
                break;
            case PRINT_RESULTS:
                break;
            case GET_PARTY1_ROOM_ID:
                break;
            case GET_PARTY2_ROOM_ID:
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for a message class 1.
     * @param messageType is the type of message
     * @param value int value from the message
     */
    public Message(MessageType messageType, int value){
        this();
        this.type = messageType;
        
        switch(type){
            case AM_I_NEEDED:
                this.idThief = value;
                break;
            case PREPARE_EXCURSION:
                this.idThief = value;
                break;
            case WAIT_FOR_SEND_ASSAULT_PARTY:
                this.idThief = value;
                break;
            case AT_MUSEUM:
                this.idThief = value;
                break;
            case WAIT_FOR_MEMBER:
                this.idThief = value;
                break;
            case CRAWL_IN:
                this.idThief = value;
                break;
            case CRAWL_OUT:
                this.idThief = value;
                break;
            case AT_CONCENTRATION:
                this.idThief = value;
                break;
            case WAIT_FOR_REVERSE_DIRECTION:
                this.idThief = value;
                break;
            case PREPARE_ASSAULT_PARTY:
                this.lastAssault = value;
                break;
            case SEND_ASSAULT_PARTY:
                this.idParty = value;
                break;
            case GET_MUSEUM_PAINTINGS:
                this.idRoom = value;
                break;
            case GET_DISTANCE:
                this.idRoom = value;
                break;
            case GET_PARTY_ELEM_POS:
                this.idThief = value;
                break;
            case GET_MAX_DISPLACEMENT:
                this.idThief = value;
                break;
            case RESPONSE_INTEGER:
                this.value = value;
                break;
            case SET_PARTY1_ROOM_ID:
                this.idRoom = value;
                break;
            case SET_PARTY2_ROOM_ID:
                this.idRoom = value;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for a message class 2.
     * @param messageType is the type of message
     * @param value1 int value from the message
     * @param value2 int value from the message
     */
    public Message(MessageType messageType, int value1, int value2) {
        this();
        this.type = messageType;
        
        switch(type){
            case ROLL_A_CANVAS:
                this.idThief = value1;
                this.idRoom = value2;
                break;
            case UPDATE_MUSEUM:
                this.idRoom = value1;
                this.paintings = value2;
                break;
            case UPDATE_PARTY_ELEM_CV:
                this.idThief = value1;
                this.cv = value2;
                break;
            case SET_PARTY_ACTION:
                this.action1 = value1;
                this.action2 = value2;
                break;
            case UPDATE_PARTY_ELEM_ID:
                this.idParty = value1;
                this.idThief = value2;
                break;
            case GET_PARTY_ELEM_ID:
                this.idParty = value1;
                this.idThief = value2;
                break;
            case UPDATE_PARTY_ELEM_POS:
                this.idThief = value1;
                this.pos = value2;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for a message class 3.
     * @param messageType is the type of message
     * @param value1 int value from the message
     * @param value2 int value from the message
     * @param value3 int value from the message
     */
    public Message(MessageType messageType, int value1, int value2, int value3) {
        this();
        this.type = messageType;
        
        switch(type){
            case INIT_MUSEUM:
                this.idRoom = value1;
                this.distance = value2;
                this.paintings = value3;
                break;
            case SET_PARTY_MEMBER:
                this.idParty = value1;
                this.idThief = value3;
                this.partyPos = value2;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for a message class 4.
     * @param messageType is the type of message
     * @param value1 int value from the message
     * @param value2 int value from the message
     * @param value3 int value from the message
     * @param value4 int value from the message
     */
    public Message(MessageType messageType, int value1, int value2, int value3, int value4) {
        this();
        this.type = messageType;
        
        switch(type){
            case HAND_A_CANVAS:
                this.idThief = value1;
                this.idParty = value2;
                this.idRoom = value3;
                this.cv = value4;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for a message class 5.
     * @param messageType is the type of message
     * @param value1 int value from the message
     * @param value2 char value from the message
     */
    public Message(MessageType messageType, int value1, char value2){
        this();
        this.type = messageType;
        
        switch(type){
            case UPDATE_THIEF_SITUATION:
                this.idThief = value1;
                this.situation = value2;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for a message class 6.
     * @param messageType is the type of message
     * @param state master state
     */
    public Message(MessageType messageType, MasterState state) {
        this();
        this.type = messageType;
        
        switch(type){
            case SET_MASTER_STATE:
                this.masterState = state;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for a message class 7.
     * @param messageType is the type of message
     * @param state thief state
     * @param value thief id
     */
    public Message(MessageType messageType, ThievesState state, int value) {
        this();
        this.type = messageType;
        
        switch(type){
            case SET_THIEF_STATE:
                this.thiefState = state;
                this.idThief = value;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }

    /**
     * Constructor for a message class 8.
     * @param messageType is the type of message
     * @param state thief state
     * @param value1 thief id
     * @param value2 thief situation
     * @param value3 thief max displacement
     */
    public Message(MessageType messageType, ThievesState state, int value1, char value2, int value3) {
        this();
        this.type = messageType;
        
        switch(type){
            case INIT_THIEF:
                this.thiefState = state;
                this.idThief = value1;
                this.situation = value2;
                this.maxDisplacement = value3;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;    
        }
    }
    
    /**
     * Constructor for a message class 9.
     * @param messageType is the type of message
     * @param value array of interger values
     */
    public Message(MessageType messageType, int[] value){
        this();
        this.type = messageType;
        
        switch(type){
            case RESPONSE_ARRAY:
                this.decision = value;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Get the current message type.
     * @return message type
     */
    public MessageType getType(){
        return type;
    }
    
    /**
     * Get the id of the thief.
     * @return idThief
     */
    public int getIdThief(){
        return idThief;
    }
    
    /**
     * Get the information about the Master state present in the current message.
     * @return masterState
     */
    public MasterState getMasterState(){
        return masterState;
    }
    
    /**
     * Get the information about the Thief state present in the current message.
     * @return thiefState
     */
    public ThievesState getThiefState(){
        return thiefState;
    }
    
    /**
     * Get party identification.
     * @return party id
     */
    public int getIdParty(){
        return idParty;
    }
    
    /**
     * Get museum room number.
     * @return room number
     */
    public int getIdRoom(){
        return idRoom;
    }
    
    /**
     * Get canvas.
     * @return canvas
     */
    public int getCv(){
        return cv;
    }
    
    /**
     * Get position.
     * @return position
     */
    public int getPos(){
        return pos;
    }
    
    /**
     * Get last assault variable.
     * @return last assault
     */
    public int getLastAssault(){
        return lastAssault;
    }
    
    /**
     * Get thief situation.
     * @return thief situation
     */
    public char getThiefSituation(){
        return situation;
    }
    
    /**
     * Get thieves maximum displacement.
     * @return max displacement
     */
    public int getMaxDisplacement(){
        return maxDisplacement;
    }
    
    /**
     * Get party position.
     * @return party element position
     */
    public int getPartyPos(){
        return partyPos;
    }
    
    /**
     * Get thief situation
     * @return situation
     */
    public char getSituation(){
        return situation;
    }
    
    /**
     * Get action 1.
     * @return action
     */
    public int getAction1(){
        return action1;
    }
    
    /**
     * Get action 2.
     * @return action
     */
    public int getAction2(){
        return action2;
    }
    
    /**
     * Get distance.
     * @return distance
     */
    public int getDistance(){
        return distance;
    }
    
    /**
     * Get paintings.
     * @return paintings
     */
    public int getPaintings(){
        return paintings;
    }
    
    /**
     * Get decision matrix.
     * @return decision
     */
    public int[] getDecision(){
        return decision;
    }
    
    /**
     * Get integer value.
     * @return integer
     */
    public int getInteger(){
        return value;
    }
    
    /**
     * Convert the message type to a readable/writable format.
     * @return message type as a string
     */
    @Override
    public String toString(){
        return this.type.toString();
    }
}
