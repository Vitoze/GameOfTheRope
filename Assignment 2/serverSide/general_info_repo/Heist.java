/*
 * Distributed Systems
 */
package serverSide.general_info_repo;

import java.util.HashMap;
import clientSide.Master.MasterState;
import clientSide.Thieves.ThievesState;

/**
 *  This heist singleton will have assaults, thieves displacements, states.
 *  @author João Brito, 68137
 */
public class Heist{
    private int totalPaintings;
    private int assault_party1_Rid;
    private int assault_party2_Rid;
    private MasterState master_state;
    private final HashMap<Integer, Integer> assault_party_cv;
    private final HashMap<Integer, Integer> assault_party_pos;
    private final HashMap<Integer, ThievesState> thieves_states;
    private final HashMap<Integer, Character> thieves_situation;
    private final HashMap<Integer, Integer> museum_rooms_distance;
    private final HashMap<Integer, Integer> museum_rooms_paintings;
    private final HashMap<Integer, Integer> thieves_maxDisplacement;
    private final HashMap<Integer, Integer> assault_party_elem_number;
    private final HashMap<Integer, HashMap<Integer,Integer>> assault_party;
    
    private static Heist instance = null;
     
    private Heist(){
        this.totalPaintings = 0;
        this.assault_party1_Rid = 0;
        this.assault_party2_Rid = 0;
        this.assault_party = new HashMap<>();
        this.thieves_states = new HashMap<>();
        this.assault_party_cv = new HashMap<>();
        this.thieves_situation = new HashMap<>();
        this.assault_party_pos = new HashMap<>();
        this.museum_rooms_distance = new HashMap<>();
        this.museum_rooms_paintings = new HashMap<>();
        this.thieves_maxDisplacement = new HashMap<>();
        this.assault_party_elem_number = new HashMap<>();
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
    
    /**
     * This method will return the Master state.
     * @return Master state.
     */
    public synchronized MasterState getMasterState() {
        return master_state;
    }
    
    /**
     * This method will return the Thief state.
     * @param id Thied identification.
     * @return Thief state.
     */
    public synchronized ThievesState getThiefState(int id) {
       return thieves_states.get(id);
    }
    
    /**
     * This method will return the Thief situation.
     * @param id Thief identification.
     * @return Thief situation.
     */
    public synchronized char getThiefSituation(int id) {
        return thieves_situation.get(id);
    }
    
    /**
     * This method will return the thief max displacement.
     * @param id thief identification.
     * @return thief md.
     */
    public synchronized int getThiefMaxDisplacement(int id) {
        return thieves_maxDisplacement.get(id);
    }
    
    /**
     * This method will return the assault party#1 objective.
     * @return museum room number.
     */
    public synchronized int getAssaultParty1Rid() {
        return this.assault_party1_Rid;
    }
    
    /**
     * This method will set the assault party#1 objective.
     * @param rid museum room number.
     */
    public synchronized void setAssaultParty1Rid(int rid) {
        this.assault_party1_Rid=rid;
    }
    
    /**
     * This method will return the assault party#2 objective.
     * @return museum room number.
     */
    public synchronized int getAssaultParty2Rid() {
        return this.assault_party2_Rid;
    }
    
    /**
     * This method will set the assault party#2 objective.
     * @param rid museum room number.
     */
    public synchronized void setAssaultParty2Rid(int rid) {
        this.assault_party2_Rid=rid;
    }
    
    /**
     * This method will return the total of paintings in the museum.
     * @return total number of paintings.
     */
    public synchronized int getTotalPaintings(){
        return this.totalPaintings;
    }

    /**
     * This method will set the assault party members.
     * @param party assault party number.
     * @param i assault party element number.
     * @param id thief identification.
     */
    public synchronized void setAssaultPartyElemId(int party, int i, int id){
        if(!this.assault_party.containsKey(party)){
            this.assault_party.put(party, new HashMap<>());
        }
        if(this.assault_party.get(party).containsKey(i)){
            this.assault_party.get(party).replace(i, id);
        }else{
            this.assault_party.get(party).put(i, id);
        }
    }
    
    public synchronized void initAssaultPartyElemId(){
        for(int i=1; i<=3;i++){
            if(!this.assault_party.containsKey(1)){
                this.assault_party.put(1, new HashMap<>());
            }
            if(!this.assault_party.containsKey(2)){
                this.assault_party.put(2, new HashMap<>());
            }
            if(this.assault_party.get(1).containsKey(i)){
                this.assault_party.get(1).replace(i, 0);
            }else{
                this.assault_party.get(1).put(i, 0);
            }
            if(this.assault_party.get(2).containsKey(i)){
                this.assault_party.get(2).replace(i, 0);
            }else{
                this.assault_party.get(2).put(i, 0);
            }
        }
    }
    
    /**
     * This method will set the assault party element number.
     * @param id thief identification.
     * @param i assault party element number.
     */
    public synchronized void setAssaultPartyElemNumber(int id, int i){
        if(this.assault_party_elem_number.containsKey(id)){
            this.assault_party_elem_number.replace(id, i);
        }else{
            this.assault_party_elem_number.put(id, i);
        }
    }
    
    /**
     * This method will set the assault party element has canvas
     * @param id thief id.
     * @param cv thief canvas.
     */
    public synchronized void setAssaultPartyElemCv(int id, int cv){
        if(this.assault_party_cv.containsKey(id)){
            this.assault_party_cv.replace(id, cv);
        }else{
            this.assault_party_cv.put(id,cv);
        }
    }
    
    /**
     * This method will return the assault party element number.
     * @param id thief id.
     * @return assault party element number.
     */
    public synchronized int getAssaultPartyElemNumber(int id){
        return this.assault_party_elem_number.get(id);
    }
    
    /**
     * This method will return the assault party thief identification.
     * @param party assault party number.
     * @param i assault party member number.
     * @return thief identification.
     */
    public synchronized int getAssaultPartyElemId(int party, int i) {
        return this.assault_party.get(party).get(i);
    }
    
    /** 
     * This method will set the assault party element position.
     * @param id thief id.
     * @param pos thief position.
     */
    public synchronized void setAssaultPartyElemPos(int id, int pos){
        if(this.assault_party_pos.containsKey(id)){
            this.assault_party_pos.replace(id, pos);
        }else{
            this.assault_party_pos.put(id, pos);
        }
    }
    
    /**
     * This method will return the assault party element number.
     * @param id thief id.
     * @return thief position.
     */
    public synchronized int getAssaultPartyElemPos(int id) {
        return this.assault_party_pos.get(id);
    }
    
    /**
     * This method will return if the assault party member has canvas.
     * @param party assault party number.
     * @param i assault party element number.
     * @return thief has canvas.
     */
    public synchronized int getAssaultPartyElemCv(int party, int i){
       return this.assault_party_cv.get(this.assault_party.get(party).get(i));
    }
    
    /**
     * This method will set the museum room distance.
     * @param id room number.
     * @param dt room distance.
     */
    public synchronized void setMuseumRoomsDistance(int id, int dt) {
        if(this.museum_rooms_distance.containsKey(id)){
            this.museum_rooms_distance.replace(id, dt);
        }else{
            this.museum_rooms_distance.put(id, dt);
        }
    }
    
    /**
     * This method will return the room distance.
     * @param id room number.
     * @return room distance.
     */
    public synchronized int getMuseumRoomDistance(int id){
        return this.museum_rooms_distance.get(id);
    }
    
    /**
     * This method will set the museum room paintings.
     * @param id room number.
     * @param np room paintings number.
     */
    public synchronized void setMuseumRoomsPaintings(int id, int np) {
        if(this.museum_rooms_paintings.containsKey(id)){
            this.museum_rooms_paintings.replace(id, np);
        }else{
            this.museum_rooms_paintings.put(id, np);
            this.totalPaintings+=np;
        }
    }
    
    /**
     * This method will return the museum room paintings.
     * @param id room number.
     * @return paintings number.
     */
    public synchronized int getMuseumRoomPaintings(int id){
        return this.museum_rooms_paintings.get(id);
    }
}