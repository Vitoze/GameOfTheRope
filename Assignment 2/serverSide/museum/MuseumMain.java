package serverSide.museum;

import communication.ServerCom;
import communication.SimulConfig;
import communication.proxy.ClientProxy;
import java.io.IOException;
import java.net.SocketException;

/**
 *
 * @author João Brito
 */
public class MuseumMain {
    
    public static void main(String[] args) throws SocketException, IOException{
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        ServerCom scon, sconi;
        ClientProxy cliProxy;
        
        /* estabelecimento do serviço */
        scon = new ServerCom(SimulConfig.concentrationServerPort);
        scon.start();
        
        Museum museum = new Museum();
        MuseumInterface museumInterface = new MuseumInterface(museum);
        System.out.println("******************************************************************\nMuseum service has started!");
        System.out.println("Server is listening.\n******************************************************************");
        
        /* processamento de dados */
        
        while(true){
            sconi = scon.accept();
            cliProxy = new ClientProxy(scon, sconi, museumInterface);
            cliProxy.start();
        }
        
    }
    
}
