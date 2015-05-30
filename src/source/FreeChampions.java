package source;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import com.google.gson.*;

public class FreeChampions {
    public static void main(String[] args) {
        try {
        	final TrayIcon trayIcon =
                    new TrayIcon(createImage("CStiny.jpg", "tray icon"));
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
    }
     
    private static void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("CStiny.jpg", "tray icon"));
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
                    		FileWriter file = new FileWriter("mode.json");
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
                        		FileWriter file = new FileWriter("mode.json");
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
                        		FileWriter file = new FileWriter("mode.json");
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
                    	File f = new File("mode.json");
                        if(f.exists() && !f.isDirectory()) {
                        	try{
                        JsonParser parser = new JsonParser();
                    	JsonElement Obj = parser.parse(new FileReader("mode.json"));
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
                    		FileWriter file = new FileWriter("mode.json");
                			file.write(jobj.toString());
                			file.flush();
                			file.close();
                			frame2.dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                			}catch (IOException f) {
                    	        f.printStackTrace();
                        }
                    	}
                    	else if(newskin.isSelected()){
                    		try{
                        		JsonObject jobj = new JsonObject();
                                jobj.addProperty("mode", "newskin");
                        		FileWriter file = new FileWriter("mode.json");
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
                        		FileWriter file = new FileWriter("mode.json");
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
                File f = new File("mode.json");
                if(f.exists() && !f.isDirectory()) {
                	try{
                JsonParser parser = new JsonParser();
            	JsonElement Obj = parser.parse(new FileReader("mode.json"));
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
                    		FileWriter file = new FileWriter("delay.json");
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
                    	File f = new File("delay.json");
                        if(f.exists() && !f.isDirectory()) {
                    	try{
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
                            FileWriter file = new FileWriter("delay.json");
                            	file.write(mainObj.toString());
                            	file.flush();
                            	file.close();
                        	}
                        catch (IOException f) {
                        	f.printStackTrace();
                        	}
                    }
                }); 
                File f = new File("delay.json");
                if(f.exists() && !f.isDirectory()) {
                try{
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
}