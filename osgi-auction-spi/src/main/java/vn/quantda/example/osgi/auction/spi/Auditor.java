package vn.quantda.example.osgi.auction.spi;

import vn.quantda.example.osgi.auction.api.Participant;

public interface Auditor {

	void onAcceptance(Auctioneer auctioneer, Participant participant, String item, float ask, float acceptedBid, Float[] bids);
	
	void onRejection(Auctioneer auctioneer, Participant participant, String item, float ask, Float[] rejectedBids);
}
