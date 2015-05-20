package source;

import java.io.IOException;

import org.jsoup.Jsoup;

public class FreeChamps {
	public static void main(String[] args) {
		try{
			String html = Jsoup.connect("http://leagueoflegends.wikia.com/wiki/Free_champion_rotation").get().html();
			System.out.print(html);
		} catch (IOException f) {
	        f.printStackTrace();
	    } 
		}
	}
