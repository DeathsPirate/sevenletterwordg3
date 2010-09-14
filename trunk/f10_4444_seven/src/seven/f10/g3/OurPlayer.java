package seven.f10.g3;

import java.util.ArrayList;
import java.io.*;

import seven.ui.Letter;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class OurPlayer implements Player {

	/*When our player loads*/
	public void Register() {
		System.out.println("loading player");
		BufferedReader r;
		String line = null;
		ArrayList<Word> wtmp = new ArrayList<Word>(55000);
		try {
			r = new BufferedReader(new FileReader(
					"src/seven/g1/super-small-wordlist.txt"));
			while (null != (line = r.readLine())) {
				wtmp.add(new Word(line.trim()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wordlist = wtmp.toArray(new Word[wtmp.size()]);
	}
	
	/*Player Bids*/
	public int Bid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList,
			int total_rounds, ArrayList<String> PlayerList,
			SecretState secretstate, int PlayerID) {
		/*if (PlayerBidList.isEmpty()) {
			cachedBids = PlayerBidList;
		}

		if (null == currentLetters) {
			currentLetters = new ArrayList<Character>(7);
			ourID = PlayerID;
			for (Letter l : secretstate.getSecretLetters()) {
				currentLetters.add(l.getAlphabet());
			}
		} else {
			if (cachedBids.size() > 0) {
				checkBid(cachedBids.get(cachedBids.size() - 1));
			}
		}

		return 0;*/
		
		return 1;
	}

	/*Check to see if we win the bid*/
	private void checkBid(PlayerBids b) {
		if (ourID == b.getWinnerID()) {
			currentLetters.add(b.getTargetLetter().getAlphabet());
		}
	}

	/*Return our final word back to the simulator*/
	public String returnWord() {
		/*checkBid(cachedBids.get(cachedBids.size() - 1));
		char c[] = new char[7];
		for (int i = 0; i < currentLetters.size(); i++) {
			c[i] = currentLetters.get(i);
		}
		String s = new String(c);
		Word ourletters = new Word(s);
		Word bestword = new Word("");
		for (Word w : wordlist) {
			if (ourletters.contains(w)) {
				if (w.score > bestword.score) {
					bestword = w;
				}

			}
		}
		currentLetters = null;
		System.out.println("about to return: " + bestword.word);*/
		//return bestword.word;
		return("hello");
	}

	ArrayList<Character> currentLetters;
	private int ourID;
	private ArrayList<PlayerBids> cachedBids;
	private Word[] wordlist;

}
