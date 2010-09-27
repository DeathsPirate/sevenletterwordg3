package seven.f10.g3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class wordValueCount {
	
	public static void main(String args[])
	{
		int totalCount = 0;
		int totalWords = 0;
		
		char[] letterValues = {1,3,3,2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
		
		//System.out.println(letterValues.length);
		
		String filename = "src/seven/f10/g3/alpha-smallwordlist7.txt";
		String line = "";
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				totalWords++;
				line = line.toUpperCase();
				String[] l = line.split(", ");
				String l2 = l[0];
				
				int wordValue = 0;
				for(int i = 0; i < l2.length(); i++)
				{
					System.out.print(l2.charAt(i) + " ");
					 wordValue += letterValues[l2.charAt(i) - 'A'];
				}
				System.out.println(" " +  wordValue  + " "+ totalWords);
				totalCount += wordValue;
			}
		}
		catch(Exception e)
		{
			// eww
		}
		
		System.out.println(totalWords);
		System.out.println(totalCount);
		System.out.println(totalCount/totalWords);
	}
}