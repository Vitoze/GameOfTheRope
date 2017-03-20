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
    private final HashMap<Integer, Integer> assault_party1_id;
    private final HashMap<Integer, Integer> assault_party2_id;
    private final HashMap<Integer, Integer> assault_party1_pos;
    private final HashMap<Integer, Integer> assault_party2_pos;
    private final HashMap<Integer, Integer> assault_party1_cv;
    private final HashMap<Integer, Integer> assault_party2_cv;
    private final HashMap<Integer, Integer> museum_rooms_distance;
    private int assault_party1_Rid;
    private int assault_party2_Rid;
    private MasterState master_state;
    
    private static Heist instance = null;
     
    private Heist(){
        this.thieves_maxDisplacement = new HashMap<>();
        this.thieves_states = new HashMap<>();
        this.thieves_situation = new HashMap<>();
        this.assault_party1_id = new HashMap<>();
        this.assault_party2_id = new HashMap<>();
        this.assault_party1_pos = new HashMap<>();
        this.assault_party2_pos = new HashMap<>();
        this.assault_party1_cv = new HashMap<>();
        this.assault_party2_cv = new HashMap<>();
        this.assault_party1_Rid = 0;
        this.assault_party2_Rid = 0;
        this.museum_rooms_distance = new HashMap<>();
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
    
    public synchronized int getAssaultParty2Rid() {
        return this.assault_party2_Rid;
    }

    public synchronized int getAssaultParty1ElemId(int i) {
        return this.assault_party1_id.get(i);
    }
    
    public synchronized int getAssaultParty2ElemId(int i) {
        return this.assault_party2_id.get(i);
    }

    public synchronized int getAssaultParty1ElemPos(int i) {
        return this.assault_party1_pos.get(i);
    }
    
    public synchronized int getAssaultParty2ElemPos(int i) {
        return this.assault_party2_pos.get(i);
    }

    public synchronized int getAssaultParty1ElemCv(int i) {
        return this.assault_party1_cv.get(i);
    }
    
    public synchronized int getAssaultParty2ElemCv(int i) {
        return this.assault_party2_cv.get(i);
    }
    
    /*
    public synchronized int getRoomPaintings(int i) {
        return this.museum.get(i).getPaintings(i);
    }
    
    public synchronized int getRoomDistance(int i) {
        return this.museum.get(i).getDistance(i);
    }

    public synchronized void setMuseumRooms(Room[] museum_rooms) {
        for(int i = 0; i < N_ROOMS; i++){
            int id = museum_rooms[i].getID();
            if(this.museum.containsKey(id)){
                this.museum.replace(id, museum_rooms[i]);
            }else{
                this.museum.put(id, museum_rooms[i]);
            }
        }
    }
    */

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
}
