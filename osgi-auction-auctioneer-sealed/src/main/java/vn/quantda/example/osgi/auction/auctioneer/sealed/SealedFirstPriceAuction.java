package vn.quantda.example.osgi.auction.auctioneer.sealed;

import java.util.HashMap;
import java.util.Map;

import vn.quantda.example.osgi.auction.api.Auction;
import vn.quantda.example.osgi.auction.api.InvalidOfferException;
import vn.quantda.example.osgi.auction.api.Participant;

public class SealedFirstPriceAuction implements Auction {

	private class Book {
		float ask;
		Participant seller;
		float highestBid;
		Participant highestBidder;
		int numberOfBids;
	}

	private Map<String, Book> openTransactions;
	private final int maxAllowedBids;

	public SealedFirstPriceAuction(int duration) {
		maxAllowedBids = duration;
		openTransactions = new HashMap<>();
	}

	@Override
	public Float ask(String item, float price, Participant seller) throws InvalidOfferException {
		if (price <= 0.0f) {
			throw new InvalidOfferException("Ask must be greater than zero.");
		}

		Book book = openTransactions.get(item); // get transaction for item
		if (book == null) {
			book = new Book();
			openTransactions.put(item, book);
		}
		// Check if item is already being sold
		if (book.seller != null) {
			throw new InvalidOfferException("Item [" + item + "] has already being auctioned.");
		}
		book.ask = price;
		book.seller = seller;
		System.out.println(seller.getName() + " offering item " + item + " for the asking price of " + price);
		return price;
	}

	@Override
	public Float bid(String item, float price, Participant buyer) throws InvalidOfferException {
		if (price <= 0.0f) {
			throw new InvalidOfferException("Bid must be greater than zero.");
		}
		Book book = openTransactions.get(item);
		if (book == null) {
			book = new Book();
			openTransactions.put(item, book);
		}
		assert book.numberOfBids < maxAllowedBids;
		// Check if current bid is highest
		if (price > book.highestBid) {
			book.highestBid = price;
			book.highestBidder = buyer;
		}
		// Check if bids have reached maximum
		if ((++book.numberOfBids) == maxAllowedBids) {
			if (book.seller != null) { // Check if there’s an existing seller
				if (book.highestBid >= book.ask) {
					book.seller.onAcceptance(this, item, book.highestBid); // Notify seller and buyer of acceptance
					book.highestBidder.onAcceptance(this, item, book.highestBid);
				} else {
					book.seller.onRejection(this, item, book.highestBid);
					book.highestBidder.onRejection(this, item, book.highestBid);
				}
			} else {
				book.highestBidder.onRejection(this, item, book.highestBid);
			}
			openTransactions.remove(item);
		} else {
			System.out.println(buyer.getName() + " bidding for item " + item);
		}
		return null; // Conceal bid price
	}

}
