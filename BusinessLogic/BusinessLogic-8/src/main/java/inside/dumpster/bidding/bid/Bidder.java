/*
 */
package inside.dumpster.bidding.bid;

/**
 *
 * @author jsnor
 */
public class Bidder {
    public final int id;
    public final String transactionId;
    

    public Bidder(String transactionId, int id) {
        this.id = id;
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", transactionId, id);
    }
            
}
