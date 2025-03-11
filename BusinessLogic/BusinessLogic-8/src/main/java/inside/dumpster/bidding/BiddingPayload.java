/*
 *
 */
package inside.dumpster.bidding;

import inside.dumpster.bidding.bid.Bidder;
import inside.dumpster.client.Payload;
import inside.dumpster.client.impl.Helper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jsnor
 */
public class BiddingPayload extends Payload {
    public List<Bidder> getBidders() {
        final int numOfBidders = Helper.fixedHash(this.getSrcBytes(), 10, 30);
        List<Bidder> bidders = new ArrayList(numOfBidders);
        for (int i = 1 ; i <= numOfBidders; i++) {
            bidders.add(new Bidder(this.getTransactionId(), i));
        }
        return bidders;
    }
}
