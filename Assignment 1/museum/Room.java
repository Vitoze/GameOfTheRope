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
    
    public Room(int id, int dt, int np){
        this.id = id;
        this.dt = dt;
        this.np = np;
    }
    
    public int getID(){
        return this.id;
    }
    
    public int getDistance(int id){
        return this.dt;
    }
    
    public void setDistance(int id, int dt){
        this.dt=dt;
    }
    
    public int getPaintings(int id){
        return this.np;
    }
    
    public void setPaintings(int id, int np){
        this.np=np;
    }
}
