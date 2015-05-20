package source;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import source.WallpaperChanger.SPI;
import com.google.gson.*;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
//import java.awt.*;
//import java.awt.event.*;

public class Newskins {
	public static void main(String[] args) {
	    try {
	       
		
	String html = Jsoup.connect("http://leagueoflegends.wikia.com/wiki/League_of_Legends_Wiki/New_skins").get().html();
	for(int x = 2; x <= 14; x = x+2) {
	String parse = Jsoup.parse(html,"ISO-8859-1").select("body").get(0).select("div").get(40).select("div").get(5).select("div").get(2).select("div").get(3).select("div").get(x).html();
	String[] parts = parse.split("/wiki/");
	String part2 = parts[1]; 
	String[] parted = part2.split("/");
	String part3 = parted[0];
	String url = "https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion?champData=skins&api_key=bf7ec21b-9468-4e70-9019-e836fc5af85d";
    String json = IOUtils.toString(new URL(url));
    JsonParser jsonParser = new JsonParser();
    JsonElement results = jsonParser.parse(json)
            .getAsJsonObject().get("data") 
            .getAsJsonObject().get(part3)
    		.getAsJsonObject().get("skins");
    JsonArray skins = results.getAsJsonArray();
    int size = skins.size();
    int realsize = size - 1;
    URL url2 = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + part3 +"_" + realsize + ".jpg");
		InputStream is = url2.openStream();
		OutputStream os = new FileOutputStream( "image.jpg");
		byte[] b = new byte[2048];
		int length;
		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		is.close();
		os.close();
		String path = "image.jpg";
	      SPI.INSTANCE.SystemParametersInfo(
	          new UINT_PTR(SPI.SPI_SETDESKWALLPAPER), 
	          new UINT_PTR(0), 
	          path, 
	          new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE));
	TimeUnit.SECONDS.sleep(5);
	}
	} catch (IOException f) {
        f.printStackTrace(); 
	} catch(InterruptedException ex) {
	   Thread.currentThread().interrupt();
	}
    //catch (ParseException e)
    {
   // e.printStackTrace();
}
	}
}
