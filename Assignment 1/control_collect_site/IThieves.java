/*
 * Distributed Systems
 */
package control_collect_site;

/**
 * Thieves interface of Conctrol Collection Site instance.
 * @author João Brito, 68137
 */
public interface IThieves {
    
    /**
     * Represents the action of handing a canvas and move to the concentration site.
     * @param id thief id.
     * @param party party number.
     * @param rid room number.
     * @param cv has canvas? 0 or 1.
     */
    public void handACanvas(int id, int party, int rid, int cv);

}
