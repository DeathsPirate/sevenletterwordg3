package seven.f10.g3;

import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;
import seven.ui.Letter;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class OurPlayer implements Player
{
	private ArrayList<Character> currentLetters;
	private ArrayList<PlayerBids> cachedBids;
	private int ourID;
	private TrieTree<String> t;
	private ArrayList<String> combination_list;
	protected Logger l = Logger.getLogger(this.getClass());
	
	public OurPlayer(){
		
	}
	/* When our player loads */
	public void Register()
	{
		String filename="src/seven/f10/g3/alpha-smallwordlist.txt";
		String line="";
		t=new TrieTree<String>();
		combination_list = new ArrayList<String>();

		System.out.println("Loading Dictonary. Standby...");

		try
		{
			BufferedReader reader=new BufferedReader(new FileReader(filename));
			// Read each line and then add word to trie
			while ((line=reader.readLine())!=null)
			{

				line=line.toLowerCase();
				String[] l=line.split(", ");
				t.insert(l[0], l[1]);
			}

			l.trace("Dictionary Loaded!");


		}catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

	/* Player Bids */
	public int Bid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList,
		int total_rounds, ArrayList<String> PlayerList,
		SecretState secretstate, int PlayerID)
	{
		if (PlayerBidList.isEmpty()) {
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

		//Generate a random bid
		Random generator = new Random();
		int r=generator.nextInt(20);
		return (r);
	}

	/** Check to see if we win the bid */
	private void checkBid(PlayerBids b)
	{
		if (ourID==b.getWinnerID())
		{
			currentLetters.add(b.getTargetLetter().getAlphabet());
		}
	}

	/* Return our final word back to the simulator */
	public String returnWord()
	{
		l.trace("currentLetters.size(): "+currentLetters.size()+"\n");

		char[] rack=new char[currentLetters.size()];

		for (int i=0; i<currentLetters.size(); i++)
			rack[i]=currentLetters.get(i);
		Arrays.sort(rack);
		
		// Look in trie for words
		combinations("", rack.toString());
		ArrayList<String> temp = new ArrayList<String>();
		//Arrays.sort(combination_list);
		if(currentLetters.size() > 0)
			for(int i = 0; i < combination_list.size(); i++){
				TrieNode<String> node = t.returnAutoNode(combination_list.get(i));
				if(node != null && node.isWord() == true){
					l.trace("letters used: " + currentLetters.get(i));					
					l.trace("word returned: " + node.returnWord());
					return(node.returnWord());
				}	
		}
		return ("");
	}
	
	private void combinations(String prefix, String s) {
        if (s.length() > 0) {
            combination_list.add(prefix + s.charAt(0));
            combinations(prefix + s.charAt(0), s.substring(1));
            combinations(prefix,               s.substring(1));
        }
    }
	
}
