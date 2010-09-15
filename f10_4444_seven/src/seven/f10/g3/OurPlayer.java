package seven.f10.g3;

import java.util.*;
import java.io.*;

import javax.xml.stream.events.Characters;

import org.apache.log4j.Logger;
import seven.ui.Letter;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class OurPlayer implements Player {
	private ArrayList<Character> currentLetters;
	private ArrayList<PlayerBids> cachedBids;
	private int ourID;
	private TrieTree<String> t;
	private ArrayList<String> combination_list_short;
	private ArrayList<String> combination_list_long;
	protected Logger l;;
	private char[][] alphabet;

	public OurPlayer() {

	}

	/* When our player loads */
	public void Register() {
		String filename = "src/seven/f10/g3/alpha-smallwordlist.txt";
		String line = "";
		t = new TrieTree<String>();
		combination_list_short = new ArrayList<String>();
		combination_list_long = new ArrayList<String>();
		l = Logger.getLogger(this.getClass());
		alphabet = new char[26][2];

		// Initialize Trie
		l.trace("Loading Dictonary. Standby...");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			// Read each line and then add word to trie
			while ((line = reader.readLine()) != null) {

				line = line.toLowerCase();
				String[] l = line.split(", ");
				t.insert(l[0], l[1]);
			}

			l.trace("Dictionary Loaded!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Initialize alphabet

	}

	/* Player Bids */
	public int Bid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList,
			int total_rounds, ArrayList<String> PlayerList,
			SecretState secretstate, int PlayerID) {
		
		/*if(currentLetters != null){
		if(currentLetters.size() > 0)
			l.trace("\n\nAt beginning of bid method our rack is: " + currentLetters.toString());
		else
			l.trace("\n\nAt beginning of bid method our rack is: 0");}
		else{
			l.trace("\n\nAt beginning of bid method our rack is: null");
			}*/
		
		if (PlayerBidList.isEmpty()) {
			cachedBids = PlayerBidList;
		}

		if (null == currentLetters) {
			currentLetters = new ArrayList<Character>();
			ourID = PlayerID;
			for (Letter l : secretstate.getSecretLetters()) {
				currentLetters.add(l.getAlphabet());
			}
		} else {
			if (cachedBids.size() > 0) {
				checkBid(cachedBids.get(cachedBids.size() - 1));
			}
		}

		// Generate Bids
		int bid = 1;

		ArrayList<Character> temp = new ArrayList<Character>(currentLetters);
		temp.add(bidLetter.getAlphabet());
		char[] tempRack = new char[temp.size() + 1];
		for (int i = 0; i < temp.size(); i++)
			tempRack[i] = temp.get(i);
		Arrays.sort(tempRack);
		TrieNode<String> node = t.returnAutoNode(new String(tempRack));
		if (node != null && node.isWord() == true) {
			bid = 5;
		} 
		/*else {
			for (char c = 'a'; c <= 'z'; c++) {
				tempRack[(tempRack.length - 1)] = c;
				Arrays.sort(tempRack);
				node = t.returnAutoNode(new String(tempRack));
				if (node != null && node.isWord() == true) {
					bid = 5;
				}
			}
		}*/
		
		l.trace("Our rack is: " + currentLetters.toString() + ", the letter up was: " + bidLetter.getAlphabet() + ", and we bid: " + bid);
		return (bid);
	}

	/** Check to see if we win the bid */
	private void checkBid(PlayerBids b) {
		if (ourID == b.getWinnerID()) {
			currentLetters.add(b.getTargetLetter().getAlphabet());
		}
	}

	/* Return our final word back to the simulator */
	public String returnWord() {
		l.trace("currentLetters.size(): " + currentLetters.size());

		char[] rack = new char[currentLetters.size()];

		for (int i = 0; i < currentLetters.size(); i++)
			rack[i] = currentLetters.get(i);
		Arrays.sort(rack);

		// Look in trie for words
		String temp = new String(rack);
		l.trace("current rack: " + temp);
		combinations("", temp);
		if (rack.length > 0) {
			String str = search(combination_list_long);
			if (str.length() > 0)
				return (str);
			else
				search(combination_list_short);
		}

		return ("");
	}

	private String search(ArrayList<String> combination_list) {

		for (int i = 0; i < combination_list.size(); i++) {
			TrieNode<String> node = t.returnAutoNode(combination_list.get(i));
			if (node != null && node.isWord() == true) {
				l.trace("letters used: " + combination_list.get(i));
				l.trace("word returned: " + node.returnWord());
				return (node.returnWord());
			}
		}
		return ("");
	}

	private void combinations(String prefix, String s) {
		if (s.length() > 0) {
			String str = prefix + s.charAt(0);
			if (str.length() > 3)
				combination_list_long.add(str);
			else
				combination_list_short.add(str);
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

}
