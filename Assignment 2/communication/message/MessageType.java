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
     * Simulates the boolean functionality for the messages (like TRUE return).
     */
    POSITIVE,
    
    /**
     * Simulates the boolean functionality for the messages (like FALSE return).
     */
    NEGATIVE,
    
    /**
     * Alerts that the message is an error message.
     */
    ERROR,
    
    /**
     * Alerts the logger that the clients are finishing.
     */
    TERMINATE,
    
    /**
     * This message type allows to pass an integer value.
     */
    RESPONSE_INTEGER,
    
    /**
     * This message type allows to pass an arrays of values.
     */
    RESPONSE_ARRAY,
    
    /**
     * Master start operations.
     */
    START_OPERATIONS,
    
    /**
     * Master, take a rest.
     */
    TAKE_A_REST,
    
    /**
     * Master, appraise sit.
     */
    APPRAISE_SIT,
    
    /**
     * Thieves, hand a canvas.
     */
    HAND_A_CANVAS,
    
    /**
     * Master, prepare assault party.
     */
    PREPARE_ASSAULT_PARTY,
    
    /**
     * Master, wait for prepare excursion.
     */
    WAIT_FOR_PREPARE_EXCURSION,
    
    /**
     * Master, sum up results.
     */
    SUM_UP_RESULTS,
    
    /**
     * Thief, am i needed.
     */
    AM_I_NEEDED,
    
    /**
     * Thief, prepare excursion.
     */
    PREPARE_EXCURSION,
    
    /**
     * Master, send assault party.
     */
    SEND_ASSAULT_PARTY,
    
    /**
     * Thief, crawl in.
     */
    CRAWL_IN,
    
    /**
     * Thief, crawl out.
     */
    CRAWL_OUT,
    
    /**
     * Thief, wait for send assault party.
     */
    WAIT_FOR_SEND_ASSAULT_PARTY,
        
    /**
     * Thief, wait for member.
     */
    WAIT_FOR_MEMBER,
    
    /**
     * Thief, at museum.
     */
    AT_MUSEUM,
       
    /**
     * Thief, at concentration.
     */
    AT_CONCENTRATION,
    
    /**
     * Thief, wait for reverse direction.
     */
    WAIT_FOR_REVERSE_DIRECTION,
    
    /**
     * Thief, roll a canvas.
     */
    ROLL_A_CANVAS,
    
    /**
     * Thief, inits thief in log.
     */
    INIT_THIEF,
    
    /**
     * Museum, init museum.
     */
    INIT_MUSEUM,
    
    /**
     * Log, get museum paintings.
     */
    GET_MUSEUM_PAINTINGS,
    
    /**
     * Log, update museum.
     */
    UPDATE_MUSEUM,
    
    /**
     * Log, update element canvas.
     */
    UPDATE_ELEM_CANVAS,
    
    /**
     * Log, new heist.
     */
    NEW_HEIST,
    
    /**
     * Log, set master state.
     */
    SET_MASTER_STATE,
    
    /**
     * Log, init party element id.
     */
    INIT_PARTY_ELEM_ID,
    
    /**
     * Log, set party action.
     */
    SET_PARTY_ACTION,
    
    /**
     * Log, update thief situation.
     */
    UPDATE_THIEF_SITUATION,
    
    /**
     * Log, update party element canvas.
     */
    UPDATE_PARTY_ELEM_CV,
    
    /**
     * Log, update party element identification.
     */
    UPDATE_PARTY_ELEM_ID,
    
    /**
     * Log, set party element identification.
     */
    SET_PARTY_ELEM_ID,
    
    /**
     * Log, set party1 room id.
     */
    SET_PARTY1_ROOM_ID,
    
    /**
     * Log, set party room id.
     */
    SET_PARTY_ROOM_ID,
    
    /**
     * Log, set party2 room id.
     */
    SET_PARTY2_ROOM_ID,
    
    /**
     * Log, set party member.
     */
    SET_PARTY_MEMBER,
    
    /**
     * Log, set thief state.
     */
    SET_THIEF_STATE,
    
    /**
     * Log, print results.
     */
    PRINT_RESULTS,
    
    /**
     * Log, update party element position.
     */
    UPDATE_PARTY_ELEM_POS,
    
    /**
     * Log, get party 1 room id.
     */
    GET_PARTY1_ROOM_ID,
    
    /**
     * Log, get party 2 room id.
     */
    GET_PARTY2_ROOM_ID,
    
    /**
     * Log, get distance.
     */
    GET_DISTANCE,
    
    /**
     * Log, get party element position.
     */
    GET_PARTY_ELEM_POS,
    
    /**
     * Log, get max displacement.
     */
    GET_MAX_DISPLACEMENT,
    
    /**
     * Log, get party elem id.
     */
    GET_PARTY_ELEM_ID,
    
}
