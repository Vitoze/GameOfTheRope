/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museum;

/**
 *
 * @author João Brito
 */
public class Room {
    private int id;
    private int dt;
    private int np;
    
    public int getDistance(int id){
        return dt;
    }
    
    public void setDistance(int id, int dt){
        this.dt=dt;
    }
    
    public int getPaintings(int id){
        return np;
    }
    
    public void setPaintings(int id, int np){
        this.np=np;
    }
}
