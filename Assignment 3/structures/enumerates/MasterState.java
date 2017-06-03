package structures.enumerates;

/**
 * States for the Master
 * @author João Brito, 68137
 */
public enum MasterState {
    /**
     * PLANNING_THE_HEIST = PLTH
     */
    PLANNING_THE_HEIST{
        @Override
        public String toString(){
            return "PLTH";
        }
    },
    
    /**
     * DECIDING_WHAT_TO_DO = DWTD
     */
    DECIDING_WHAT_TO_DO{
        @Override
        public String toString(){
            return "DWTD";
        }
    },
    
    /**
     * ASSEMBLING_A_GROUP = ASAG
     */
    ASSEMBLING_A_GROUP{
        @Override
        public String toString(){
            return "ASAG";
        }
    },
    
    /**
     * WAITING_FOR_GROUP_ARRIVAL = WFGA
     */
    WAITING_FOR_GROUP_ARRIVAL{
        @Override
        public String toString(){
            return "WFGA";
        }
    },
    
    /**
     * PRESENTING_THE_REPORT = PRTR
     */
    PRESENTING_THE_REPORT{
        @Override
        public String toString(){
            return "PRTR";
        }
    }
}
