package communication.message;

/**
 * This file defines the message types.
 * @author João Brito
 */
public enum MessageType {
    
    /**
     * This message type allows the sender of a previous message to make sure the message was received.
     */
    ACK,
    
    /**
     * Simulates the boolean functionality for the messages (like TRUE return)
     */
    POSITIVE,
    
    /**
     * Simulates the boolean functionality for the messages (like FALSE return)
     */
    NEGATIVE,
    
    /**
     * Alerts that the message is an error message
     */
    ERROR,
    
    /**
     * Alerts the logger that the clients are finishing
     */
    TERMINATE,
    
    /**
     * This message type allows to pass a integer value.
     */
    RESPONSE_INTEGER,
    
    RESPONSE_ARRAY,
    
    START_OPERATIONS,
    
    TAKE_A_REST,
    
    APPRAISE_SIT,
    
    HAND_A_CANVAS,
    
    PREPARE_ASSAULT_PARTY,
    
    WAIT_FOR_PREPARE_EXCURSION,
    
    SUM_UP_RESULTS,
    
    AM_I_NEEDED,
    
    PREPARE_EXCURSION,
    
    SEND_ASSAULT_PARTY,
    
    CRAWL_IN,
    
    CRAWL_OUT,
    
    WAIT_FOR_SEND_ASSAULT_PARTY,
        
    WAIT_FOR_MEMBER,
    
    AT_MUSEUM,
       
    AT_CONCENTRATION,
    
    WAIT_FOR_REVERSE_DIRECTION,
    
    ROLL_A_CANVAS,
    
    INIT_THIEF,
    
    INIT_MUSEUM,
    
    GET_MUSEUM_PAINTINGS,
    
    UPDATE_MUSEUM,
    
    UPDATE_ELEM_CANVAS,
    
    NEW_HEIST,
    
    SET_MASTER_STATE,
    
    INIT_PARTY_ELEM_ID,
    
    SET_PARTY_ACTION,
    
    UPDATE_THIEF_SITUATION,
    
    UPDATE_PARTY_ELEM_CV,
    
    UPDATE_PARTY_ELEM_ID,
    
    SET_PARTY_ELEM_ID,
    
    SET_PARTY1_ROOM_ID,
    
    SET_PARTY_ROOM_ID,
    
    SET_PARTY2_ROOM_ID,
    
    SET_PARTY_MEMBER,
    
    SET_THIEF_STATE,
    
    PRINT_RESULTS,
    
    UPDATE_PARTY_ELEM_POS,
    
    GET_PARTY1_ROOM_ID,
    
    GET_PARTY2_ROOM_ID,
    
    GET_DISTANCE,
    
    GET_PARTY_ELEM_POS,
    
    GET_MAX_DISPLACEMENT,
    
    GET_PARTY_ELEM_ID,
    
}
