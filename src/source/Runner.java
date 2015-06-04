package source;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

import source.WallpaperChanger.SPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class Runner {
public static void main(String[] args) throws InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(2);
    //ExecutorService freechamp = Executors.newFixedThreadPool(1);
    service.submit(new ModeScanner());
    service.submit(new Tray());
}  
}  
 class Newchamp implements Callable<Object> {
	 public Object call() throws Exception {
		 System.out.println("New Champion Cycle");
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
			      File f = new File("json/delay.json");
		          if(f.exists() && !f.isDirectory()) {
			      JsonParser parser = new JsonParser();
		      	JsonElement Obj = parser.parse(new FileReader("json/delay.json"));
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
		 return null; 
	 }
 }
 class Newskin implements Callable<Object> {
	 public Object call() throws Exception {
		 System.out.println("New Skin Cycle");
		 while(true){
		 try {	
				String html = Jsoup.connect("http://leagueoflegends.wikia.com/wiki/League_of_Legends_Wiki/New_skins").get().html();
				for(int x = 2; x <= 14; x = x+2) {
					System.out.println(x);
					String parse = Jsoup.parse(html,"ISO-8859-1").select("body").get(0).select("div").get(40).select("div").get(5).select("div").get(6).select("div").get(3).select("div").get(x).html();	
				String[] parts = parse.split("/wiki/");
				String part2 = parts[1]; 
				String[] parted = part2.split("/");
				String part3 = parted[0];
				String[] temp;
			    String delimiter ="_";
			    temp = part3.split(delimiter);
			    String output ="";
			    for(String str: temp)
			        output=output+str;
				System.out.println(output);
				String url = "https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion?champData=skins&api_key=bf7ec21b-9468-4e70-9019-e836fc5af85d";
			    String json = IOUtils.toString(new URL(url));
			    JsonParser jsonParser = new JsonParser();
			    JsonElement results = jsonParser.parse(json)
			            .getAsJsonObject().get("data") 
			            .getAsJsonObject().get(output)
			    		.getAsJsonObject().get("skins");
			    JsonArray skins = results.getAsJsonArray();
			    int size = skins.size();
			    int realsize = size - 1;
			    JsonElement getpart = skins.get(realsize);
			    int finalnum = getpart.getAsJsonObject().get("num").getAsInt();
			    URL url2 = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + output +"_" + finalnum + ".jpg");
					InputStream is = url2.openStream();
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
				      File f = new File("json/delay.json");
			          if(f.exists() && !f.isDirectory()) {
				      JsonParser parser = new JsonParser();
			      	JsonElement Obj = parser.parse(new FileReader("json/delay.json"));
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
			        	  TimeUnit.SECONDS.sleep(4);
			          }
				
				}
				} catch (IOException f) {
			        f.printStackTrace(); 
				} catch(InterruptedException ex) {
				   Thread.currentThread().interrupt();
				}
		 return null;
		 }
	 }
 }
 class Freechamp implements Callable<Object> {
	 public Object call() throws Exception {
		 while(true){
		 try{
			 System.out.println("Free Champion Cycle");
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
			      File f = new File("json/delay.json");
		          if(f.exists() && !f.isDirectory()) {
			      JsonParser parser = new JsonParser();
		      	JsonElement Obj = parser.parse(new FileReader("json/delay.json"));
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
		        	  TimeUnit.SECONDS.sleep(10);
		          }
				}
			} catch (IOException f) {
		        f.printStackTrace();
			} catch(InterruptedException ex) {
				   Thread.currentThread().interrupt();
				}
		return null;
			}
	 }
 }

 class ModeScanner implements Callable<Object> {
	 ExecutorService newchamp = Executors.newFixedThreadPool(1);
	 ExecutorService freechamp = Executors.newFixedThreadPool(1);
	 ExecutorService newskin = Executors.newFixedThreadPool(1);
    @Override
    public Object call() throws Exception {
    	File f = new File("json/mode.json");
        if(f.exists() && !f.isDirectory()) {
        	JsonParser parser = new JsonParser();
        	JsonElement Obj = parser.parse(new FileReader("json/mode.json"));
        	 String mode = 
        			Obj.getAsJsonObject().get("mode").getAsString();
        System.out.println(mode);
        if(mode.equals("newchamp")){
        	newchamp.submit(new Newchamp());
        }
        if(mode.equals("freechamp")){
        	freechamp.submit(new Freechamp());
        }
        if(mode.equals("newskin")){
        	newskin.submit(new Newskin());
        }
        }
        else{
        	newchamp.submit(new Newchamp());
        }
    	 WatchService watcher = FileSystems.getDefault().newWatchService();
         Path dir = Paths.get("json");
         dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
          
         System.out.println("Watch Service registered for dir: " + dir.getFileName());
          
         while (true) {
             WatchKey key = null;
             try {
                 key = watcher.take();
             } catch (InterruptedException ex) {
                 //return;
             }
             //stop from sending two messages
             TimeUnit.SECONDS.sleep(1);
             for (WatchEvent<?> event : key.pollEvents()) {
                 WatchEvent.Kind<?> kind = event.kind();
                  
                 @SuppressWarnings("unchecked")
                 WatchEvent<Path> ev = (WatchEvent<Path>) event;
                 Path fileName = ev.context();
                  
               //  System.out.println(kind.name() + ": " + fileName);
                 if (kind == ENTRY_MODIFY &&  fileName.toString().equals("mode.json") || fileName.toString().equals("delay.json")) {
                	 System.out.println("File Changed");
                	 newchamp.shutdownNow();
                	 freechamp.shutdownNow();
                	 newskin.shutdownNow();
                	 ExecutorService starter = Executors.newFixedThreadPool(1);
                	 starter.submit(new Starter());
                 }
          
                /* 
                 if (kind == ENTRY_CREATE &&
                 		fileName.toString().equals("delay.json")) {
                 	System.out.println("Delay File Created");
                 	
             }
             */
             }
             boolean valid = key.reset();
             if (!valid) {
                 break;
             }
                  
   //catch (IOException e) {
 //	 System.err.println(e);
 // System.out.println("Thread " +  threadName + " exiting.");

        //return null;
    }
		return null;
    }
 }
    /*
    protected void scan(File path, int deepth) {
        if (deepth < 15) {
            System.out.println("Scanning " + path + " at a deepth of " + deepth);

            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    scan(file, ++deepth);
                }
            }
        }
    }
}
*/
 class Starter implements Callable<Object> {
	 ExecutorService newchamp = Executors.newFixedThreadPool(1);
	 ExecutorService freechamp = Executors.newFixedThreadPool(1);
	 ExecutorService newskin = Executors.newFixedThreadPool(1);
	    @Override
	    public Object call() throws Exception {
	    	File f = new File("json/mode.json");
	        if(f.exists() && !f.isDirectory()) {
	        	JsonParser parser = new JsonParser();
	        	JsonElement Obj = parser.parse(new FileReader("json/mode.json"));
	        	 String mode = 
	        			Obj.getAsJsonObject().get("mode").getAsString();
	        System.out.println(mode);
	        if(mode.equals("newchamp")){
	        	newchamp.submit(new Newchamp());
	        }
	        if(mode.equals("freechamp")){
	        	freechamp.submit(new Freechamp());
	        }
	        if(mode.equals("newskin")){
	        	newskin.submit(new Newskin());
	        }
	        }
	        else{
	        	newchamp.submit(new Newchamp());
	        }
	    	return null;
	    	
	    }
 }
 class Shutdown implements Callable<Object> {
	 public Object call() throws Exception {

		 return null; 
	 }
 }
 class Tray implements Callable<Object> {
    @Override
    public Object call() throws Exception {
    	try {
    		Image image = Toolkit.getDefaultToolkit().getImage("images/tray.gif");
        	final TrayIcon trayIcon =
                    new TrayIcon(image, "League Wallpaper");
        	final JPopupMenu jpopup = new JPopupMenu();

            JMenuItem javaCupMI = new JMenuItem("Example", new ImageIcon("javacup.gif"));
            jpopup.add(javaCupMI);

            jpopup.addSeparator();

            JMenuItem exitMI = new JMenuItem("Exit");
            jpopup.add(exitMI);
            trayIcon.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        jpopup.setLocation(e.getX(), e.getY());
                        jpopup.setInvoker(jpopup);
                        jpopup.setVisible(true);
                    }
                }
            });
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } 

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
		return null;
    }
    private static void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        Image image = Toolkit.getDefaultToolkit().getImage("json/CStiny.jpg");
        final TrayIcon trayIcon =
                new TrayIcon(image, "tray icon");
        final SystemTray tray = SystemTray.getSystemTray();
         
        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem modeItem = new MenuItem("Change Mode");
        MenuItem delayItem = new MenuItem("Set Delay");
        
        //Add components to popup menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(modeItem);
        popup.add(delayItem);
        popup.addSeparator();
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
         
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
         
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });        
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
            }
        });
        modeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	final JFrame frame2 = new JFrame("Modes");

                JPanel panel = new JPanel(new GridLayout(0, 1));
          
                ButtonGroup group = new ButtonGroup();
                final JRadioButton newchamp = new JRadioButton("Newest Champion");
                final JRadioButton newskin = new JRadioButton("Newest Skins");
                final JRadioButton freechamp = new JRadioButton("Free Champion Rotation");

                ActionListener sliceActionListener = new ActionListener() {
                  public void actionPerformed(ActionEvent actionEvent) {
                   // AbstractButton aButton = (AbstractButton) actionEvent.getSource();
                    //System.out.println("Selected: " + aButton.getText());
                  }
                };
                ActionListener testActionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                      //AbstractButton bButton = (AbstractButton) actionEvent.getSource();
                      //System.out.println("test");
                    }
                  };
                panel.add(newchamp);
                group.add(newchamp);
                panel.add(newskin);
                group.add(newskin);
                panel.add(freechamp);
                group.add(freechamp);

                newchamp.addActionListener(sliceActionListener);
                newskin.addActionListener(testActionListener);
                freechamp.addActionListener(sliceActionListener);

                JButton ok;
                ok = new JButton("OK");
                ok.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	if(newchamp.isSelected()){
                    		try{
                    		JsonObject jobj = new JsonObject();
                            jobj.addProperty("mode", "newchamp");
                    		FileWriter file = new FileWriter("json/mode.json");
                			file.write(jobj.toString());
                			file.flush();
                			file.close();
                			}catch (IOException f) {
                    	        f.printStackTrace();
                        }
                    	}
                    	else if(newskin.isSelected()){
                    		try{
                        		JsonObject jobj = new JsonObject();
                                jobj.addProperty("mode", "newskin");
                        		FileWriter file = new FileWriter("json/mode.json");
                    			file.write(jobj.toString());
                    			file.flush();
                    			file.close();
                    			}catch (IOException f) {
                        	        f.printStackTrace();}
                    	}
                    	else if(freechamp.isSelected()){
                    		try{
                        		JsonObject jobj = new JsonObject();
                                jobj.addProperty("mode", "freechamp");
                        		FileWriter file = new FileWriter("json/mode.json");
                    			file.write(jobj.toString());
                    			file.flush();
                    			file.close();
                    			}catch (IOException f) {
                        	        f.printStackTrace();}
                    	}
                    	frame2.dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                    }
                });  
                JButton cancel;
                cancel = new JButton("Cancel");
                cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	File f = new File("json/mode.json");
                        if(f.exists() && !f.isDirectory()) {
                        	try{
                        JsonParser parser = new JsonParser();
                    	JsonElement Obj = parser.parse(new FileReader("json/mode.json"));
                    	 String mode = 
                    			Obj.getAsJsonObject().
            		    		 getAsJsonObject().get("mode").getAsString();
                    		if(mode.equals("newskin")){
                    		newskin.setSelected(true);
                    		}
                    		if(mode.equals("newchamp")){
                        		newchamp.setSelected(true);
                        		}
                    		if(mode.equals("freechamp")){
                    			freechamp.setSelected(true);
                    		}
                    	}
                    	catch (IOException g) {
                	        g.printStackTrace();
                    	}
                    }
                }
                });  
                JButton apply;
                apply = new JButton("Apply");
                apply.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	if(newchamp.isSelected()){
                    		try{
                    		JsonObject jobj = new JsonObject();
                            jobj.addProperty("mode", "newchamp");
                    		FileWriter file = new FileWriter("json/mode.json");
                			file.write(jobj.toString());
                			file.flush();
                			file.close();
                			}catch (IOException f) {
                    	        f.printStackTrace();
                        }
                    	}
                    	else if(newskin.isSelected()){
                    		try{
                        		JsonObject jobj = new JsonObject();
                                jobj.addProperty("mode", "newskin");
                        		FileWriter file = new FileWriter("json/mode.json");
                    			file.write(jobj.toString());
                    			file.flush();
                    			file.close();
                    			}catch (IOException f) {
                        	        f.printStackTrace();}
                    	}
                    	else if(freechamp.isSelected()){
                    		try{
                        		JsonObject jobj = new JsonObject();
                                jobj.addProperty("mode", "freechamp");
                        		FileWriter file = new FileWriter("json/mode.json");
                    			file.write(jobj.toString());
                    			file.flush();
                    			file.close();
                    			}catch (IOException f) {
                        	        f.printStackTrace();}
                    	}
                    }
                }); 
                
                frame2.add(panel);
                frame2.setSize(200, 200);
                frame2.setResizable(false);
                frame2.setVisible(true);
                panel.add(ok);
                panel.add(cancel);
                panel.add(apply);
                File f = new File("json/mode.json");
                if(f.exists() && !f.isDirectory()) {
                	try{
                JsonParser parser = new JsonParser();
            	JsonElement Obj = parser.parse(new FileReader("json/mode.json"));
            	 String mode = 
            			Obj.getAsJsonObject().
    		    		 getAsJsonObject().get("mode").getAsString();
            		if(mode.equals("newskin")){
            		newskin.setSelected(true);
            		}
            		if(mode.equals("newchamp")){
                		newchamp.setSelected(true);
                		}
            		if(mode.equals("freechamp")){
            			freechamp.setSelected(true);
            		}
            	}
            	catch (IOException g) {
        	        g.printStackTrace();
            }
                }
            }
        });
        delayItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	final JFrame frame = new JFrame("Change Wallpaper Delay");
            	 
                // Spinners for delay selection
                SpinnerNumberModel snm = new SpinnerNumberModel(
                        new Integer(0),
                        new Integer(0),
                        new Integer(31),
                        new Integer(1)
                );
                final JSpinner spnNumber = new JSpinner(snm);
         
                
                 SpinnerNumberModel snm2 = new SpinnerNumberModel(
                        new Integer(0),
                        new Integer(0),
                        new Integer(23),
                        new Integer(1)
                );
                final JSpinner spnNumber2 = new JSpinner(snm2);
                
                SpinnerNumberModel snm3 = new SpinnerNumberModel(
                        new Integer(0),
                        new Integer(0),
                        new Integer(59),
                        new Integer(1)
                );
                final JSpinner spnNumber3 = new JSpinner(snm3);
                //Setup spinners/window
                frame.setSize(300, 85);
                frame.setResizable(false);
                
                Container cont = frame.getContentPane();
         
                cont.setLayout(new FlowLayout());
                cont.add(new JLabel("Days:"));
                cont.add(spnNumber);
         
                cont.add(new JLabel("Hours:"));
                cont.add(spnNumber2);
         
                cont.add(new JLabel("Minutes:"));
                cont.add(spnNumber3);
            	
                JButton ok;
                ok = new JButton("OK");
                ok.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	try{
                    		final Integer DayValue = (Integer)spnNumber.getValue();
                    		final Integer HourValue = (Integer)spnNumber2.getValue();
                    		final Integer MinuteValue = (Integer)spnNumber3.getValue();
                    		JsonObject jobj = new JsonObject();
                    		jobj.addProperty("days", DayValue);
                    		jobj.addProperty("hours", HourValue);
                    		jobj.addProperty("minutes", MinuteValue);
                    		JsonArray ja = new JsonArray();
                    		ja.add(jobj);
                    		JsonObject mainObj = new JsonObject();
                    		mainObj.add("time", ja);
                    		FileWriter file = new FileWriter("json/delay.json");
                    			file.write(mainObj.toString());
                    			file.flush();
                    			file.close();
                    		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    	}
                    	catch (IOException f) {
                	        f.printStackTrace();
                    }
                    	}
                });  
                JButton cancel;
                cancel = new JButton("Cancel");
                cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	File f = new File("json/delay.json");
                        if(f.exists() && !f.isDirectory()) {
                    	try{
                    	JsonParser parser = new JsonParser();
                    	JsonElement Obj = parser.parse(new FileReader("json/delay.json"));
                    	int days = 
                    			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
            		    		 .getAsJsonObject().get("days").getAsInt() ;
                    	int hours =
                    			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
                    			.getAsJsonObject().get("hours").getAsInt();
                    	int minutes =
                    			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
                    			.getAsJsonObject().get("minutes").getAsInt();
                    	spnNumber.setValue(days);
                    	spnNumber2.setValue(hours);
                    	spnNumber3.setValue(minutes);
                    	}
                    	catch (IOException ex) {
                    		                System.err.println("An IOException was caught!");
                    		                ex.printStackTrace();
                    	}
                    	} 
                    }
                });  
                JButton apply;
                apply = new JButton("Apply");
                apply.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	try{ 
                        	final Integer DayValue = (Integer)spnNumber.getValue();
                        	final Integer HourValue = (Integer)spnNumber2.getValue();
                        	final Integer MinuteValue = (Integer)spnNumber3.getValue();
                        	JsonObject jobj = new JsonObject();
                            jobj.addProperty("days", DayValue);
                            jobj.addProperty("hours", HourValue);
                            jobj.addProperty("minutes", MinuteValue);
                            JsonArray ja = new JsonArray();
                            ja.add(jobj);
                            JsonObject mainObj = new JsonObject();
                            mainObj.add("time", ja);
                            FileWriter file = new FileWriter("json/delay.json");
                            	file.write(mainObj.toString());
                            	file.flush();
                            	file.close();
                        	}
                        catch (IOException f) {
                        	f.printStackTrace();
                        	}
                    }
                }); 
                File f = new File("json/delay.json");
                if(f.exists() && !f.isDirectory()) {
                try{
                JsonParser parser = new JsonParser();
            	JsonElement Obj = parser.parse(new FileReader("json/delay.json"));
                int days = 
            			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
    		    		 .getAsJsonObject().get("days").getAsInt() ;
            	int hours =
            			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
            			.getAsJsonObject().get("hours").getAsInt();
            	int minutes =
            			Obj.getAsJsonObject().getAsJsonArray("time").get(0)
            			.getAsJsonObject().get("minutes").getAsInt();
            	spnNumber.setValue(days);
            	spnNumber2.setValue(hours);
            	spnNumber3.setValue(minutes);
            }catch (IOException ex) {
                System.err.println("An IOException was caught!");
                ex.printStackTrace();
            }
                }
                
                cont.add(ok);
                cont.add(cancel);
                cont.add(apply);
                frame.setVisible(true);
            }     	
        });
         
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }
     
    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = FreeChampions.class.getResource(path);
         
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
{
        //return null;
    }
}