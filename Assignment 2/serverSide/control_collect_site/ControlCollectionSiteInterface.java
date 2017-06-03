package serverSide.control_collect_site;

import communication.ServerCom;
import communication.message.Message;
import communication.message.MessageException;
import communication.message.MessageType;
import communication.proxy.ServerInterface;
import java.net.SocketException;

/**
 * Control Collection site interface.
 * @author João Brito
 */
public class ControlCollectionSiteInterface implements ServerInterface {
    
    /**
     * Instance of Control collection site.
     * 
     */
    private final ControlCollectionSite control;
    /**
     * The Boolean variable represents whether the service is finished.
     */
    private boolean serviceEnded;
    
    public ControlCollectionSiteInterface(ControlCollectionSite control){
        this.control = control;
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
        switch(inMessage.getType()){
            case TERMINATE:
                outMessage = new Message(MessageType.ACK);
                this.serviceEnded = true;
                break;
            case START_OPERATIONS:
                control.startOperations();
                outMessage = new Message(MessageType.ACK);
                break;
            case TAKE_A_REST:
                control.takeARest();
                outMessage = new Message(MessageType.ACK);
                break;
            case APPRAISE_SIT:
                int[] decision = control.appraiseSit();
                outMessage = new Message(MessageType.RESPONSE_ARRAY, decision);
                break;
            case HAND_A_CANVAS:
                control.handACanvas(inMessage.getIdThief(), inMessage.getIdParty(), inMessage.getIdRoom(), inMessage.getCv());
                outMessage = new Message(MessageType.ACK);
                break;
            default:
                System.out.println("Mensagem inválida  recebida: " + inMessage);
                break;
        }
        return outMessage;
    }

    /**
     * Tell the service if it is allowed to end or not.
     * @return true if the system can terminated, false otherwise
     */
    @Override
    public boolean serviceEnded() {
        return serviceEnded;
    }
    
}
