package net.Ox4a42.volksempfaenger.feedparser;

import java.util.ArrayList;
import java.util.List;

public class FeedItem {
	public String getTitle() {
		return "E001 Example Episode";
	}
	
	public String getUrl() {
		return "http://example.com/E001/";
	}
	
	public String getDescription() {
		return "This is the first example episode";
	}
	
	public List<String> getEnclosures() {
		List<String> l = new ArrayList<String>();
		l.add("http://cdn.example.com/e001.mp3");
		return l;
	}
}