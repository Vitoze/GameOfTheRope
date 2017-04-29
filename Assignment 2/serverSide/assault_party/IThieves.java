package serverSide.assault_party;

/**
 * Tieves interface of AssaultParty#2 instance.
 * @author João Brito, 68137
 */
public interface IThieves {
    
    /**
     * The thieves wil crawl to the museum.
     * @param id thief if
     */
    public void crawlIn(int id);
    
    /**
     * The thieves will crawl to the concentration site.
     * @param id thief id.
     */
    public void crawlOut(int id);
    
    /**
     * The thieves will wait for the master to send the party.
     * @param id thief id.
     * @return museum room to assault.
     */
    public int waitForSendAssaultParty(int id);
    
    /**
     * The thieves will wait for other thief to make is crawl movement.
     * @param id thief id.
     */
    public void waitForMember(int id);
    
    /**
     * This method will check if a thief is already at the museum.
     * @param id thief id.
     * @return true or false.
     */
    public boolean atMuseum(int id);
    
    /**
     * This method will check if a thief is already at the concentration site.
     * @param id thief id.
     * @return true or false.
     */
    public boolean atConcentration(int id);
    
    /**
     * The thieves will wait for all to be ready to craw out.
     * @param id thief id
     */
    public void waitForReverseDirection(int id);
}
