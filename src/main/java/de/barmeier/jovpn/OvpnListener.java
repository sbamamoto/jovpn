/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.barmeier.jovpn;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author barmeier
 */
public class OvpnListener extends Thread {

    AppDialog main;
    StatusDialog statusDialog;
    
    public OvpnListener(AppDialog main) {
        this.main = main;
        statusDialog = new StatusDialog(null, false);
    }
    static char[] passwd = new char[5];

    @Override
    public void run() {
        try {
            int port = 9999;
            java.net.ServerSocket serverSocket = new java.net.ServerSocket(port);
            java.net.Socket client = serverSocket.accept();
            while (true) {
                String nachricht = leseNachricht(client);
                System.out.println(nachricht);
                if (nachricht.contains("PASSWORD:Need 'Private Key'")) {
                    System.out.println("*** Sending password");
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                Passwd pwdIn = new Passwd(null, true);
                                pwdIn.setVisible(true);
                                OvpnListener.passwd = pwdIn.getPassword();
                                statusDialog.setVisible(true);
                            }
                        });
                    } catch (InterruptedException ex) {
                        Logger.getLogger(OvpnListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(OvpnListener.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    schreibeNachricht(client, "password \"Private Key\" "
                            + new String(passwd) + "\n");
                } else if (nachricht.contains("PASSWORD:Verification Failed")) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JOptionPane.showMessageDialog(null, "Password incorrect.");
                        }
                    });
                }
                else if (nachricht.contains("CONNECTED,SUCCESS")) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            statusDialog.dispose();
                            JOptionPane.showMessageDialog(null, "CONNECTED");
                            URL imgUrl = getClass().getResource("/green.png");
                            ImageIcon icon = new ImageIcon(imgUrl);
                            main.getTrayIcon().setImage(icon.getImage());
                            main.setIconImage(icon.getImage());
                        }
                    });
                    System.out.println("Connected");
                } else {
                    final String statusMessage = nachricht;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (statusMessage.contains("AUTH")) {
                                statusDialog.setStatus("Authenticating ...");
                            }                            
                            else if (statusMessage.contains("WAIT")) {
                                statusDialog.setStatus("Waiting ...");
                            }
                            else if (statusMessage.contains("GET_CONFIG")) {
                                statusDialog.setStatus("Configuring ..");
                            }
                       }
                    });
                    schreibeNachricht(client, "state\n");
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(OvpnListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(OvpnListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("TERMINATED");
    }

    String leseNachricht(java.net.Socket socket) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(
                new InputStreamReader(
                socket.getInputStream()));
        char[] buffer = new char[200];
        int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert bis Nachricht empfangen
        String nachricht = new String(buffer, 0, anzahlZeichen);
        return nachricht;
    }

    void schreibeNachricht(java.net.Socket socket, String nachricht) throws IOException {
        PrintWriter printWriter =
                new PrintWriter(
                new OutputStreamWriter(
                socket.getOutputStream()));
        printWriter.print(nachricht);
        printWriter.flush();
    }
}
