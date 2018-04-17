package vn.quantda.example.osgi.auction.seller.simple;

import vn.quantda.example.osgi.auction.api.Auction;
import vn.quantda.example.osgi.auction.api.InvalidOfferException;
import vn.quantda.example.osgi.auction.api.Participant;

public class Seller implements Participant, Runnable {

	private final String name;
	private Auction auction;
	
	public Seller(String name) {
		this.name = name;
	}
	@Override
	public void run() {
		try {
			auction.ask("bicyle", 24.0f, this);
		} catch (InvalidOfferException e) {
			e.printStackTrace();
		}
		auction = null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void onAcceptance(Auction auction, String item, float price) {
		System.out.println(this.name + " sold " + item + " for " + price);

	}

	@Override
	public void onRejection(Auction auction, String item, float bestBid) {
		System.out.println("No bidders accepted asked price for " + item
				+ ", best bid was " + bestBid);
	}
	public void ask(Auction auction) {
		this.auction = auction;
		new Thread(this).start();
	}

}
