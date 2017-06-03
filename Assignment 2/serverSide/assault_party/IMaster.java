package serverSide.assault_party;

/**
 * Master interface of AssaultParty instance.
 * @author João Brito, 68137
 */
public interface IMaster {
    
    /**
     * The Master will send the assault party out.
     * @param aid assault party id
     */
    public void sendAssaultParty(int aid);
}