package serverSide.concentration_site;

import communication.ServerCom;
import communication.SimulConfig;
import communication.proxy.ClientProxy;
import java.io.IOException;
import java.net.SocketException;

/**
 * Concentration Site Communication Interface Stub
 * @author João Brito
 */
public class ConcentrationSiteMain {
    
    public static void main(String[] args) throws SocketException, IOException{
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        ServerCom scon, sconi;
        ClientProxy cliProxy;
        
        /* estabelecimento do serviço */
        scon = new ServerCom(SimulConfig.concentrationServerPort);
        scon.start();
        
        ConcentrationSite concentration = new ConcentrationSite();
        ConcentrationSiteInterface concentrationInterface = new ConcentrationSiteInterface(concentration);
        System.out.println("******************************************************************\nConcentration Site service has started!");
        System.out.println("Server is listening.\n******************************************************************");
        
        /* processamento de dados */
        
        while(true){
            sconi = scon.accept();
            cliProxy = new ClientProxy(scon, sconi, concentrationInterface);
            cliProxy.start();
        }
        
    }
    
}
