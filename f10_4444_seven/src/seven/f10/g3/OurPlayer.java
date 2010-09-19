package seven.f10.g3;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import org.apache.log4j.Logger;
import seven.ui.Letter;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.Scrabble;
import seven.ui.SecretState;

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
	private int lowBid = 0;
	private int highBid = 5;
	private String highWord = "";
	private int highWordAmt = 0;
	private ArrayList<String> seven_letter_words;

	// For use to keep track of market value of letters
	private int[] bidTimes = new int[26];
	private int[] bidSums = new int[26];

	// Letter Frequency Array
	private int[] letterFrequency = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 
										1, 6, 4, 6, 4, 2, 2, 1, 2, 1};

	static {

		String filename = "src/seven/f10/g3/alpha-smallwordlist.txt";
		String line = "";
		t = new TrieTree<String>();

		// Initialize Trie
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			// Read each line and then add word to trie
			while ((line = reader.readLine()) != null) {

				line = line.toUpperCase();
				String[] l = line.split(", ");
				t.insert(l[0], l[1]);
			}

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
		ArrayList<String> seven_letter_words = new ArrayList<String>();
		String filename7 = "src/seven/f10/g3/alpha-smallwordlist7.txt";
		String line = "";
		BufferedReader reader7;
		try {
			reader7 = new BufferedReader(new FileReader(filename7));
			while ((line = reader7.readLine()) != null) {

				line = line.toUpperCase();
				String[] l = line.split(", ");
				seven_letter_words.add(l[0]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Instantiate the market value arrays 
		for(int i = 0; i < 26; i++)
		{
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
		int bid = lowBid;
		if (sevenLetterWordLeft() == true)
			bid = comparisonBid(bid, bidLetter);
		else
			bid = defaultBid(bid, bidLetter);

		// Adjustment bidding according to History
		double adjust=History.adjust(cachedBids, ourID);
		return (int)(bid+adjust);
	}

	public int comparisonBid(int bid, Letter bidLetter) {

		int posOnRack = numberOfPossibilitisOnRack(); // Do we want to search whole rack or just a
		// subset

		for (char c = 'A'; c <= 'Z'; c++) {//How do we want to order this?
			
		}

		// if greater than threshold, return high bid

		return (0);
	}
	
	/** A function to quickly get market value as calculated by previous bid wins. */ 
	public int marketValue(char Letter)
	{
		int letterPlace = Letter - 'a';
		return bidSums[letterPlace] / bidTimes[letterPlace]; 	// return winning bid sums divided by times bid on.
																// i.e. average winning bid.
	
	}
	
	
	public void printMarketValues()
	{
		for(int i = 0; i < 26; i++)
			l.trace("Letter: " + ('a'+i) + ", Value: " + bidSums[i] / bidTimes[i]);
	}
	
	
	//How do we want to deal with letters that we don't yet have?
	public int numberOfPossibilitisOnRack(){
		
		int i = 0;
		String pat = "[?A-";
		char[] c = currentRack.getCharArray();
		for(int j = 0; j < c.length; j++){
			pat += (c[j] + c[j] + "?" + c[j] + "-");
		}
        Pattern p = Pattern.compile(pat);

		while (i < seven_letter_words.size()){
			Matcher m = p.matcher(seven_letter_words.get(i));
			if(m.find())
				i++;
			else
				seven_letter_words.remove(i);
		}
		
		return(seven_letter_words.size());
	}

	public int numberOfPossibilities(String letters) {

		return (1);
	}

	public int defaultBid(int bid, Letter bidLetter) {
		char[] c = new char[currentRack.wantSize() + 2];
		for (int i = 0; i < currentRack.wantSize(); i++)
			if (currentRack.get(i).getWant() == true)
				c[i] = currentRack.get(i).getL();
		c[currentRack.wantSize()] = bidLetter.getAlphabet();
		Arrays.sort(c);

		TrieNode<String> node = t.returnAutoNode(new String(c));

		if (node != null && node.isWord() == true) {
			bid = highBid;

		} else {
			for (char ch = 'A'; ch <= 'Z'; ch++) {
				c[0] = ch;
				Arrays.sort(c);
				node = t.returnAutoNode(new String(c));
				if (node != null && node.isWord() == true
						&& getWordAmount(node.returnWord()) > highWordAmt) {
					bid = highBid;
					ch = 'Z';
				}
			}
		}

		if (bid == lowBid)// If we still have not decided we need it, then we
		// search every possible combination
		{
			System.out.println("look for a completely new word");
			String temp = getHighWord();
			if (getWordAmount(temp) > highWordAmt)
				bid = highBid;
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
		if (ourID == b.getWinnerID() && b.getWinAmmount() > lowBid)
			want = true;

		if (ourID == b.getWinnerID()) {
			currentRack.add(new RackLetter(b.getTargetLetter().getAlphabet(),
					want));
			setHighs();
		}
		
		// get bid info to add to the market value statistics
		int letterPlace = b.getTargetLetter().getAlphabet() - 'a';	// get letter place
		bidTimes[letterPlace]++;									// got bid on 
		bidSums[letterPlace]+= b.getWinAmmount();					// add to win amount
		
		// to print the market values at the end of bidding. 
		printMarketValues();
	}

	/** Reset high word and high score */
	public void setHighs() {

		String temp = getHighWord();
		if (temp != null) {
			highWord = temp;
			currentRack.resetWants(highWord);
			highWordAmt = getWordAmount(highWord);
			l.trace("Just set highs to: " + highWord + ", " + highWordAmt);

		}
	}

	public String getHighWord() {

		char[] rack = new char[currentRack.size()];
		rack = currentRack.getCharArray();
		Arrays.sort(rack);

		System.out.println("finding word for: " + new String(rack));

		// Look in trie for words
		String temp = new String(rack);
		combinations("", temp);
		if (rack.length > 0) {
			String str = search(combination_list_long);
			if (str.length() > 0)
				return (str.toUpperCase());
			else
				return (search(combination_list_short).toUpperCase());
		}

		return ("");
	}

	/** Return our final word back to the simulator */
	public String returnWord() {
		setHighs();
		return (highWord);
	}

	private String search(ArrayList<String> combination_list) {

		for (int i = 0; i < combination_list.size(); i++) {
			TrieNode<String> node = t.returnAutoNode(combination_list.get(i)
					.toUpperCase());
			if (node != null && node.isWord() == true) {
				/*
				 * l.trace("letters used: " + combination_list.get(i));
				 * l.trace("word returned: " + node.returnWord());
				 */
				if (getWordAmount(node.returnWord()) > highWordAmt) {
					return (node.returnWord());
				}
			}
		}
		return ("");
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

	private double bidRef(int round) {
		if (cachedBids.size() == 0)
			return 0;
		PlayerBids bid = cachedBids.get(round);
		int sum = 0;
		int top = 0;
		ListIterator<Integer> it = bid.getBidvalues().listIterator();
		while (it.hasNext()) {
			int temp = it.next();
			if (temp > top)
				top = temp;
			sum += temp;
			// sum+=it.next();
		}

		double refValue = 0.5 + 1.000 * (sum - top)
				/ (bid.getBidvalues().size() - 1) / top;
		double avg = 1.000 * sum / bid.getBidvalues().size();
		double ref = refValue * (avg - bid.getBidvalues().get(ourID));
		/*
		 * l.trace("refValue: "+refValue); l.trace("avg: "+avg);
		 * l.trace("outID: "+bid.getBidvalues().get(ourID));
		 * l.trace("ref: "+ref);
		 */
		return ref;
	}

	private int getWordAmount(String word) {

		char[] c = word.toCharArray();
		int amt = 0;
		for (int i = 0; i < c.length; i++) {
			amt += Scrabble.letterScore(c[i]);
		}

		return (amt);
	}

	/**
	 * Lets us know if it is possible to get a seven letter word with the
	 * remaining letters
	 */
	public boolean sevenLetterWordLeft() {
		
		if(seven_letter_words.size() > 0)
			return (true);
		else
			return(false);
	}
}
