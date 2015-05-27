package source;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
 
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
            	JFrame frame = new JFrame("Modes");

                JPanel panel = new JPanel(new GridLayout(0, 1));
          
                ButtonGroup group = new ButtonGroup();
                JRadioButton aRadioButton = new JRadioButton("Newest Champion");
                JRadioButton bRadioButton = new JRadioButton("Newest Skins");
                JRadioButton cRadioButton = new JRadioButton("Free Champion Rotation");

                ActionListener sliceActionListener = new ActionListener() {
                  public void actionPerformed(ActionEvent actionEvent) {
                    AbstractButton aButton = (AbstractButton) actionEvent.getSource();
                    System.out.println("Selected: " + aButton.getText());
                  }
                };

                panel.add(aRadioButton);
                group.add(aRadioButton);
                panel.add(bRadioButton);
                group.add(bRadioButton);
                panel.add(cRadioButton);
                group.add(cRadioButton);

                aRadioButton.addActionListener(sliceActionListener);
                bRadioButton.addActionListener(sliceActionListener);
                cRadioButton.addActionListener(sliceActionListener);

                JButton ok;
                ok = new JButton("OK");
                ok.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null,
                                "OK");
                    }
                });  
                JButton cancel;
                cancel = new JButton("Cancel");
                cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null,
                                "cancelled");
                    }
                });  
                JButton apply;
                apply = new JButton("Apply");
                apply.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null,
                                "Applied");
                    }
                }); 
                frame.add(panel);
                frame.setSize(200, 200);
                frame.setResizable(false);
                frame.setVisible(true);
                panel.add(ok);
                panel.add(cancel);
                panel.add(apply);
            }
        });
        delayItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JFrame frame = new JFrame("Change Wallpaper Delay");
            	 
                // Spinner with number
                SpinnerNumberModel snm = new SpinnerNumberModel(
                        new Integer(0),
                        new Integer(0),
                        new Integer(100),
                        new Integer(5)
                );
                JSpinner spnNumber = new JSpinner(snm);
         
                // Spinner with Dates
                SpinnerNumberModel snm2 = new SpinnerNumberModel(
                        new Integer(0),
                        new Integer(0),
                        new Integer(100),
                        new Integer(5)
                );
                JSpinner spnNumber2 = new JSpinner(snm2);
                
                SpinnerNumberModel snm3 = new SpinnerNumberModel(
                        new Integer(0),
                        new Integer(0),
                        new Integer(100),
                        new Integer(5)
                );
                JSpinner spnNumber3 = new JSpinner(snm3);
         
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
                        JOptionPane.showMessageDialog(null,
                                "OK");
                    }
                });  
                JButton cancel;
                cancel = new JButton("Cancel");
                cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null,
                                "cancelled");
                    }
                });  
                JButton apply;
                apply = new JButton("Apply");
                apply.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null,
                                "Applied");
                    }
                }); 
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