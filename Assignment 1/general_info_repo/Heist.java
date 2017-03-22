/*
 * Distributed Systems
 */
package general_info_repo;

import entities.MasterState;
import entities.ThievesState;
import java.util.HashMap;
import museum.Room;

/**
 *  This heist singleton will have assaults, thieves displacements, states.
 *  @author João Brito
 */
public class Heist {
    public static final int N_THIEVES = 6;
    public static final int N_ROOMS = 5;
    
    private final HashMap<Integer, Integer> thieves_maxDisplacement;
    private final HashMap<Integer, ThievesState> thieves_states;
    private final HashMap<Integer, Character> thieves_situation;
    private final int[] assault_party1_id;
    private final int[] assault_party2_id;
    private final HashMap<Integer, Integer> assault_party1_pos;
    private final HashMap<Integer, Integer> assault_party2_pos;
    private final HashMap<Integer, Integer> assault_party1_cv;
    private final HashMap<Integer, Integer> assault_party2_cv;
    private final HashMap<Integer, Integer> museum_rooms_distance;
    private final HashMap<Integer, Integer> museum_rooms_paintings;
    private int assault_party1_Rid;
    private int assault_party2_Rid;
    private int nElemParty1;
    private int nElemParty2;
    private MasterState master_state;
    
    private static Heist instance = null;
     
    private Heist(){
        this.thieves_maxDisplacement = new HashMap<>();
        this.thieves_states = new HashMap<>();
        this.thieves_situation = new HashMap<>();
        this.assault_party1_id = new int[3];
        this.assault_party2_id = new int[3];
        this.assault_party1_pos = new HashMap<>();
        this.assault_party2_pos = new HashMap<>();
        this.assault_party1_cv = new HashMap<>();
        this.assault_party2_cv = new HashMap<>();
        this.assault_party1_Rid = 0;
        this.assault_party2_Rid = 0;
        this.nElemParty1 = 0;
        this.nElemParty2 = 0;
        this.museum_rooms_distance = new HashMap<>();
        this.museum_rooms_paintings = new HashMap<>();
    }
    
    /**
     * The heist is a singleton.
     * @return Heist instance, is a singleton.
     */
    public static Heist getInstance(){
        if(instance == null){
            instance = new Heist();
        }
        return instance;
    }
 
    /**
     * Set master state.
     * @param state master state.
     */
    public synchronized void setMasterState(MasterState state){
        this.master_state = state;
    }
    
    /**
     * Set the thieves state.
     * @param id thief ID.
     * @param state thief state.
     */
    public synchronized void setThievesState(int id, ThievesState state) {
        if(this.thieves_states.containsKey(id)){
            this.thieves_states.replace(id, state);
        }else{
            this.thieves_states.put(id, state);
        }
    }
    
    /**
     * Set the thieves situation.
     * @param id thief ID.
     * @param s thief situation 'W' or 'P'
     */   
    public synchronized void setThievesSituation(int id, char s) {
        if(this.thieves_situation.containsKey(id)){
            this.thieves_situation.replace(id, s);
        }else{
            this.thieves_situation.put(id, s);
        }
    }
    
    /**
     * Set thieves maximum displacement.
     * @param id thief ID.
     * @param md thief max displacement.
     */
    public synchronized void setThievesMaxDisplacement(int id, int md) {
        if(this.thieves_maxDisplacement.containsKey(id)){
            this.thieves_maxDisplacement.replace(id, md);
        }else{
            this.thieves_maxDisplacement.put(id, md);
        }
    }

    public synchronized MasterState getMasterState() {
        return master_state;
    }

    public synchronized ThievesState getThiefState(int id) {
       return thieves_states.get(id);
    }

    public synchronized char getThiefSituation(int id) {
        return thieves_situation.get(id);
    }

    public synchronized int getThiefMaxDisplacement(int id) {
        return thieves_maxDisplacement.get(id);
    }

    public synchronized int getAssaultParty1Rid() {
        return this.assault_party1_Rid;
    }
    
    public synchronized void setAssaultParty1Rid(int rid) {
        this.assault_party1_Rid=rid;
    }
    
    public synchronized int getAssaultParty2Rid() {
        return this.assault_party2_Rid;
    }
    
    public synchronized void setAssaultParty2Rid(int rid) {
        this.assault_party2_Rid=rid;
    }

    public synchronized int getAssaultParty1ElemId(int i) {
        return this.assault_party1_id[i-1];
    }
    
    public synchronized void setAssaultParty1ElemId(int id, int nElemParty){
        this.assault_party1_id[nElemParty] = id;
    }
    
    public synchronized int getAssaultParty2ElemId(int i) {
        return this.assault_party2_id[i-1];
    }
    
    public synchronized void setAssaultParty2ElemId(int id, int nElemParty){
        this.assault_party2_id[nElemParty] = id;
    }

    public synchronized int getAssaultParty1ElemPos(int i) {
        return this.assault_party1_pos.get(this.assault_party1_id[i-1]);
    }
    
    public synchronized void setAssaultParty1ElemPos(int id, int pos) {
        if(this.assault_party1_pos.containsKey(id)){
            this.assault_party1_pos.replace(id, pos);
        }else{
            this.assault_party1_pos.put(id, pos);
        }
    }
    
    public synchronized int getAssaultParty2ElemPos(int i) {
        return this.assault_party2_pos.get(this.assault_party2_id[i-1]);
    }
    
    public synchronized void setAssaultParty2ElemPos(int id, int pos) {
        if(this.assault_party2_pos.containsKey(id)){
            this.assault_party2_pos.replace(id, pos);
        }else{
            this.assault_party2_pos.put(id, pos);
        }
    }

    public synchronized int getAssaultParty1ElemCv(int i) {
        return this.assault_party1_cv.get(this.assault_party1_id[i-1]);
    }
    
    public synchronized void setAssaultParty1ElemCv(int id, int cv) {
        if(this.assault_party1_cv.containsKey(id)){
            this.assault_party1_cv.replace(id, cv);
        }else{
            this.assault_party1_cv.put(id, cv);
        }
    }
    
    public synchronized int getAssaultParty2ElemCv(int i) {
        return this.assault_party2_cv.get(this.assault_party2_id[i-1]);
    }
    
    public synchronized void setAssaultParty2ElemCv(int id, int cv) {
        if(this.assault_party2_cv.containsKey(id)){
            this.assault_party2_cv.replace(id, cv);
        }else{
            this.assault_party2_cv.put(id, cv);
        }
    }

    public synchronized void setMuseumRoomsDistance(int id, int dt) {
        if(this.museum_rooms_distance.containsKey(id)){
            this.museum_rooms_distance.replace(id, dt);
        }else{
            this.museum_rooms_distance.put(id, dt);
        }
    }
    
    public synchronized int getMuseumRoomDistance(int id){
        return this.museum_rooms_distance.get(id);
    }
    
    public synchronized void setMuseumRoomsPaintings(int id, int np) {
        if(this.museum_rooms_paintings.containsKey(id)){
            this.museum_rooms_paintings.replace(id, np);
        }else{
            this.museum_rooms_paintings.put(id, np);
        }
    }
    
    public synchronized int getMuseumRoomPaintings(int id){
        return this.museum_rooms_paintings.get(id);
    }
}
