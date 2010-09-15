package seven.f10.g3;

import java.util.*;
import java.io.*;
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
	private ArrayList<String> combination_list;
	protected Logger l;;
	private char[][] alphabet;

	public OurPlayer() {

	}

	/* When our player loads */
	public void Register() {
		String filename = "src/seven/f10/g3/alpha-smallwordlist.txt";
		String line = "";
		t = new TrieTree<String>();
		combination_list = new ArrayList<String>();
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

		// Generate a random bid
		Random generator = new Random();
		int r = generator.nextInt(5);
		return (r);
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
		combination_list = sort_by_length(combination_list);
		if (rack.length > 0)
			for (int i = 0; i < combination_list.size(); i++) {
				l.trace("looking for: " + combination_list.get(i));
				TrieNode<String> node = t.returnAutoNode(combination_list
						.get(i));
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
			combination_list.add(prefix + s.charAt(0));
			combinations(prefix + s.charAt(0), s.substring(1));
			combinations(prefix, s.substring(1));
		}
	}

	private ArrayList<String> sort_by_length(ArrayList<String> old_list) {

		l.trace("here");
		int i = 0;
		int j = 0;
		Boolean keepgoing = true;
		while (keepgoing == true) {
			keepgoing = false;
			for (i = 0; i < old_list.size(); i++) {
				for (j = 0; j < old_list.size(); j++) {
					if (old_list.get(i).length() < old_list.get(j).length()) {
						l.trace("before: " + old_list.get(i));
						Collections.swap(old_list, i, j);
						l.trace("after: " + old_list.get(i)+ "\n");						
						keepgoing = true;
					}
				}
			}
			i = 0;
		}
		l.trace("returning");
		return old_list;
	}

}
