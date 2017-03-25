/*
 * Distributed systems
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
 * @author João Brito, 68137
 */
public class Log {
    
    private final Heist heist = Heist.getInstance();
    /**
     * File where the log will be saved.
     */
    private final File log;
    
    private static PrintWriter pw;
    
    /**
     * This will be a singleton.
     */
    private static Log instance = null;
    
    /**
     *  This will instantiate the log Class
     *  Name of the file where the log will be saved
     *  @param filename where the log will be saved
     */
    private Log(String filename){
        if(filename.length()==0){
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
            filename = "Heist_" + date.format(today) + ".log";
        }
        this.log = new File(filename);
    }
    
    /**
     * This static constructor is used to init the log file, this is a singleton and we
     * need to specify what is the filename, so we created this method that
     * will allow to instantiate the singleton
     */
    static{
        instance = new Log("");
        instance.writeInit();
    }
    
    /**
     *   This is a singleton, this is important to return the Log instance.
     *   @return log instance, this is a singleton.
     */
    public synchronized static Log getInstance(){
        return instance;
    }
    
    /**
     *   This method writes the head of the logging file.
     */
    private void writeInit(){
        try{
            pw = new PrintWriter(log);
            pw.println("                            Heist to the Museum - Description of the internal state");
            
            String head = "MstT";
            for(int i=1; i<=6; i++){
                head += "   Thief " + Integer.toString(i)+"   ";
            }
            pw.println(head);
            
            head = "Stat  ";
            for(int i=1; i<=6; i++){
                head += "Stat S MD    ";
            }
            pw.println(head);
            pw.print("                   ");
            head = "Assault party 1                       Assault party 2                         Museum";
            pw.println(head);
            
            head = "           ";
            for(int i=1; i<=3; i++){
                head+= "Elem " + Integer.toString(i)+"     ";
            }
            head += "     ";
            for(int i=1; i<=3; i++){
                head+= "Elem " + Integer.toString(i)+"     ";
            }
            head += "";
            for(int i=1; i<=5; i++){
                head+= "Room " + Integer.toString(i)+"  ";
            }
            pw.println(head);
            
            head = "    RId  ";
            for(int i=1; i<=3; i++){
                head+= "Id Pos Cv  ";
            }
            head += "RId  ";
            for(int i=1; i<=3; i++){
                head+= "Id Pos Cv  ";
            }
            head += "  ";
            for(int i=1; i<=5; i++){
                head+= " NP DT  ";
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
        this.printResults();
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
        pw.print("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30");
        pw.flush();
        pw.close();
    } 
    
    /**
     *  This method will be called on the start of the heist
     */
    public synchronized void newHeist(){
       this.printStatesLine();
    }
    
    /**
     * This method will initiate the Master state.
     * @param state Master state.
     */
    public synchronized void initMasterState(MasterState state){
        this.heist.setMasterState(state);
    }
    
    /**
     * This method will set the Master state and update the log.
     * @param state Master state.
     */
    public synchronized void setMasterState(MasterState state){
        MasterState tmp = this.heist.getMasterState();
        this.heist.setMasterState(state);
        if(tmp!=state){
            this.printStatesLine();
        }
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
    
    /**
     * This method will return the thief maximum displacement.
     * @param id thief identification.
     * @return thief max displacement.
     */
    public synchronized int getThiefMaxDisplacement(int id){
        return this.heist.getThiefMaxDisplacement(id);
    }
    
    /**
     * This method will set the thief state and update the log.
     * @param state Thief state.
     * @param id Thief identification.
     */
    public synchronized void setThiefState(ThievesState state, int id){
        ThievesState tmp = this.heist.getThiefState(id);
        this.heist.setThievesState(id, state);
        if(tmp!=state){
            this.printStatesLine();
        }
    }
    
    /**
     * This method will update the thief situation.
     * @param id Thief identification
     * @param s Thief situation wither 'W' or 'P'
     */
    public synchronized void updateThiefSituation(int id, char s){
        this.heist.setThievesSituation(id, s);
    }
    
    /**
     * This method will initiate the museum, its room.
     * @param id Room number.
     * @param dt Room distance.
     * @param np Room paintings.
     */
    public synchronized void initMuseum(int id, int dt, int np) {
        this.heist.setMuseumRoomsDistance(id, dt);
        this.heist.setMuseumRoomsPaintings(id, np);
    }
    
    /**
     * This method will update the museum.
     * @param rid Room identification.
     * @param np Room number of paintings.
     */
    public synchronized void updateMuseum(int rid, int np){
        this.heist.setMuseumRoomsPaintings(rid, np);
        this.printAssaultLine();
    }
    
    /**
     * This method will return the number of paintings of a room.
     * @param rid Room identification.
     * @return Room number of paintings.
     */
    public synchronized int getMuseumPaintings(int rid){
        return this.heist.getMuseumRoomPaintings(rid);
    }
    
    /**
     * This method will return the room distance.
     * @param roomId room identification.
     * @return room distance.
     */
    public int getRoomDistance(int roomId) {
        return this.heist.getMuseumRoomDistance(roomId);
    }
    
    /**
     * This method will set the assault party member.
     * @param party assault party number.
     * @param i assault party member number.
     * @param id thief identification.
     */
    public synchronized void setAssaultPartyMember(int party, int i, int id){
        this.heist.setAssaultPartyElemId(party, i, id);
        this.heist.setAssaultPartyElemPos(id, 0);
        this.heist.setAssaultPartyElemNumber(id, i);
        this.heist.setAssaultPartyElemCv(id, 0);
    }
    
    /**
     * This method will return the assault party element number.
     * @param id thief identification.
     * @return assault party element number.
     */
    public synchronized int getAssaultPartyElemNumber(int id){
        return this.heist.getAssaultPartyElemNumber(id);
    }
    
    /**
     * This method will return the assault party member identification.
     * @param party assault party number.
     * @param i assault party element number.
     * @return assault party thief identification.
     */
    public synchronized int getAssaultPartyElemId(int party, int i){
        return this.heist.getAssaultPartyElemId(party, i);
    }
    
    /**
     * This method will return the assault party member position.
     * @param id thief identification.
     * @return thief position.
     */
    public synchronized int getAssaultPartyElemPosition(int id){
        return this.heist.getAssaultPartyElemPos(id);
    }
    
    /**
     * This method will return the assault party #1 room number to assault.
     * @return museum room number.
     */
    public synchronized int getAssaultParty1RoomId(){
        return this.heist.getAssaultParty1Rid();
    }
    
    /**
     * This method will return the assault party #2 room number to assault.
     * @return museum room number.
     */
    public synchronized int getAssaultParty2RoomId(){
        return this.heist.getAssaultParty2Rid();
    }
    
    /**
     * This method will update the assault party element position.
     * @param id thief identification.
     * @param pos thief position.
     */
    public synchronized void updateAssautPartyElemPosition(int id, int pos){
        this.heist.setAssaultPartyElemPos(id, pos);
        this.printAssaultLine();
    }
    
    /**
     * This method will return if the assault party member has a canvas.
     * @param party assault party number.
     * @param i assault party element number.
     * @return thief has canvas
     */
    public synchronized int getAssaultPartyElemCv(int party, int i){
        return this.heist.getAssaultPartyElemCv(party, i);
    }
    
    /**
     * This method will update the thief canvas.
     * @param id thief identification.
     * @param cv thief canvas state.
     */
    public synchronized void updateAssaultPartyElemCv(int id, int cv){
        this.heist.setAssaultPartyElemCv(id, cv);
        this.printAssaultLine();
    }
    
    /**
     * This method will set the assault parties objective.
     * @param rid1 assault party#1 room to invade.
     * @param rid2 assault party#2 room to invade.
     */
    public synchronized void setAssaultPartyAction(int rid1, int rid2){
        this.heist.setAssaultParty1Rid(rid1);
        this.heist.setAssaultParty2Rid(rid2);
    }
    
    /**
     * This method will update the assault log.
     */
    public synchronized void printALine(){
        this.printAssaultLine();
    }
    
    /**
     * This method will update the states log.
     */
    private void printStatesLine(){
        pw.print(this.heist.getMasterState());
        pw.print("  ");
        
        for(int i=1; i<=6; i++){
            pw.print(this.heist.getThiefState(i));
            pw.print(" ");
            pw.print(this.heist.getThiefSituation(i));
            pw.print("  ");
            pw.print(this.heist.getThiefMaxDisplacement(i));
            pw.print("    ");
        }
        
        pw.println();
        pw.flush();
    }
    
    /**
     * This method will update the assault log.
     */
    private void printAssaultLine(){
        pw.print("     ");
        pw.print(this.heist.getAssaultParty1Rid());
        pw.print("    ");
        for(int i = 1; i<=3; i++){
            pw.print(this.heist.getAssaultPartyElemId(1, i));
            pw.print("  ");
            pw.print(String.format("%1$2s", this.heist.getAssaultPartyElemPos(this.heist.getAssaultPartyElemId(1, i))));
            pw.print("  ");
            pw.print(this.heist.getAssaultPartyElemCv(1, i));
            pw.print("   ");
        }
        if(this.heist.getAssaultParty2Rid()==0){
            pw.print("-");
            pw.print("    ");
            for(int i = 1; i<=3; i++){
                pw.print("-");
                pw.print("  ");
                pw.print(String.format("%1$2s", "-"));
                pw.print("  ");
                pw.print("-");
                pw.print("   ");
            }
            pw.print("  ");
        }else{
            pw.print(this.heist.getAssaultParty2Rid());
            pw.print("    ");
            for(int i = 1; i<=3; i++){
                pw.print(this.heist.getAssaultPartyElemId(2, i));
                pw.print("  ");
                pw.print(String.format("%1$2s", this.heist.getAssaultPartyElemPos(this.heist.getAssaultPartyElemId(2, i))));
                pw.print("  ");
                pw.print(this.heist.getAssaultPartyElemCv(2, i));
                pw.print("   ");
            }
            pw.print("  ");
        }
        for(int i = 1; i<=5; i++){
            pw.print(this.heist.getMuseumRoomPaintings(i));
            pw.print(" ");
            pw.print(this.heist.getMuseumRoomDistance(i));
            pw.print("   ");
        }
        pw.println();
        pw.flush();      
    }
    
    /**
     * This will print the results line
     */
    public void printResults(){
        pw.println("My friends, tonight's effort produced "+this.heist.getTotalPaintings()+" priceless paintings!");
        pw.println();
        pw.flush();
    }
    
}
