/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.barmeier.jovpn;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author barmeier
 */
public class AppDialog extends javax.swing.JFrame implements FileFilter {

    TrayIcon trayIcon;

    /**
     * Creates new form Main
     */
    public AppDialog() {
        initComponents();
        fillConnections("/etc/openvpn");
        this.setLocationRelativeTo(getRootPane());
        URL imgUrl = getClass().getResource("/g3184.png");
        ImageIcon icon = new ImageIcon(imgUrl);
        setIconImage(icon.getImage());

    }

    private void fillConnections(String path) {
        File configDir = new File(path);
        File configs[] = configDir.listFiles(this);
        for (File config : configs) {
            cbConfigurations.addItem(config.getName());
        }
        btQuit.setVisible(false);
    }

    public void setTrayIcon(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    public TrayIcon getTrayIcon() {
        return (trayIcon);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cbConfigurations = new javax.swing.JComboBox();
        btConnect = new javax.swing.JButton();
        lbTitle = new javax.swing.JLabel();
        btQuit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JOvpn");
        setBackground(new java.awt.Color(254, 254, 254));
        setResizable(false);

        jLabel1.setText("Select connection:");

        btConnect.setText("Connect");
        btConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectActionPerformed(evt);
            }
        });

        lbTitle.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g3184.png"))); // NOI18N
        lbTitle.setText("  JOvpn");
        lbTitle.setToolTipText("");

        btQuit.setText("Quit");
        btQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btQuitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTitle)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btConnect))
                            .addComponent(cbConfigurations, 0, 328, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbConfigurations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btConnect)
                    .addComponent(btQuit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 0, 491, 175);
    }// </editor-fold>//GEN-END:initComponents

    private void btConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectActionPerformed
        // Start ovpn listener thread
        OvpnListener ovpnListener = new OvpnListener(this);
        ovpnListener.start();
        try {
            Runtime.getRuntime().exec("/usr/sbin/openvpn --management localhost "
                    + "9999 --management-client --management-query-passwords "
                    + "--config /etc/openvpn/" + (String) cbConfigurations.getSelectedItem());
        } catch (IOException iex) {
            iex.printStackTrace();
        }
        btConnect.setVisible(false);
        btQuit.setVisible(true);
        cbConfigurations.setEnabled(false);
        this.dispose();
        // Start ovpn daemon
    }//GEN-LAST:event_btConnectActionPerformed

    private void btQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btQuitActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btQuitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AppDialog frame = new AppDialog();
                Image image = Toolkit.getDefaultToolkit().getImage(
                        getClass().getResource("/g3184.png"));

                final JPopupMenu jpopup = new JPopupMenu();
                ActionListener menuActionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                };

                JMenuItem exitMI = new JMenuItem("Exit");

                exitMI.addActionListener(menuActionListener);
                jpopup.add(exitMI);
                JMenuItem javaCupMI3 = new JMenuItem("Example");//, new ImageIcon("javacup.gif"));
                jpopup.add(javaCupMI3);

                TrayIcon trayIcon = new TrayIcon(image, null, null);
                trayIcon.setImageAutoSize(true);
                trayIcon.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            jpopup.setLocation(e.getX(), e.getY());
                            jpopup.setInvoker(jpopup);
                            jpopup.setVisible(true);
                        }
                    }

                    public void mousePressed(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            jpopup.setLocation(e.getX(), e.getY());
                            jpopup.setInvoker(jpopup);
                            jpopup.setVisible(true);
                        }
                    }
                });
                SystemTray tray = SystemTray.getSystemTray();
                try {
                    tray.add(trayIcon);
                } catch (AWTException ex) {
                    Logger.getLogger(AppDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.setTrayIcon(trayIcon);
                frame.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btConnect;
    private javax.swing.JButton btQuit;
    private javax.swing.JComboBox cbConfigurations;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbTitle;
    // End of variables declaration//GEN-END:variables

    public boolean accept(File pathname) {
        return pathname.getName().endsWith(".conf");
    }
}
