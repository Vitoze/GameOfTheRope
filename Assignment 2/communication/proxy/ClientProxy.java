package communication.proxy;

import communication.ServerCom;
import communication.message.Message;
import communication.message.MessageException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This data type defines the service thread for a server-client architecture.
 * The communication is based on messages over TCP sockets.
 * @author João Brito
 */
public class ClientProxy extends Thread{
    
    /**
     * Counter fot the launched threads.
     * @serialField  nProxy
     */
    private static int nProxy;
    
    /**
     * Communication channel.
     * @serialField sconi
     */
    private final ServerCom sconi;
    
    /**
     * Server interface.
     * @serialField sInterface
     */
    private final ServerInterface sInterface;
    
    /**
     * Server communication.
     * @serialField scon
     */
    private final ServerCom scon;
    
    /**
     * Server interface instantiations.
     * @param scon server communication
     * @param sconi communication channel
     * @param sInterface server interface
     */
    public ClientProxy(ServerCom scon, ServerCom sconi, ServerInterface sInterface){
        super("Proxy_" + getProxyId());
        this.sconi = sconi;
        this.sInterface = sInterface;
        this.scon = scon;
    }
    
    /**
     * Agent's life cycle.
     */
    @Override
    public void run(){
        Message inMessage, outMessage = null;
        inMessage = (Message) sconi.readObject();
        try{
            outMessage = sInterface.processAndReply(inMessage, scon);
        }catch(MessageException e){
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getMessageVal().toString());
            System.exit(1);
        } catch (SocketException ex) {
            Logger.getLogger(ClientProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        sconi.writeObject(outMessage);
        sconi.close();
        if(sInterface.serviceEnded()){
            System.out.println("Closing service .. Done!");
            System.exit(0);
        }
    }
    
    /**
     * Instantiation identifier generator.
     * @return instantiation identifier
     */
    private static int getProxyId(){
        Class<ClientProxy> cl = null;
        int proxyId;
        try{
            cl = (Class<ClientProxy>) Class.forName("communication.proxy.ClientProxy");
        }catch(ClassNotFoundException e){
            System.out.println("O tipo de dados ClientProxy não foi encontrado!");
            System.exit(1);
        }
        synchronized(cl){
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }
    
}
