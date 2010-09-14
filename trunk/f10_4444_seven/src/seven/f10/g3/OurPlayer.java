package seven.f10.g3;

import java.util.*;
import java.io.*;

import seven.ui.Letter;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class OurPlayer implements Player {

	/* When our player loads */
	public void Register() {
		String filename = "src/seven/f10/g3/alpha-smallwordlist.txt";
		String line = "";
		t = new TrieTree<String>();
		
		System.out.println("Loading Dictonary. Standby...");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			//Read each line and then add word to trie
			while ((line = reader.readLine()) != null) {

				line = line.toLowerCase();
				String[] l = line.split(", ");
				t.insert(l[0], l[1]);
			}

			System.out.println("Dictionary loaded!");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Player Bids */
	public int Bid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList,
			int total_rounds, ArrayList<String> PlayerList,
			SecretState secretstate, int PlayerID) {
		Random generator = new Random();
		int r = generator.nextInt(20);
		return (r);
	}

	/* Check to see if we win the bid */
	private void checkBid(PlayerBids b) {
		if (ourID == b.getWinnerID()) {
			currentLetters.add(b.getTargetLetter().getAlphabet());
		}
	}

	/* Return our final word back to the simulator */
	public String returnWord() {
		
		char[] rack = new char[currentLetters.size()];
		for(int i = 0; i < currentLetters.size(); i++)
			rack[i] = currentLetters.get(i);
		Arrays.sort(rack);
		Boolean haveWord = false;
		
		//Look in trie for words
		while(haveWord == false){
			for(int i = rack.length; i > 0; i--){//find combinations for all letters
				
				
				
			}
			
		}
		
		
		return ("hello");
	}

	ArrayList<Character> currentLetters;
	private int ourID;
	private TrieTree<String> t;

}
