package serverSide.concentration_site;

import communication.ServerCom;
import communication.message.Message;
import communication.message.MessageException;
import communication.message.MessageType;
import communication.proxy.ServerInterface;
import java.net.SocketException;

/**
 * Concentration site interface.
 * @author João Brito
 */
public class ConcentrationSiteInterface implements ServerInterface {
    
    /**
     * Concentration site.
     */
    private final ConcentrationSite concentration;
    /**
     * The boolean variable represents if the service terminates.
     */
    private boolean serviceEnded;
    
    public ConcentrationSiteInterface(ConcentrationSite concentration){
        this.concentration = concentration;
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
            case PREPARE_ASSAULT_PARTY:
                concentration.prepareAssaultParty(inMessage.getLastAssault());
                outMessage = new Message(MessageType.ACK);
                break;
            case WAIT_FOR_PREPARE_EXCURSION:
                concentration.waitForPrepareExcursion();
                outMessage = new Message(MessageType.ACK);
                break;
            case SUM_UP_RESULTS:
                concentration.sumUpResults();
                outMessage = new Message(MessageType.ACK);
                break;
            case AM_I_NEEDED:
                value = concentration.amINeeded(inMessage.getIdThief());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            case PREPARE_EXCURSION:
                value = concentration.prepareExcursion(inMessage.getIdThief());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
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
