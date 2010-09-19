package seven.f10.g3;

import java.util.ArrayList;
import seven.ui.Letter;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class OurPlayerTester{
	
	public static void main(String args[]){
		
		System.out.println("Testing OurPlayer....");
	
		OurPlayer p = new OurPlayer();
		p.Register();
		ArrayList<Letter> secretLetters = new ArrayList<Letter>();		secretLetters.add(new Letter('A', 1));
		secretLetters.add(new Letter('B', 1));
		secretLetters.add(new Letter('A', 1));
		secretLetters.add(new Letter('T', 1));
		
		secretstate.setSecretLetters(secretLetters);
		int bid = p.Bid(bidLetter, PlayerBidList, total_rounds, PlayerList, 
				secretstate, p.ourID);
		
		System.out.println("Bid is: " + bid);	
		
	}
	
	static Letter bidLetter = new Letter('O', 1);
	static ArrayList<PlayerBids> PlayerBidList = new ArrayList<PlayerBids>();
	static int total_rounds = 1;
	static ArrayList<String> PlayerList;
	static SecretState secretstate = new SecretState(0);
}
