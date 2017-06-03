package serverSide.assault_party;

import interfaces.AssaultPartyInterface;
import communication.ServerCom;
import communication.SimulConfig;
import communication.proxy.ClientProxy;
import java.io.IOException;
import java.net.SocketException;

/**
 * Assault Party #1 Main
 * @author João Brito
 */
public class AssaultPartyMain {
    
    public static void main(String[] args) throws SocketException, IOException{
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        ServerCom scon, sconi;
        ClientProxy cliProxy;
        
        /* estabelecimento do serviço */
        scon = new ServerCom(SimulConfig.partyServerPort);
        scon.start();
        
        AssaultParty party = new AssaultParty();
        AssaultPartyInterface partyInterface = new AssaultPartyInterface(party);
        System.out.println("******************************************************************\nAssaultParty service has started!");
        System.out.println("Server is listening.\n******************************************************************");
        
        /* processamento de dados */
        
        while(true){
            sconi = scon.accept();
            cliProxy = new ClientProxy(scon, sconi, partyInterface);
            cliProxy.start();
        }
        
    }
    
}
