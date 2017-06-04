package interfaces;

import java.rmi.Remote;

/**
 * Concentration site interface.
 * @author João Brito
 */
public interface ConcentrationSiteInterface extends Remote, IMasterConcentration, IThievesConcentration, LogInterface {    
    
}
