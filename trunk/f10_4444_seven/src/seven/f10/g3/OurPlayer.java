package seven.f10.g3;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import seven.f10.g3.DataMine.ItemSet;
import seven.f10.g3.LetterMine.LetterSet;
import seven.ui.*;

/**
 * @author David, Elba, and Lauren
 * 
 */
public class OurPlayer implements Player {

	// Variables
	private Rack currentRack;
	private ArrayList<PlayerBids> cachedBids;
	public int ourID;
	static private TrieTree<String> t;
	private ArrayList<String> combination_list_short;
	private ArrayList<String> combination_list_long;
	protected Logger l = Logger.getLogger(this.getClass());
	private String highWord = "";
	private int highWordAmt = 0;
	private static DataMine mine;

	// For use to keep track of market value of letters
	private int[] bidTimes = new int[26];
	private int[] bidSums = new int[26];

	// Letter Frequency Array
	private int[] letterFrequency = { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2,
			6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };

	static {

		String filename = "src/seven/f10/g3/alpha-smallwordlist.txt";
		String line = "";
		t = new TrieTree<String>();

		// Initialize Trie
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				line = line.toUpperCase();
				String[] l = line.split(", ");
				t.insert(l[0], l[1]);
			}

			mine = null;
			mine = new LetterMine("src/seven/f10/g3/smallwordlist.txt");
			mine.buildIndex();
			ItemSet[] answer = mine.aPriori(0.000001);
			System.out.println("alive and well: " + answer.length
					+ " itemsets total");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** When our player loads */
	public void Register() {
		combination_list_short = new ArrayList<String>();
		combination_list_long = new ArrayList<String>();

		// Instantiate the market value arrays
		for (int i = 0; i < 26; i++) {
			bidTimes[i] = 0;
			bidSums[i] = 0;
		}
	}

