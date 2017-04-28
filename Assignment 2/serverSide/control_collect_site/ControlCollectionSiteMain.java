package serverSide.control_collect_site;

import communication.ServerCom;
import communication.SimulConfig;
import communication.proxy.ClientProxy;
import java.io.IOException;
import java.net.SocketException;

/**
 * Control & Collection site main.
 * @author João Brito
 */
public class ControlCollectionSiteMain {
    
    public static void main(String[] args) throws SocketException, IOException{
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        ServerCom scon, sconi;
        ClientProxy cliProxy;
        
        /* estabelecimento do serviço */
        scon = new ServerCom(SimulConfig.controlServerPort);
        scon.start();
        
        ControlCollectionSite control = new ControlCollectionSite();
        ControlCollectionSiteInterface controlInterface = new ControlCollectionSiteInterface(control);
        System.out.println("******************************************************************\nControl service has started!");
        System.out.println("Server is listening.\n******************************************************************");
        
        /* processamento de dados */
        while(true){
            sconi = scon.accept();
            cliProxy = new ClientProxy(scon, sconi, controlInterface);
            cliProxy.start();
        }
        
    }
    
}
