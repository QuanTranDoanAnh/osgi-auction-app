package vn.quantda.example.osgi.auction.spi;

import java.util.Dictionary;

import vn.quantda.example.osgi.auction.api.Auction;

public interface Auctioneer {

	Auction getAuction();
	
	Dictionary<String, Object> getAuctionProperties();
}
