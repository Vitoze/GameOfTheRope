package serverSide.general_info_repo;

import communication.ServerCom;
import communication.SimulConfig;
import communication.proxy.ClientProxy;
import java.io.IOException;
import java.net.SocketException;

/**
 * Log Main.
 * @author João Brito
 */
public class LogMain {
    
    public static void main(String[] args) throws SocketException, IOException{
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        ServerCom scon, sconi;
        ClientProxy cliProxy;
        
        /* estabelecimento do serviço */
        scon = new ServerCom(SimulConfig.logServerPort);
        scon.start();
        
        Log log = Log.getInstance();
        LogInterface logInterface = new LogInterface(log);
        System.out.println("******************************************************************\nLog service has started!");
        System.out.println("Server is listening.\n******************************************************************");
        
        /* processamento de dados */
        
        while(true){
            sconi = scon.accept();
            cliProxy = new ClientProxy(scon, sconi, logInterface);
            cliProxy.start();
        }
        
    }
}
