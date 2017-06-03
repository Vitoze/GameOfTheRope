package serverSide.general_info_repo;

import communication.ServerCom;
import communication.message.Message;
import communication.message.MessageException;
import communication.message.MessageType;
import communication.proxy.ServerInterface;
import java.net.SocketException;

/**
 * Log Interface Stub.
 * @author João Brito
 */
public class LogInterface implements ServerInterface{
    
    /**
     * Instance of log. 
     */
    private final Log log;
    /**
     * The boolean variable represents if the service terminates.
     */
    public boolean serviceEnded;
    
    public LogInterface(Log log){
        this.log = log;
        this.serviceEnded = false;
    }

    /**
     * Processes the received messages and replies to the entity that sent it.
     * @param inMessage the received message
     * @param scon server communication
     * @return returns the reply to the received message
     * @throws MessageException
     * @throws SocketException 
     */
    @Override
    public Message processAndReply(Message inMessage, ServerCom scon) throws MessageException, SocketException {
        Message outMessage = null;
        int value;
        
        switch(inMessage.getType()){
            case TERMINATE:
                outMessage = new Message(MessageType.ACK);
                this.serviceEnded = true;
                break;
            case INIT_THIEF:
                log.initThieves(inMessage.getThiefState(), inMessage.getIdThief(), inMessage.getThiefSituation(), inMessage.getMaxDisplacement());
                outMessage = new Message(MessageType.ACK);
                break;
            case GET_PARTY1_ROOM_ID:
                value = log.getAssaultParty1RoomId();
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            case GET_PARTY2_ROOM_ID:
                value = log.getAssaultParty2RoomId();
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            case GET_PARTY_ELEM_POS:
                value = log.getAssaultPartyElemPosition(inMessage.getIdThief());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            case SET_THIEF_STATE:
                log.setThiefState(inMessage.getThiefState(), inMessage.getIdThief());
                outMessage = new Message(MessageType.ACK);
                break;
            case GET_MAX_DISPLACEMENT:
                value = log.getThiefMaxDisplacement(inMessage.getIdThief());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            case UPDATE_PARTY_ELEM_POS:
                log.updateAssautPartyElemPosition(inMessage.getIdThief(), inMessage.getPos());
                outMessage = new Message(MessageType.ACK);
                break;
            case SET_MASTER_STATE:
                log.setMasterState(inMessage.getMasterState());
                outMessage = new Message(MessageType.ACK);
                break;
            case SET_PARTY_MEMBER:    
                log.setAssaultPartyMember(inMessage.getIdParty(), inMessage.getPartyPos(), inMessage.getIdThief());
                outMessage = new Message(MessageType.ACK);
                break;
            case UPDATE_THIEF_SITUATION:    
                log.updateThiefSituation(inMessage.getIdThief(), inMessage.getSituation());
                outMessage = new Message(MessageType.ACK);
                break;
            case PRINT_RESULTS:
                log.printResults();
                outMessage = new Message(MessageType.ACK);
                break;
            case NEW_HEIST:
                log.newHeist();
                outMessage = new Message(MessageType.ACK);
                break;
            case INIT_PARTY_ELEM_ID:
                log.initAssaultPartyElemId();
                outMessage = new Message(MessageType.ACK);
                break;
            case SET_PARTY_ACTION:
                log.setAssaultPartyAction(inMessage.getAction1(), inMessage.getAction2());
                outMessage = new Message(MessageType.ACK);
                break;
            case UPDATE_PARTY_ELEM_CV:
                log.updateAssaultPartyElemCv(inMessage.getIdThief(), inMessage.getCv());
                outMessage = new Message(MessageType.ACK);
                break;
            case UPDATE_PARTY_ELEM_ID:
                log.updateAssaultPartyElemId(inMessage.getIdParty(), inMessage.getIdThief());
                outMessage = new Message(MessageType.ACK);
                break;
            case SET_PARTY1_ROOM_ID:
                log.setAssaultParty1RoomId(inMessage.getIdRoom());
                outMessage = new Message(MessageType.ACK);
                break;
            case SET_PARTY2_ROOM_ID:
                log.setAssaultParty2RoomId(inMessage.getIdRoom());
                outMessage = new Message(MessageType.ACK);
                break;
            case INIT_MUSEUM:
                log.initMuseum(inMessage.getIdRoom(), inMessage.getDistance(), inMessage.getPaintings());
                outMessage = new Message(MessageType.ACK);
                break;
            case GET_MUSEUM_PAINTINGS:
                value = log.getMuseumPaintings(inMessage.getIdRoom());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            case UPDATE_MUSEUM:
                log.updateMuseum(inMessage.getIdRoom(), inMessage.getPaintings());
                outMessage = new Message(MessageType.ACK);
                break;
            case GET_DISTANCE:
                value = log.getRoomDistance(inMessage.getIdRoom());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            case GET_PARTY_ELEM_ID:
                value = log.getAssaultPartyElemId(inMessage.getIdParty(), inMessage.getIdThief());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            default:
                System.out.println("Mensagem inválida recebida: " + inMessage);
                break;
        }
        return outMessage;
    }

    /**
     * Tell the service if it is allowed to end or not.
     * @return true if the system can terminate, false otherwise
     */
    @Override
    public boolean serviceEnded() {
        return serviceEnded;
    }
    
}
