package serverSide.control_collect_site;

/**
 * Master interface of Control Collection Site instance.
 * @author João Brito, 68137
 */
public interface IMaster {
    
    /**
     * Initiates the heist operations.
     */
    public void startOperations();
    
    /**
     * The master will wait until the arrival of the assault party. 
     */
    public void takeARest();
    
    /**
     * The master will decide what to do next.
     * @return decision.
     */
    public int[] appraiseSit();
    
}