	/** Player Bids */
	public int Bid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList,
			int total_rounds, ArrayList<String> PlayerList,
			SecretState secretstate, int PlayerID) {

		if (PlayerBidList.isEmpty()) {
			cachedBids = PlayerBidList;
		}
		if (null == currentRack) {
			setupRack(PlayerID, secretstate);
		} else {
			if (cachedBids.size() > 0) {
				checkBid(cachedBids.get(cachedBids.size() - 1));
			}
		}

		// Generate Bids
		String bid = "L";
		if (sevenLetterWordLeft() == true)
			bid = comparisonBid(bidLetter);
		else
			bid = defaultBid(bidLetter);

		//Adjustment bidding according to History
		double adjustedBid = History.adjust(bid, cachedBids, ourID);
		return (int) (adjustedBid);
	}

	/**
	 * Main bidding strategy - we find the number of possibilities with our
	 * current rack and compare that to the number of possibilities with the bid
	 * letter. If that is over a certain threshold we bid high
	 */
	public String comparisonBid(Letter bidLetter) {

		int b = 0;

		// Set up char array to use as our temporary rack
		char[] rack = new char[currentRack.size() + 1];
		for (int i = 0; i < currentRack.size(); i++)
			rack[i] = currentRack.get(i).getL();

		// Create two lists
		int[] sortedAmounts = new int[26];

		for (char c = 'A'; c <= 'Z'; c++) {
			if (lettersPossiblyLeft(c)) {
				rack[rack.length - 1] = c;
				int pos = numberOfPossibilities(rack);
				sortedAmounts[c - 65] = pos;
				if (c == bidLetter.getAlphabet()) {
					b = pos;
				}
			}
		}
		
		//Determine high, medium, or low bid
		Arrays.sort(sortedAmounts);
		if (b > sortedAmounts[5])
			return ("H");
		else if (b > sortedAmounts[10])
			return ("M");
		else
			return ("L");
	}

	/**
	 * A function to quickly get market value as calculated by previous bid
	 * wins.
	 */
	public int marketValue(char Letter) {
		int letterPlace = Letter - 'A';
		return bidSums[letterPlace] / bidTimes[letterPlace]; // return winning
		// bid sums
		// divided by
		// times bid on.
		// i.e. average winning bid.

	}

	public void printMarketValues() {
		for (int i = 0; i < 26; i++)
			l.trace("Letter: " + ('A' + i) + ", Value: " + bidSums[i]
					/ bidTimes[i]);
	}

	public int numberOfPossibilities(char[] arr) {

		int count = 0;
		String[] strarr = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			strarr[i] = Character.toString(arr[i]);
		}

		String[] args = strarr;
		LetterSet i = (LetterSet) mine.getCachedItemSet(args);

		if (null != i) {
			String[] words = i.getWords();
			count = words.length;
		}

		return (count);
	}

	public String defaultBid(Letter bidLetter) {
		String bid = "L";
		char[] c = new char[currentRack.wantSize() + 2];
		for (int i = 0; i < currentRack.wantSize(); i++)
			if (currentRack.get(i).getWant() == true)
				c[i] = currentRack.get(i).getL();
		c[currentRack.wantSize()] = bidLetter.getAlphabet();
		Arrays.sort(c);

		TrieNode<String> node = t.returnAutoNode(new String(c));

		if (node != null && node.isWord() == true) {
			bid = "H";

		} else {
			for (char ch = 'A'; ch <= 'Z'; ch++) {
				c[0] = ch;
				Arrays.sort(c);
				node = t.returnAutoNode(new String(c));
				if (node != null && node.isWord() == true
						&& getWordAmount(node.returnWord()) > highWordAmt) {
					bid = "H";
					ch = 'Z';
				}
			}
		}

		if (bid == "L")// If we still have not decided we need it, then we
		// search every possible combination
		{
			String temp = getHighWord();
			if (getWordAmount(temp) > highWordAmt)
				bid = "H";
		}

		return (bid);
	}

	/** Set up rack at beginning of game - includes adding hidden letters */
	public void setupRack(int PlayerID, SecretState secretstate) {

		currentRack = new Rack();
		ourID = PlayerID;
		for (Letter l : secretstate.getSecretLetters()) {
			currentRack.add(new RackLetter(l.getAlphabet(), false));
		}
		setHighs();

	}

	/** Check to see if we win the bid, if so add it to your rack */
	private void checkBid(PlayerBids b) {
		Boolean want = false;

		// check to see if we actually wanted it
		if (ourID == b.getWinnerID() && b.getWinAmmount() > 0)
			want = true;

		if (ourID == b.getWinnerID()) {
			currentRack.add(new RackLetter(b.getTargetLetter().getAlphabet(),
					want));
			setHighs();
		}

		// get bid info to add to the market value statistics
		// int letterPlace = b.getTargetLetter().getAlphabet() - 'A'; // get
		// letter place
		// bidTimes[letterPlace]++; // got bid on
		// bidSums[letterPlace]+= b.getWinAmmount(); // add to win amount

		// to print the market values at the end of bidding.
		// printMarketValues();
	}

	/** Reset high word and high score */
	public void setHighs() {

		String temp = getHighWord();
		if (temp != null) {
			highWord = temp;
			currentRack.resetWants(highWord);
			highWordAmt = getWordAmount(highWord);
			System.out.println("Just set highs to: " + highWord + ", "
					+ highWordAmt);
		}
	}

	public String getHighWord() {

		char[] rack = new char[currentRack.size()];
		rack = currentRack.getCharArray();
		Arrays.sort(rack);

		// Look in trie for words
		String temp = new String(rack);
		combinations("", temp);
		if (rack.length > 0) {
			String str = search(combination_list_long);
			if (str.length() > 0)
				return (str.toUpperCase());
			else {
				str = search(combination_list_short);
				if (str.length() > 0)
					return (str.toUpperCase());
			}
		}
		return ("");
	}

	/** Return our final word back to the simulator */
	public String returnWord() {
		setHighs();
		return (highWord);
	}

	private String search(ArrayList<String> combination_list) {

		String word = "";
		int amount = 0;
		for (int i = 0; i < combination_list.size(); i++) {
			TrieNode<String> node = t.returnAutoNode(combination_list.get(i)
					.toUpperCase());
			if (node != null && node.isWord() == true) {
				int amt = getWordAmount(node.returnWord());
				if (amt > amount) {
					word = node.returnWord();
					amount = amt;
				}

			}
		}
		return (word);
	}

	private void combinations(String prefix, String s) {
		if (s.length() > 0) {
			String str = prefix + s.charAt(0);
			if (str.length() > 3) {
				combination_list_long.add(str);
			} else {
				combination_list_short.add(str);
			}
			combinations(prefix + s.charAt(0), s.substring(1));
			combinations(prefix, s.substring(1));
		}
	}

	/*
	 * private ArrayList<String> sort_by_length(ArrayList<String> old_list) {
	 * 
	 * l.trace("here"); int i = 0; int j = 0; Boolean keepgoing = true; while
	 * (keepgoing == true) { keepgoing = false; for (i = 0; i < old_list.size();
	 * i++) { for (j = 0; j < old_list.size(); j++) { if
	 * (old_list.get(i).length() < old_list.get(j).length()) {
	 * l.trace("before: " + old_list.get(i)); Collections.swap(old_list, i, j);
	 * l.trace("after: " + old_list.get(i)+ "\n"); keepgoing = true; } } } i =
	 * 0; } l.trace("returning"); return old_list; }
	 */

	public int getWordAmount(String word) {

		char[] c = word.toCharArray();
		int amt = 0;
		for (int i = 0; i < c.length; i++) {
			amt += Scrabble.letterScore(c[i]);
		}
		if (c.length == 7)
			amt += 50;

		return (amt);
	}

	/**
	 * Lets us know if it is possible to get a seven letter word with the
	 * remaining letters
	 */
	public boolean sevenLetterWordLeft() {

		/*
		 * if(seven_letter_words.size() > 0) return (true); else return(false);
		 */
		return true;
	}

	public boolean lettersPossiblyLeft(char Letter) {

		if (bidTimes[Letter - 'A'] == letterFrequency[Letter - 'A'])
			return false;
		else
			return true;
	}

	/** Method only for debugging - artiicially adds to rack */
	public void addToRack(char c) {
		currentRack.add(new RackLetter(c, true));
		System.out.println("Rack is now: "
				+ new String(currentRack.getCharArray()));
	}
}