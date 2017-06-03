package structures.constants;

import java.util.Properties;

/**
 * This class contains all the constants related with the Registry.
 * @author João Brito
 */
public class RegistryConfig {
    /**
     * Repository name entry on the registry.
     */
    public static String repNameEntry = "RepositoryInt";

    /**
     * Control name entry on the registry.
     */
    public static String controlNameEntry = "ControlInt";

    /**
     * Concentration name entry on the registry.
     */
    public static String concentationNameEntry = "ConcentrationInt";
    
    /**
     * Concentration name entry on the registry.
     */
    public static String partyNameEntry = "PartyInt";
    
    /**
     * Concentration name entry on the registry.
     */
    public static String party2NameEntry = "Party2Int";

    /**
     * Museum name entry on the registry.
     */
    public static String museumNameEntry = "MuseumInt";

    /**
     * RegisterHandler name entry on the registry.
     */
    public static String registerHandler = "RegisterHandler";
    /**
     * Bash property of the file.
     */
    private Properties prop;
    /**
     * Constructor that receives the file with the configurations.
     */
    public RegistryConfig() {
    }

    /** 
     * Loads the parameter REGISTER_HOST from the configuration file.
     * @return parameter value
     */
    public String registryHost() {
        return "l040101-ws01.ua.pt";
    }
    /** 
     * Loads the parameter REGISTER_PORT from the configuration file.
     * @return parameter value
     */
    public int registryPort() {
        return 22200;
    }
    /** 
     * Loads the parameter REGISTER_OBJECT_PORT from the configuration file.
     * @return parameter value
     */
    public int objectPort() {
        return 22201;
    }
    /** 
     * Loads the parameter REPOSITORY_PORT from the configuration file.
     * @return parameter value
     */
    public int repositoryPort() {
        return 22200;
    }
    /** 
     * Loads the parameter CONTROL_PORT from the configuration file.
     * @return parameter value
     */
    public int controlPort() {
        return 22200;
    }
    /** 
     * Loads the parameter CONCENTRATION_PORT from the configuration file.
     * @return parameter value
     */
    public int concentrationPort() {
        return 22200;
    }
    /** 
     * Loads the parameter PARTY1_PORT from the configuration file.
     * @return parameter value
     */
    public int partyPort() {
        return 22200;
    }
    /** 
     * Loads the parameter PARTY2_PORT from the configuration file.
     * @return parameter value
     */
    public int party2Port() {
        return 22200;
    }
    /** 
     * Loads the parameter MUSEUM_PORT from the configuration file.
     * @return parameter value
     */
    public int museumPort() {
        return 22200;
    }
}
