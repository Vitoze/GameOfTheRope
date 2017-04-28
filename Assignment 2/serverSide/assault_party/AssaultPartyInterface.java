package serverSide.assault_party;

import communication.ServerCom;
import communication.message.Message;
import communication.message.MessageException;
import communication.message.MessageType;
import communication.proxy.ServerInterface;
import java.net.SocketException;

/**
 *
 * @author João Brito
 */
public class AssaultPartyInterface implements ServerInterface{
    
    private final AssaultParty party;
    private boolean serviceEnded;
    
    public AssaultPartyInterface(AssaultParty party){
        this.party = party;
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
        boolean flag;
        
        switch(inMessage.getType()){
            case TERMINATE:
                outMessage = new Message(MessageType.ACK);
                this.serviceEnded = true;
                break;
            case SEND_ASSAULT_PARTY:
                party.sendAssaultParty(inMessage.getIdParty());
                outMessage = new Message(MessageType.ACK);
                break;
            case CRAWL_IN:
                party.crawlIn(inMessage.getIdThief());
                outMessage = new Message(MessageType.ACK);
                break;
            case CRAWL_OUT:
                party.crawlOut(inMessage.getIdThief());
                outMessage = new Message(MessageType.ACK);
                break;
            case WAIT_FOR_SEND_ASSAULT_PARTY:
                value = party.waitForSendAssaultParty(inMessage.getIdThief());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            case WAIT_FOR_MEMBER:
                party.waitForMember(inMessage.getIdThief());
                outMessage = new Message(MessageType.ACK);
                break;
            case AT_MUSEUM:
                flag = party.atMuseum(inMessage.getIdThief());
                if(flag){
                    outMessage = new Message(MessageType.POSITIVE);
                }else{
                    outMessage = new Message(MessageType.NEGATIVE);
                }
                break;
            case AT_CONCENTRATION:
                flag = party.atConcentration(inMessage.getIdThief());
                if(flag){
                    outMessage = new Message(MessageType.POSITIVE);
                }else{
                    outMessage = new Message(MessageType.NEGATIVE);
                }
                break;
            case WAIT_FOR_REVERSE_DIRECTION:
                party.waitForReverseDirection(inMessage.getIdThief());
                outMessage = new Message(MessageType.ACK);
                break;
            default:
                System.out.println("Mensagem inválida recebido: " + inMessage);
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
