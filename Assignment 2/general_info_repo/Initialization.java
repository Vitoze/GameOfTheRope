/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general_info_repo;

/**
 *
 * @author Vítor Barros
 */
public class Initialization {
    
    private final int n_rooms;
    private final int n_thieves;
    private final int thief_min_md;
    private final int thief_max_md;
    private final int n_min_paintings;
    private final int n_max_paintings;
    private final int n_min_distance;
    private final int n_max_distance;
    
    public Initialization(int n_rooms, int n_thieves, int thief_min_md, int thief_max_md, int n_min_paintings, int n_max_paintings, int n_min_distance, int n_max_distance)
    {
        this.n_rooms = n_rooms;
        this.n_thieves = n_thieves;
        this.thief_min_md = thief_min_md;
        this.thief_max_md = thief_max_md;
        this.n_min_paintings = n_min_paintings;
        this.n_max_paintings = n_max_paintings;
        this.n_min_distance = n_min_distance;
        this.n_max_distance = n_max_distance;
    }

    public int getN_rooms() {
        return n_rooms;
    }

    public int getN_thieves() {
        return n_thieves;
    }

    public int getThief_min_md() {
        return thief_min_md;
    }

    public int getThief_max_md() {
        return thief_max_md;
    }

    public int getN_min_paintings() {
        return n_min_paintings;
    }

    public int getN_max_paintings() {
        return n_max_paintings;
    }

    public int getN_min_distance() {
        return n_min_distance;
    }

    public int getN_max_distance() {
        return n_max_distance;
    }    
}
