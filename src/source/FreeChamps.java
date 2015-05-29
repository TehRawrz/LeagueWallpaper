package source;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import source.WallpaperChanger.SPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;

public class FreeChamps {
	public static void main(String[] args) {
		try{
			//parse freetoplay json
			String url= "https://na.api.pvp.net/api/lol/na/v1.2/champion?freeToPlay=true&api_key=bf7ec21b-9468-4e70-9019-e836fc5af85d";
			String json = IOUtils.toString(new URL(url));
			JsonParser parse = new JsonParser();
			JsonElement skins = parse.parse(json)
					.getAsJsonObject().getAsJsonArray("champions");
			JsonArray test = skins.getAsJsonArray();
		    int size = test.size();
		    int realsize = size - 1 ;
			for(int x=0; x <= realsize; x = x+1){
		    JsonParser jsonParser = new JsonParser();
		    JsonElement results = jsonParser.parse(json)
		    		 .getAsJsonObject().getAsJsonArray("champions").get(x)
		    		 .getAsJsonObject().get("id");
		    //get name of champ from id
		    String url2= "https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" + results +"?api_key=bf7ec21b-9468-4e70-9019-e836fc5af85d";
		    String json2 = IOUtils.toString(new URL(url2));
		    String champ = jsonParser.parse(json2)
		    		 .getAsJsonObject().get("key")
		    		 .getAsString();
		    //parse out special characters
		    String[] temp;
	           String delimiter ="'";
	           temp = champ.split(delimiter);
	           String output ="";
	           for(String str: temp)
	               output=output+str;
		    //download/save image
	        URL url3 = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + output +"_0.jpg");
			InputStream is = url3.openStream();
			OutputStream os = new FileOutputStream("image.jpg");
			//change wallpaper
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
		     //Set delay 
		      File f = new File("delay.json");
	          if(f.exists() && !f.isDirectory()) {
		      JsonParser parser = new JsonParser();
	      	JsonElement Obj = parser.parse(new FileReader("delay.json"));
	      	int days = 
	      			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
			    		 .getAsJsonObject().get("days").getAsInt() ;
	      	int hours =
	      			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
	      			.getAsJsonObject().get("hours").getAsInt();
	      	int minutes =
	      			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
	      			.getAsJsonObject().get("minutes").getAsInt();
	      	TimeUnit.DAYS.sleep(days);
	      	TimeUnit.HOURS.sleep(hours);
	      	TimeUnit.MINUTES.sleep(minutes);
	      	}
	          else{
	        	  TimeUnit.HOURS.sleep(4);
	          }
			}
		} catch (IOException f) {
	        f.printStackTrace();
		} catch(InterruptedException ex) {
			   Thread.currentThread().interrupt();
			}
		} 
		}
