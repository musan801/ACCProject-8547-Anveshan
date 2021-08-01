package Google;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import searchtrees.Sequences;

public class spellchecktest {

	public static String SpellCheck(String word) {
		int temp;
		BufferedReader reader;
		ArrayList<String> dictionary = new ArrayList();
		try {
			reader = new BufferedReader(new FileReader("words.txt"));
			String line = reader.readLine();
			while (line != null) {
				// System.out.println(line);
				int distance = Sequences.editDistance(line, word);
				
				if (distance <= 5 && line.startsWith(word.substring(0, 1)) && word.length()<= line.length()) {
					dictionary.add(line);
					// System.out.println(line);
				}
				line = reader.readLine();
			}

			if (dictionary.contains(word)) {

				
				int c = 0;
				for (int i = 0; i < dictionary.size(); i++) {
					// auto complete
					//Collections.sort(dictionary, Collections.reverseOrder());
					
					if (dictionary.get(i).length() > word.length() && dictionary.get(i).indexOf(word) != -1) {
						if (c==0)
						{
							System.out.println("*************************************");
							System.out.println("Our Auto Complete Suggestion:");
						}
						System.out.println(dictionary.get(i));
						c++;
						if (c > 4) {
							break;
						}
					}
				}
				
		
			} else {
				
				
				for(String w:dictionary)
				{
					int Comparator=Sequences.editDistance(word, w);
					if (Comparator<=1)
						return w;
				}
				
				
				if (dictionary.size() >= 5) {
					// Collections.sort(dictionary);
					// Spell correction
					// System.out.println(dictionary.subList(0, 4));
					Collections.sort(dictionary);
					//return dictionary.subList(0, 4);
					
				} else {
					temp=1;
					// System.out.println(dictionary);
					Collections.sort(dictionary);
					//return dictionary;
				}
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return word;

		
	}

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		// String word = input.next();
		String word = "apple";
		// Hashtable<String, String> dictionary;
		// dictionary = new Hashtable<String, String>();
		String a = SpellCheck("match");
		
		
		System.out.println(a);
	}
}
