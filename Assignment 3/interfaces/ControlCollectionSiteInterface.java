package interfaces;

import java.rmi.Remote;

/**
 * Control and Collection site interface.
 * @author João Brito
 */
public interface ControlCollectionSiteInterface extends Remote, IMasterControl, IThievesControl, LogInterface {
    
}
