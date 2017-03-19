/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    private final HashMap<Integer, Room> museum;
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
        this.museum = new HashMap<>();
    }
    
    public static Heist getInstance(){
        if(instance == null){
            instance = new Heist();
        }
        return instance;
    }
   
    public synchronized void setMasterState(MasterState state){
        this.master_state = state;
    }
    
    /**
     * Update the thieves state
     * @param state thief state
     * @param id thief ID
     */
    public synchronized void setThievesState(ThievesState state, int id) {
        if(this.thieves_states.containsKey(id)){
            this.thieves_states.replace(id, state);
        }else{
            this.thieves_states.put(id, state);
        }
    }
    
    public synchronized void setThievesSituation(int id, char s) {
        if(this.thieves_situation.containsKey(id)){
            this.thieves_situation.replace(id, s);
        }else{
            this.thieves_situation.put(id, s);
        }
    }

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

}
