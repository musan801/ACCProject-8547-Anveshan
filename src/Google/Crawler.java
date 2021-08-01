/**
 * 
 */
package Google;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import searchtrees.RedBlackBST;
import textprocessing.BoyerMoore;

public class Crawler {
	private static final int MAX_DEPTH = 2;
	private HashSet<String> links;
	public Hashtable<String, String> TextOfWebsites;

	public Crawler() {
		links = new HashSet<>();
		TextOfWebsites = new Hashtable<String, String>();
	}

	public void getPageLinks(String URL, int depth) {
		if ((!links.contains(URL) && (depth < MAX_DEPTH))) {
			System.out.println(">> Depth: " + depth + " [" + URL + "]");
			try {
				links.add(URL);
				Document document = Jsoup.connect(URL).get();
				Elements linksOnPage = document.select("a[href]");
				String HtmlToText = Jsoup.parse(document.toString()).text();
				TextOfWebsites.put(URL, HtmlToText);
				// System.out.println(TextOfWebsites.get("http://en.wikipedia.org/").length());
				depth++;
				for (Element page : linksOnPage) {
					getPageLinks(page.attr("abs:href"), depth);
				}
			} catch (IOException e) {
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
	}

	public int KeyWordMatching(String readfile, String patterns) {
		readfile = TextOfWebsites.get(readfile);
		readfile = readfile.toLowerCase();
		ArrayList<Integer> offsets = new ArrayList<Integer>();
		BoyerMoore bm = new BoyerMoore(patterns);
		int start = 0, offset = 0, k = 0, counter = 0;
		String searchStr = "";
		while (true) {
			searchStr = readfile.substring(start);
			offset = bm.search(searchStr);
			counter += offset;
			if (offset == searchStr.length())
				break;
			offsets.add(k, counter);
			counter += patterns.length();
			k++;
			start = offset + start + patterns.length();
		}
		return offset;

	}

	public void StoreCounts(String keyword) {
		RedBlackBST< Integer,String> Tree = new RedBlackBST<Integer,String>();
		Set<String> URLs = TextOfWebsites.keySet();
		for (String URL : URLs) {
			// System.out.println(TextOfWebsites.get(URL)+" aaaa");
			int temp = KeyWordMatching(URL, keyword.toLowerCase());
			Tree.put(temp,URL);
		}

		System.out.println(keyword + " occurrences:");
		for (int i = 0; i < 5; i++) {
			if (Tree.isEmpty()) {
				break;
			}
			int count = Tree.max();
			String URL = Tree.get(Tree.max());
			Tree.deleteMax();
			System.out.println(count + " times in: " + URL );
		}
	}

	public static void main(String[] args) {
		System.out.println();
		Crawler myObj = new Crawler();
		myObj.getPageLinks("http://en.wikipedia.org/", 0);
		myObj.StoreCounts("English");

	}
}