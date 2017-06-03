package clientSide.Master;

/**
 * Master's main class.
 * @author João Brito
 */
public class MasterMain {
    
    public static void main(String[] args){
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println("******************************************************************\nEntity master has started!");
        System.out.println("******************************************************************");
        
        Master master = new Master();
        
        master.start();
        
        try{
            master.join();
            System.err.println("Master Died");
        }catch(InterruptedException ex){}
        
    }
    
}
