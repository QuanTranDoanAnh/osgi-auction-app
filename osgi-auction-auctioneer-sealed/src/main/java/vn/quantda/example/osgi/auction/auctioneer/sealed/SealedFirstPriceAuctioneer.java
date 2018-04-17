package vn.quantda.example.osgi.auction.auctioneer.sealed;

import java.util.Dictionary;
import java.util.Hashtable;

import vn.quantda.example.osgi.auction.api.Auction;
import vn.quantda.example.osgi.auction.spi.Auctioneer;

public class SealedFirstPriceAuctioneer implements Auctioneer {

	private static final String SEALED_FIRST_PRICE = "Sealed-First-Price";
	private final int DURATION = 3;
	
	private final Dictionary<String, Object> properties = new Hashtable<>();
	private Auction auction;
	
	public SealedFirstPriceAuctioneer() {
		properties.put(Auction.TYPE, SEALED_FIRST_PRICE);
		properties.put(Auction.DURATION, DURATION);
		auction = new SealedFirstPriceAuction(DURATION);
	}
	
	@Override
	public Auction getAuction() {
		return this.auction;
	}

	@Override
	public Dictionary<String, Object> getAuctionProperties() {
		return this.properties;
	}

}
