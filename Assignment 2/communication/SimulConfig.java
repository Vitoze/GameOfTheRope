package communication;

/**
 * This file stores the communication so that it is easier to change the values, if needed.
 * @author João Brito
 */
public class SimulConfig {
    
    /**
     * Variable that holds the address for the repository server.
     */
    public final static String logServerName = "127.0.0.1";
    //public final static String logServerName = "l040101-ws01.ua.pt";
    
    /**
     * Variable that holds the port number for the repository server.
     */
    public final static int logServerPort = 22200;
    
    /**
     * Variable that holds the address for the control & collection site server.
     */
    public final static String controlServerName = "127.0.0.1";
    //public final static String controlServerName = "l040101-ws09.ua.pt";
    
    /**
     * Variable that holds the port number for the control & collection site server.
     */
    public final static int controlServerPort = 22201;
    
    /**
     * Variable that holds the address for the concentration site server.
     */
    public final static String concentrationServerName = "127.0.0.1";
    //public final static String concentrationServerName = "l040101-ws03.ua.pt";
    
    /**
     * Variable that holds the port number for the concentration site server.
     */
    public final static int concentrationServerPort = 22202;
    
    /**
     * Variable that holds the address for the assault party #1 server.
     */
    public final static String partyServerName = "127.0.0.1";
    //public final static String partyServerName = "l040101-ws04.ua.pt";
    
    /**
     * Variable that holds the port number for the assault party #1 server.
     */
    public final static int partyServerPort = 22203;
    
    /**
     * Variable that holds the address for the assault party #2 server.
     */
    public final static String party2ServerName = "127.0.0.1";
    //public final static String party2ServerName = "l040101-ws05.ua.pt";
    
    /**
     * Variable that holds the port number for the assault party #2 server.
     */
    public final static int party2ServerPort = 22204;
    
    /**
     * Variable that holds the address for the museum server.
     */
    public final static String museumServerName = "127.0.0.1";
    //public final static String museumServerName = "l040101-ws06.ua.pt";
    
    /**
     * Variable that holds the port number for the museum server.
     */
    public final static int museumServerPort = 22205;
    
    /**
     * Variable that holds the timeout value for the server sockets.
     */
    public final static int socketTimeout = 500;
    
}
