package source;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import source.WallpaperChanger.SPI;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;

public class FreeChamps {
	public static void main(String[] args) {
		try{
			String url= "https://na.api.pvp.net/api/lol/na/v1.2/champion?freeToPlay=true&api_key=bf7ec21b-9468-4e70-9019-e836fc5af85d";
			String json = IOUtils.toString(new URL(url));
			for(int x=0; x <= 9; x = x+1){
		    JsonParser jsonParser = new JsonParser();
		    JsonElement results = jsonParser.parse(json)
		    		 .getAsJsonObject().getAsJsonArray("champions").get(x)
		    		 .getAsJsonObject().get("id");
		    String url2= "https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" + results +"?api_key=bf7ec21b-9468-4e70-9019-e836fc5af85d";
		    String json2 = IOUtils.toString(new URL(url2));
		    String champ = jsonParser.parse(json2)
		    		 .getAsJsonObject().get("key")
		    		 .getAsString();
		    String[] temp;
	           String delimiter ="'";
	           temp = champ.split(delimiter);
	           String output ="";
	           for(String str: temp)
	               output=output+str;
		    URL url3 = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + output +"_0.jpg");
			InputStream is = url3.openStream();
			OutputStream os = new FileOutputStream("image.jpg");
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
		    System.out.print(champ);
		    System.out.println();
			}
		} catch (IOException f) {
	        f.printStackTrace();
		} catch(InterruptedException ex) {
			   Thread.currentThread().interrupt();
			}
		} 
		}
