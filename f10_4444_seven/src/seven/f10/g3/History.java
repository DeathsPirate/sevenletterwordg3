package seven.f10.g3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import org.apache.log4j.Logger;
import seven.ui.Letter;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class History {

	private ArrayList<BidLog> bidLogList;
	private final int[] associatedValue = { 7, 2, 3, 4, 7, 1, 3, 3, 7, 0, 1, 5,
			3, 6, 6, 3, 0, 7, 7, 5, 4, 1, 1, 0, 2, 0 };
	private int[] totalLetters = { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6,
			8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };
	private ArrayList[] marketValue;
	private ArrayList<Integer> allBids;
	protected Logger l = Logger.getLogger(this.getClass());
	private int[] lettersUsed;
	private int numberOfPlayers = 0, numHidden = 0, numBidRoundsPlayed,
			totalLettersInBag = 98;
	private final double H = 0.8;
	private final double lStrategy = 1.000 * 10 / 26;
	int np = 0;

	public int getAmountBidOnRound() {
		return amountBidOnRound;
	}

	public void setAmountBidOnRound(int amountBidOnRound) {
		this.amountBidOnRound = amountBidOnRound;
	}

	private int amountBidOnRound = 0;

	public History() {
		bidLogList = new ArrayList<BidLog>();
		marketValue = new ArrayList[26];
		allBids = new ArrayList();
		lettersUsed = new int[26];
	}

	public int adjust(double bidStrategy, Letter bidLetter,
			ArrayList<PlayerBids> cachedBids, int ourID) {
		// strategy
		double strength = bidStrategy * H;

		double bid = 0;
		int bidLetterIndex = bidLetter.getAlphabet() - 'A';

		if (bidStrategy == -1) { // First round, no hidden letters
			l.warn("First round, no hidden letters");
			return (associatedValue[bidLetterIndex]);
		}

		else if (cachedBids.size() == 0) {
			l.warn("First round, hidden letters");
			return (boost(associatedValue[bidLetterIndex], bidStrategy));
		}

		// round other than first round
		// Setup - number of players, add old bids
		int np = cachedBids.get(0).getBidvalues().size();
		PlayerBids lastBids = cachedBids.get(cachedBids.size() - 1);
		for (int i = 0; i < lastBids.getBidvalues().size(); i++)
			allBids.add(lastBids.getBidvalues().get(i));
		Letter lastLetter = lastBids.getTargetLetter();
		int lastLetterIndex = lastLetter.getAlphabet() - 'A';

		// update market value
		if (marketValue[lastLetterIndex] == null)
			marketValue[lastLetterIndex] = new ArrayList<Integer>();
		ListIterator<Integer> it = lastBids.getBidvalues().listIterator();
		while (it.hasNext())
			marketValue[lastLetterIndex].add(it.next());

		// update bidTimes
		lettersUsed[lastLetterIndex]++;

		double overallAffect = .33;
		int indexm = -1;
		double bidPortionFromMarketValue = 0;
		if (marketValue[bidLetterIndex] == null)// no market value
		{
			bidPortionFromMarketValue = associatedValue[bidLetterIndex]
					* strength;
		} else {
			indexm = (int) (Math.round(strength
					* marketValue[bidLetterIndex].size()));
			if (indexm == marketValue[bidLetterIndex].size())
				indexm--;
			Collections.sort(marketValue[bidLetterIndex]);
			bidPortionFromMarketValue = (1 - overallAffect)
					* (Integer) (marketValue[bidLetterIndex].get(indexm));
		}

		int indexa = (int) (Math.round(strength * allBids.size()));
		if (indexa == allBids.size())
			indexa--;
		Collections.sort(allBids);
		double bidPortionFromOverallBids = overallAffect * allBids.get(indexa);
		
		bid = bidPortionFromOverallBids + bidPortionFromMarketValue;
		
		return (boost(bid, bidStrategy));
	}

	/**
	 * Boost/ reduce bid based on the number of letters left in round, number of
	 * players in game, how high we are bidding, and if it gets us a seven
	 * letter word
	 */
	public int boost(double bid, double bidStrategy){
		
		String str = "Before bid was: " + bid;
		
		//Adjust depending on the number of letters left in the bag
		double t = (totalLettersSeenInEntireRound() - np*getNumHidden() -numBidRoundsPlayed);
		double b = 2*(1-t/totalLettersInBag);
		bid = b * bid;
		
		str += ", and now it is: " + bid;
		
		//Dont bid too low in a small game
		if (np<=4)
		{
			if (bidStrategy<lStrategy){// make sure that statistics are not skewed
				l.warn("skewed");
				bid=.8*bid;}
			if (bid<1)
				bid=1;
		}		
		
		l.warn(str + " and now it is: " + (int)(bid + .5));
		
		return((int)(bid + .5));
	}

	/**
	 * Returns whether it's even possible that a certain letter is still in the
	 * bag to play on.
	 * 
	 * Depends on scrabble letter frequency.
	 */
	public boolean letterPossiblyLeft(char Letter) {

		if (lettersUsed[Letter - 'A'] == totalLetters[Letter - 'A'])
			return false;
		else
			return true;
	}

	/** Getter method for bid times */
	public int getBidTimes(int letterPlace) {
		return lettersUsed[letterPlace];
	}

	/**
	 * A simple function to return the number of letters left to bid on, i.e.
	 * number of rounds left. For each player, 8 letters are put in the bag.
	 * 
	 * @return number left
	 */
	public int numTilesLeftToBid() {
		return (numberOfPlayers * (8 - numHidden)) - numBidRoundsPlayed;
	}

	public void setNumBidRoundsPlayed(int i) {
		numBidRoundsPlayed = i;
	}

	public int getNumBidRoundsPlayed() {
		return numBidRoundsPlayed;
	}

	/** Return total number of letters that will be seen in this game */
	public int totalLettersSeenInEntireRound() {
		return (numberOfPlayers * 8);
	}

	public int getNumHidden() {
		return numHidden;
	}

	public void setNumHidden(int numHidden) {
		this.numHidden = numHidden;
	}

	public void setNumberOfPlayers(int size) {
		numberOfPlayers = size;
	}
}