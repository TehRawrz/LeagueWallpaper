package source;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
 
//import java.io.IOException;

public class FreeChampions {
	public static void main(String[] args) {
		try{
			if (SystemTray.isSupported()) {
			    // get the SystemTray instance
			    SystemTray tray = SystemTray.getSystemTray();
			    // load an image
			    //Image image = Toolkit.getDefaultToolkit().getImage("CStiny.jpg");
			    // create a action listener to listen for default action executed on the tray icon
			    ActionListener listener = new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            // execute default action of the application
			            // ...
			        	 final JFrame mainFrame;
			        	  final JLabel headerLabel;
			        	   final JLabel statusLabel;
			        	   final JPanel controlPanel;
			        	 mainFrame = new JFrame("League Desktop");
			             mainFrame.setSize(400,400);
			             mainFrame.setLayout(new GridLayout(3, 1));
			             mainFrame.addWindowListener(new WindowAdapter() {
			                public void windowClosing(WindowEvent windowEvent){
			                   System.exit(0);
			                }        
			             });    
			             headerLabel = new JLabel("", JLabel.CENTER);        
			             statusLabel = new JLabel("",JLabel.CENTER);    

			             statusLabel.setSize(350,100);

			             controlPanel = new JPanel();
			             controlPanel.setLayout(new FlowLayout());

			             mainFrame.add(headerLabel);
			             mainFrame.add(controlPanel);
			             mainFrame.add(statusLabel);
			             mainFrame.setVisible(true);
			             headerLabel.setText("Set Delay Time:"); 
			             SpinnerModel spinnerModel =
			                new SpinnerNumberModel(10, //initial value
			                   0, //min
			                   23, //max
			                   1);//step
			             final JSpinner spinner = new JSpinner(spinnerModel);
			             spinner.addChangeListener(new ChangeListener() {
			                public void stateChanged(ChangeEvent e) {
			                   statusLabel.setText("Value : " 
			                   + ((JSpinner)e.getSource()).getValue());
			                  //int n = ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();
			                }
			                });
			                   SpinnerModel spinnerModel2 =
			            		 new SpinnerNumberModel (10,
			            				 0,
			            				 59,
			            				 1);
			                
			             final JSpinner spinner2 = new JSpinner(spinnerModel2);
			             spinner2.addChangeListener(new ChangeListener() {
			            	public void stateChanged(ChangeEvent f){
			            	System.out.print(((JSpinner)f.getSource()).getValue());
			            		//	 statusLabel.setText("Value : " 
						           //        + ((JSpinner)f.getSource()).getValue());
			            	 }  
			             });
			             SpinnerModel spinnerModel3 =
			            		 new SpinnerNumberModel (10,
			            				 0,
			            				 59,
			            				 1);
			                
			             final JSpinner spinner3 = new JSpinner(spinnerModel3);
			             spinner3.addChangeListener(new ChangeListener() {
			            	public void stateChanged(ChangeEvent g){
			            		Integer currentValue = (Integer)spinner3.getValue();
			            		System.out.print(currentValue);
			            		//	 statusLabel.setText("Value : " 
						           //        + ((JSpinner)f.getSource()).getValue());
			            	 }  
			             });
			             
			             controlPanel.add(spinner);
			             controlPanel.add(spinner2);
			             controlPanel.add(spinner3);
			             mainFrame.setVisible(true);
			        }
			    };
			    // create a pop up menu
			    // create menu item for the default action
			    //MenuItem defaultItem = new MenuItem("...");
			    //defaultItem.addActionListener(listener);
			    //menu.add(defaultItem);
			    // a group of radio button menu items
		        //menu.addSeparator();
			    JPopupMenu menu;
		        //JMenu  submenu;
		        JRadioButtonMenuItem rbMenuItem;
		        menu = new JPopupMenu("A Menu");
		        ButtonGroup group = new ButtonGroup();
		        rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		        rbMenuItem.setSelected(true);
		        rbMenuItem.setMnemonic(KeyEvent.VK_R);
		        group.add(rbMenuItem);
		        rbMenuItem.addActionListener(listener);
		        menu.add(rbMenuItem);
		        rbMenuItem = new JRadioButtonMenuItem("Another one");
		        rbMenuItem.setMnemonic(KeyEvent.VK_O);
		        group.add(rbMenuItem);
		        rbMenuItem.addActionListener(listener);
		        menu.add(rbMenuItem);
			    /// ... add other items
			    // construct a TrayIcon
		       // ImageIcon i = new ImageIcon("CSTiny.jpg");
		        //ImageIcon i = new ImageIcon(Tray.class.getResource("images/duke.gif"));
		        TrayIcon trayIcon = new TrayIcon(null);
			    // set the TrayIcon properties
			    trayIcon.addActionListener(listener);
			    // ...
			    // add the tray image
			    try {
			        tray.add(trayIcon);
			    } catch (AWTException e) {
			        System.err.println(e);
			    }
			    // ...
			} else {
			    // disable tray option in your application or
			    // perform other actions
			}
		} finally {
	}
	}
}
			// ...