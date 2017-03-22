/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general_info_repo;

import com.sun.javafx.binding.Logging;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.MasterState;
import entities.ThievesState;

/**
 * The log will be the gateway to all the information of the assault, trials,
 * strengths of the thieves. It will be responsible too for write in one file
 * with the log of the assault.
 * @author João Brito
 */
public class Log {
    
    private final Heist heist = Heist.getInstance();
    
    /*
        File where the log will be saved
    */
    private final File log;
    
    private static PrintWriter pw;
    
    /*
        This will be a singleton
    */
    private static Log instance = null;
    
    /*
        This will instantiate the log Class
        Name of the file where the log will be saved
        @param filename where the log will be saved
    */
    private Log(String filename){
        if(filename.length()==0){
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
            filename = "Heist_" + date.format(today) + ".log";
        }
        this.log = new File(filename);
    }
    
    /*
        This static constructor is used to init the log file, this is a singleton and we
        need to specify what is the filename, so we created this method that
        will allow to instantiate the singleton
    */
    static{
        instance = new Log("");
        instance.writeInit();
    }
    
    /*
        This is a singleton, this is important to return the Log instance.
        @return log instance, this is a singleton.
    */
    public synchronized static Log getInstance(){
        return instance;
    }
    
    /*
        This method writes the head of the logging file.
    */
    private void writeInit(){
        try{
            pw = new PrintWriter(log);
            pw.println("      Heist to the Museum - Description of the internal state");
            
            String head = "MstT";
            for(int i=1; i<=6; i++){
                head += " Thief " + Integer.toString(i);
            }
            pw.println(head);
            
            head = "Stat";
            for(int i=1; i<=6; i++){
                head += "  Stat S MD ";
            }
            pw.println(head);
            
            head = "Assault party 1                              Assault party 2                 Museum";
            pw.println(head);
            
            head = "";
            for(int i=1; i<=3; i++){
                head+= " Elem " + Integer.toString(i);
            }
            head += "      ";
            for(int i=1; i<=3; i++){
                head+= " Elem " + Integer.toString(i);
            }
            head += "       ";
            for(int i=1; i<=5; i++){
                head+= " Room " + Integer.toString(i);
            }
            pw.println(head);
            
            head = "RId ";
            for(int i=1; i<=3; i++){
                head+= " Id Pos Cv";
            }
            head += " RId";
            for(int i=1; i<=3; i++){
                head+= " Id Pos Cv";
            }
            head += "  ";
            for(int i=1; i<=5; i++){
                head+= " NP DT ";
            }
            pw.println(head);
            
            pw.flush();
        }catch(FileNotFoundException ex){
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *   This method will be called to finish write the logging file.
     */
    public synchronized void writeEnd(){
        pw.println("\nLegend:");
        pw.println("MstT Stat    – state of the master thief");
        pw.println("Thief # Stat - state of the ordinary thief # (# - 1 .. 6)");
        pw.println("Thief # S    - situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join party) or 'P' (in party)");
        pw.println("Thief # MD   - maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6");
        pw.println("Assault party # RId        - assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)");
        pw.println("Assault party # Elem # Id  - assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)");
        pw.println("Assault party # Elem # Pos - assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 ..  DT RId)");
        pw.println("Assault party # Elem # Cv  - assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)");
        pw.println("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls");
        pw.println("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30");
        pw.flush();
        pw.close();
    } 
    
    /**
     *  This method will be called on the start of the heist
     */
    public synchronized void newHeist(){
       this.printStatesLine();
    }
    
    public synchronized void initMasterState(MasterState state){
        this.heist.setMasterState(state);
    }
    
    public synchronized void setMasterState(MasterState state){
        this.heist.setMasterState(state);
        this.printStatesLine();
    }
    
    /**
     * Init the thief with the initial state, id, situation and maximum displacement.
     * @param state thief state.
     * @param id thief identification.
     * @param s thief situation, can be W or P.
     * @param md thief maximum displacement.
     */
    public synchronized void initThieves(ThievesState state, int id, char s, int md) {
        this.heist.setThievesState(id, state);
        this.heist.setThievesSituation(id, s);
        this.heist.setThievesMaxDisplacement(id, md);
    }
    
    public synchronized void setThiefState(ThievesState state, int id){
        this.heist.setThievesState(id, state);
        this.printStatesLine();
    }
    
    public synchronized void initMuseum(int id, int dt, int np) {
        this.heist.setMuseumRoomsDistance(id, dt);
        this.heist.setMuseumRoomsPaintings(id, np);
    }
    
    public synchronized void setAssaultPartyAction(int rid1, int rid2){
        this.heist.setAssaultParty1Rid(rid1);
        this.heist.setAssaultParty2Rid(rid2);
    }
    
    public synchronized void setAssaultParty1MemberState(int id, int nElemParty){
        this.heist.setAssaultParty1ElemId(id, nElemParty);
        this.heist.setAssaultParty1ElemPos(id, 0);
        this.heist.setAssaultParty1ElemCv(id, 0);
    }
    
    public synchronized void updateAssaultParty1MemberState(int id, int pos, int cv){
        this.heist.setAssaultParty1ElemPos(id, pos);
        this.heist.setAssaultParty1ElemCv(id, cv);
        this.printAssaultLine();
    }
    
    public synchronized void setAssaultParty2MemberState(int id, int nElemParty){
        this.heist.setAssaultParty2ElemId(id, nElemParty);
        this.heist.setAssaultParty2ElemPos(id, 0);
        this.heist.setAssaultParty2ElemCv(id, 0);
    }
    
    public synchronized void updateAssaultParty2MemberState(int id, int pos, int cv){
        this.heist.setAssaultParty2ElemPos(id, pos);
        this.heist.setAssaultParty2ElemCv(id, cv);
        this.printAssaultLine();
    }
    
    public synchronized void printLine(){
        this.printStatesLine();
    }
    
    public synchronized void printALine(){
        this.printAssaultLine();
    }
    
    private void printStatesLine(){
        pw.print(this.heist.getMasterState());
        pw.print(" ");
        
        for(int i=1; i<=6; i++){
            pw.print(this.heist.getThiefState(i));
            pw.print(" ");
            pw.print(this.heist.getThiefSituation(i));
            pw.print(" ");
            pw.print(this.heist.getThiefMaxDisplacement(i));
            pw.print(" ");
        }
        
        pw.println();
        pw.flush();
    }
    
    private void printAssaultLine(){
        pw.print(this.heist.getAssaultParty1Rid());
        pw.print(" ");
        for(int i = 1; i<=3; i++){
            pw.print(this.heist.getAssaultParty1ElemId(i));
            pw.print(" ");
            pw.print(this.heist.getAssaultParty1ElemPos(i));
            pw.print(" ");
            pw.print(this.heist.getAssaultParty1ElemCv(i));
            pw.print(" ");
        }
        pw.print(" ");
        pw.print(this.heist.getAssaultParty2Rid());
        pw.print(" ");
        for(int i = 1; i<=3; i++){
            pw.print(this.heist.getAssaultParty2ElemId(i));
            pw.print(" ");
            pw.print(this.heist.getAssaultParty2ElemPos(i));
            pw.print(" ");
            pw.print(this.heist.getAssaultParty2ElemCv(i));
            pw.print(" ");
        }
        pw.print(" ");
        for(int i = 1; i<=5; i++){
            pw.print(this.heist.getMuseumRoomPaintings(i));
            pw.print(" ");
            pw.print(this.heist.getMuseumRoomDistance(i));
            pw.print(" ");
        }
        pw.println();
        pw.flush();      
    }

    public int getRoomDistance(int roomId) {
        return this.heist.getMuseumRoomDistance(roomId);
    }
    
}
