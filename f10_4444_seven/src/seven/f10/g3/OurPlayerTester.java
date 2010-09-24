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
		ArrayList<Letter> secretLetters = new ArrayList<Letter>();		
		secretLetters.add(new Letter('N', 3));
		secretLetters.add(new Letter('E', 1));
		secretLetters.add(new Letter('N', 1));

		secretstate.setSecretLetters(secretLetters);
		
		char[] futureLetters = {'E', 'B', 'E', 'A', 'F', 'R', 'S'};
		
		for(int i = 0; i < futureLetters.length; i++){
			
			char c = futureLetters[i];
			int bid = p.Bid(new Letter(c, 1), PlayerBidList, total_rounds, PlayerList, 
					secretstate, p.ourID);
			
			System.out.println("Bid is: " + bid);
			
			p.addToRack(c);
			
			p.setHighs();			
		}
		
		System.out.println("Game over");
	}	
	static ArrayList<PlayerBids> PlayerBidList = new ArrayList<PlayerBids>();
	static int total_rounds = 1;
	static ArrayList<String> PlayerList;
	static SecretState secretstate = new SecretState(0);
}
