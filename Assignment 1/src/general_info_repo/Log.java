/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general_info_repo;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author João Brito
 */
public class Log {

    private final File log;
    
    private static PrintWriter pw;
    
    private static Log instance = null;
    
    private Log(String filename){
        if(filename.length()==0){
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
            filename = "Heist_" + date.format(today) + ".log";
        }
        this.log = new File(filename);
    }
    
    static{
        instance = new Log("");
    }
    
    public synchronized static Log getInstance(){
        return instance;
    }
}
