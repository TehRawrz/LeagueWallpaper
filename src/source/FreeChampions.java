package source;
/*
 * TrayIconDemo.java
 */
 
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
 
public class FreeChampions {
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
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
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
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
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the Mode menu item");
            }
        });
        delayItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the Delay menu item");
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