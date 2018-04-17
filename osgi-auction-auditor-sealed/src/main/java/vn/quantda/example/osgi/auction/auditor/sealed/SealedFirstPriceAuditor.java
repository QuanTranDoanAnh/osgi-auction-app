package vn.quantda.example.osgi.auction.auditor.sealed;

import vn.quantda.example.osgi.auction.api.Auction;
import vn.quantda.example.osgi.auction.api.Participant;
import vn.quantda.example.osgi.auction.spi.Auctioneer;
import vn.quantda.example.osgi.auction.spi.Auditor;

public class SealedFirstPriceAuditor implements Auditor {

	/**
	 * Auction transaction accepted callback
	 */
	@Override
	public void onAcceptance(Auctioneer auctioneer, Participant participant, String item, float ask, float acceptedBid,
			Float[] bids) {
		verify(auctioneer, participant, bids);
	}

	/**
	 * Auction transaction rejected callback
	 */
	@Override
	public void onRejection(Auctioneer auctioneer, Participant participant, String item, float ask,
			Float[] rejectedBids) {
		verify(auctioneer, participant, rejectedBids);
	}

	private void verify(Auctioneer auctioneer, Participant participant, Float[] bids) {
		if ("Sealed-First-Price".equals( // Checks type of auction
				auctioneer.getAuctionProperties().get(Auction.TYPE))) {
			for (int i = 0; i < bids.length - 1; i++) {
				// Checks if consecutive bids are close together
				if ((bids[i + 1] - bids[i]) <= 1.0) {
					System.out.println("Warning to '" + participant.getName() + "': bids (" + bids[i] + ", "
							+ bids[i + 1] + ") are too close together, possible disclosure may have happened");
				}
			}
		}
	}
}
