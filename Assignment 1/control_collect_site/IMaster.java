/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control_collect_site;

/**
 *
 * @author João Brito
 */
public interface IMaster {
    
    public boolean sumUpResults();
    
    public void startOperations();
    
    public void updateMaster(String stat);
    
    public void sendAssaultParty();
	
    public void collectCanvas();
    
    public void takeARest();
    
    public int appraiseSit();
    
    public void waitForPrepareExcursion();
}
