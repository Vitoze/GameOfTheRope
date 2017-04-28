package communication.proxy;

import communication.ServerCom;
import communication.message.Message;
import communication.message.MessageException;
import java.net.SocketException;

/**
 * This file defines the server interface.
 * @author João Brito
 */
public interface ServerInterface {
    
    /**
     * Processes the received messages and replies to the entity that sent it.
     * @param inMessage the received message
     * @param scon server communication
     * @return returns the reply to the received message
     * @throws MessageException
     * @throws SocketException 
     */
    public Message processAndReply(Message inMessage, ServerCom scon) throws MessageException, SocketException;
    
    /**
     * Tell the service if it is allowed to end or not.
     * @return true if the system can terminate, false otherwise.
     */
    public boolean serviceEnded();
    
}
