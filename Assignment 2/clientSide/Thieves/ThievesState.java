/*
 * Distributed Systems
 */
package clientSide.Thieves;

/**
 * States for the thieves.
 * @author João Brito, 68137
 */
public enum ThievesState {
    /**
     * OUTSIDE = OUTS
     */
    OUTSIDE{
        @Override
        public String toString(){
            return "OUTS";
        }
    },
    
    /**
     * CRAWLING_OUTWARDS = CROT
     */
    CRAWLING_OUTWARDS{
        @Override
        public String toString(){
            return "CROT";
        }
    },
    
    /**
     * AT_A_ROOM = ATAR
     */
    AT_A_ROOM{
        @Override
        public String toString(){
            return "ATAR";
        }
    },
    
    /**
     * CRAWLING_INWARDS = CRIN
     */
    CRAWLING_INWARDS{
        @Override
        public String toString(){
            return "CRIN";
        }
    }
}
