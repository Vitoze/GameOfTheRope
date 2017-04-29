package serverSide.museum;

/**
 * Thieves interface of Museum instance.
 * @author João Brito, 68137
 */
public interface IThieves {
    
    /**
     * This represents the theft of a canvas.
     * @param id thief identification.
     * @param RId room number.
     * @return '0' if there isn't any canvas left, '1' if there is.
     */
    public int rollACanvas(int id, int RId);
}
