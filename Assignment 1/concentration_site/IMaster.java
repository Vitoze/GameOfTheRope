/*
 * Distributed Systems
 */
package concentration_site;

/**
 * Master interface of Concentration Site instance.
 * @author João Brito, 68137
 */
public interface IMaster {

    /**
     * The master will tell the thieves to prepare for a new assault to the museum.
     * @param last if it is the last assault, it will be signalized
     */
    public void prepareAssaultParty(int last);
    
    /**
     * The master will wait until there is enough thieves to begin a new assault.
     */
    public void waitForPrepareExcursion();
    
    /**
     * The master will end the heist and present the results.
     */
    public void sumUpResults();
    
}
