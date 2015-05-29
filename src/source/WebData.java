package source;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;

import java.io.*;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

import source.WallpaperChanger.SPI;


public class WebData {

	public static void main(String[] args) {
		try {

        List<Date> dater = new ArrayList<>();
        while(true){
        String array = "https://na.api.pvp.net/api/lol/na/v1.2/champion?api_key=bf7ec21b-9468-4e70-9019-e836fc5af85d";
        String json = IOUtils.toString(new URL(array));
            JsonParser jsonParser = new JsonParser();
            JsonElement results = jsonParser.parse(json)
            		.getAsJsonObject().get("champions");
            JsonArray skins = results.getAsJsonArray();
        int size = skins.size();
        final long now = System.currentTimeMillis();
            String html = Jsoup.connect("http://leagueoflegends.wikia.com/wiki/List_of_champions").get().html();
            //grab date and name. tr is position in table by alphabet order
            for(int x = 1; x <= size; x = x+1) {
                String date = Jsoup.parse(html,"ISO-8859-1").select("table").select("tbody").get(1).select("tr").get(x).select("td").get(7).text();
                //Format string data into dates
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date newdate = formatter.parse(date);
                dater.add(newdate);
            }
            //Find closest date to current time
            Date closest = Collections.min(dater, new Comparator<Date>() {
                public int compare(Date d1, Date d2) {
                    long diff1 = Math.abs(d1.getTime() - now);
                    long diff2 = Math.abs(d2.getTime() - now);
                    return diff1 < diff2 ? -1 : 1;
                }
            });
           int index = dater.indexOf(closest);
           int indexed = index + 1;
       	String html2 = Jsoup.connect("http://leagueoflegends.wikia.com/wiki/List_of_champions").get().html();
       	 String name = Jsoup.parse(html2,"ISO-8859-1").select("table").select("tbody").get(1).select("tr").get(indexed).select("td").get(0).select("a").get(1).text();
           String[] temp;
           String delimiter ="'";
           temp = name.split(delimiter);
           String output ="";
           for(String str: temp)
               output=output+str;
           URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + output +"_0.jpg");
   		InputStream is = url.openStream();
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
        } catch(InterruptedException ex) {
     	   Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        catch (ParseException e)
        {
        e.printStackTrace();
    }
}
}            