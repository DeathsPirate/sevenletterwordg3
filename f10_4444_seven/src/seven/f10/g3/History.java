package seven.f10.g3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import org.apache.log4j.Logger;
import seven.ui.Letter;
import seven.ui.PlayerBids;

public class History {

	private ArrayList<BidLog> bidLogList;
	private int[] frequencyValue = { 8, 2, 3, 4, 10, 1, 3, 3, 8, 0, 1, 5, 3, 6,
		6, 3, 0, 7, 8, 5, 4, 1, 1, 0, 2, 0 };
	private int[] letterCount={9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 
		1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
	private ArrayList[] marketValue;
	private ArrayList<Integer> allBids;
	protected Logger l = Logger.getLogger(this.getClass());
	private int[] bidTimes;
	private int numberOfPlayers = 0, numHidden = 0, numberOfRoundsPlayed,
			totalLettersInBag = 98;
	private double strengthArr[][];
	private final int L=0;
	private final int M=1;
	private final int H=2;

	public History() {
		bidLogList = new ArrayList<BidLog>();
		marketValue = new ArrayList[26];
		allBids = new ArrayList();
		bidTimes = new int[26];
		
		strengthArr=new double[13][3];
		strengthArr[2][0]=.20; strengthArr[2][1]=.35; strengthArr[2][2]=.75;
		strengthArr[3][0]=.10; strengthArr[3][1]=.30; strengthArr[3][2]=.65;
		strengthArr[4][0]=.05; strengthArr[4][1]=.27; strengthArr[4][2]=.58;
		for (int i=5; i<strengthArr.length; i++)
		{
			strengthArr[i][0]=0;
			strengthArr[i][1]=0.25;
			strengthArr[i][2]=0.55;
		}
	}

	public int adjust(String bidStrategy, Letter bidLetter,
			ArrayList<PlayerBids> cachedBids, int ourID) {
		double bid = 0;
		int bidLetterIndex = bidLetter.getAlphabet() - 'A';

		// round other than first round
		if (cachedBids.size() != 0) {
			int np=cachedBids.get(0).getBidvalues().size();
			boolean is2p=(np==2);
			
			PlayerBids lastBids = cachedBids.get(cachedBids.size() - 1);
			for (int i = 0; i < lastBids.getBidvalues().size(); i++) {
				allBids.add(lastBids.getBidvalues().get(i));
			}
			Letter lastLetter = lastBids.getTargetLetter();
			int lastLetterIndex = lastLetter.getAlphabet() - 'A';

			if (marketValue[lastLetterIndex] == null)
				marketValue[lastLetterIndex] = new ArrayList<Integer>();
			ListIterator<Integer> it = lastBids.getBidvalues().listIterator();
			while (it.hasNext())
				marketValue[lastLetterIndex].add(it.next());

			// update bidTimes
			bidTimes[lastLetterIndex]++;
			
			// strategy
			double strength = 0;
			l.trace("Strategy is: " + bidStrategy);
			if (bidStrategy.equals("L"))
				strength=strengthArr[np][L];
			else if (bidStrategy.equals("M"))
				strength=strengthArr[np][M];
			else if (bidStrategy.equals("H"))
				strength=strengthArr[np][H];

			double overallAffect = .33;
			int indexm = -1;
			double m = 0;
			if (marketValue[bidLetterIndex] == null)
				overallAffect = 1;
			else {
				indexm = (int) (Math.round(strength * marketValue[bidLetterIndex].size()));
				if (indexm == marketValue[bidLetterIndex].size())
					indexm--;
				Collections.sort(marketValue[bidLetterIndex]);
				m = (1 - overallAffect)
						* (Integer) (marketValue[bidLetterIndex].get(indexm));
			}

			int indexa = (int) (Math.round(strength * allBids.size()));
			if (indexa == allBids.size())
				indexa--;
			Collections.sort(allBids);
			double o = overallAffect * allBids.get(indexa);

			// account for the number of letters left in the bag
			double adj = 2 - totalLettersSeen() / totalLettersInBag;
			//account for number of letters left of this letter
			double adj2 = 1.5 - ((letterCount[bidLetterIndex] - 
						bidTimes[bidLetterIndex])/letterCount[bidLetterIndex]);
			bid = adj * adj2 * (m + o);
			
			// do not bid 0 in a 2-player game
			bid+=0.5;
			if (is2p && bid<1)
				bid=1;
		}

		else { // Only used on the first round
			return (frequencyValue[bidLetterIndex]);
		}

		l.trace("At end: " + bid);
		return (int) (bid);
	}

	/**
	 * Returns whether it's even possible that a certain letter is still in the
	 * bag to play on.
	 * 
	 * Depends on scrabble letter frequency.
	 */
	public boolean letterPossiblyLeft(char Letter) {

		if (bidTimes[Letter - 'A'] == letterCount[Letter - 'A'])
			return false;
		else
			return true;
	}

	/** Getter method for bid times */
	public int getBidTimes(int letterPlace) {
		return bidTimes[letterPlace];
	}

	/**
	 * A simple function to return the number of letters left to bid on, i.e.
	 * number of rounds left. For each player, 8 letters are put in the bag.
	 * 
	 * @return number left
	 */
	public int numTilesLeftToBid() {
		return (numberOfPlayers * (8 - numHidden)) - numberOfRoundsPlayed;
	}

	public void setNumberOfRoundsPlayed(int i) {
		numberOfRoundsPlayed = i;
	}
	
	public int getNumberOfRoundsPlayed() {
		return numberOfRoundsPlayed;
	}

	/** Return total number of letters that will be seen in this game */
	public int totalLettersSeen() {
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