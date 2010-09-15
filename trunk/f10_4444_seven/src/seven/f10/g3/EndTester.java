package seven.f10.g3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class EndTester {
	
	public static void main(String args[]){
		
			combination_list_long = new ArrayList<String>();
			combination_list_short = new ArrayList<String>();
			String str = "";
			
			String filename = "src/seven/f10/g3/alpha-smallwordlist.txt";
			String line = "";
			t = new TrieTree<String>();
			combination_list_short = new ArrayList<String>();
			combination_list_long = new ArrayList<String>();

			// Initialize Trie
			try {
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				// Read each line and then add word to trie
				while ((line = reader.readLine()) != null) {
					line = line.toLowerCase();
					String[] l = line.split(", ");
					t.insert(l[0], l[1]);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//char[] rack = { 'E', 'I', 'I', 'L','L', 'M', 'R', 'R'};
			char[] rack = { 'I', 'L','L', 'M', 'R', 'R'};
			Arrays.sort(rack);

			// Look in trie for words
			String temp = new String(rack);
			System.out.println("\n\n\n\n\ncurrent rack: " + temp);
			combinations("", temp);
			if (rack.length > 0) {
				str = search(combination_list_long);
				if( str == "")
					str = search(combination_list_short);
			}

		System.out.println("Final: " + str + "\n");
		
	}
	
	public static void combinations(String prefix, String s) {
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
	
	private static String search(ArrayList<String> combination_list) {

		for (int i = 0; i < combination_list.size(); i++) {
			TrieNode<String> node = t.returnAutoNode(combination_list.get(i));
			if (node != null && node.isWord() == true) {
				System.out.println("letters used: " + combination_list.get(i));
				System.out.println("word returned: " + node.returnWord());
				return (node.returnWord());
			}
		}
		return ("");
	}

	
	static ArrayList<String> combination_list_long;
	static ArrayList<String> combination_list_short;
	static TrieTree<String> t;

}
