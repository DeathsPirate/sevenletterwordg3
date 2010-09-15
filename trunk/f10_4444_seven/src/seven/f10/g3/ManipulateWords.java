package seven.f10.g3;

import java.io.*;
import java.util.*;

public class ManipulateWords {

	public static void main(String args[]) {

		try {
			// Open File
			FileInputStream fstream = new FileInputStream(
					"src/seven/f10/g3/smalldictionary.txt");

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			// Read File Line By Line
			FileWriter f = new FileWriter("src/seven/f10/g3/alpha-smallwordlist-temp.txt");
			BufferedWriter out = new BufferedWriter(f);
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				out.write(Sort(strLine) + ", " + strLine + "\n");
			}

			// Close the stream
			in.close();
			out.close();
			System.out.println("finished");

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/* Orders a string */
	public static String Sort(String old_string) {
		char[] c = old_string.toCharArray();
		Arrays.sort(c);
		return (new String(c));
	}

}
