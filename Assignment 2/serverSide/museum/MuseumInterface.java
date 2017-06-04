package serverSide.museum;

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
public class MuseumInterface implements ServerInterface{
    
    /**
     * Instance of museum
     */
    private final Museum museum;
    /**
     * The boolean variable represents if the service terminates.
     */
    private boolean serviceEnded;
    
    public MuseumInterface(Museum museum){
        this.museum = museum;
        this.serviceEnded = false;
    }

    @Override
    public Message processAndReply(Message inMessage, ServerCom scon) throws MessageException, SocketException {
        Message outMessage = null;
        switch(inMessage.getType()){
            case TERMINATE:
                outMessage = new Message(MessageType.ACK);
                this.serviceEnded = true;
                break;
            case ROLL_A_CANVAS:
                int value = museum.rollACanvas(inMessage.getIdThief(), inMessage.getIdRoom());
                outMessage = new Message(MessageType.RESPONSE_INTEGER, value);
                break;
            default:
                System.out.println("Mensagem inválida recebida: " + inMessage);
                break;
        }
        return outMessage;
    }

    @Override
    public boolean serviceEnded() {
        return serviceEnded;
    }
    
}
