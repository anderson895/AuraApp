package aura;

import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * AURA - Login Screen
 */
public class LoginFrame extends javax.swing.JFrame {

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null);
      
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        tfUser = new javax.swing.JTextField();
        lblPass = new javax.swing.JLabel();
        tfPass = new javax.swing.JPasswordField();
        lblMsg = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        lblNoAcct = new javax.swing.JLabel();
        btnRegister = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AURA - Login");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(200, 16, 46));
        lblTitle.setText("Welcome Back!");

        lblSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblSub.setForeground(new java.awt.Color(102, 102, 102));
        lblSub.setText("Sign in to your AURA account");

        lblUser.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblUser.setText("Username");

        lblPass.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPass.setText("Password");

        tfPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPassActionPerformed(evt);
            }
        });

        lblMsg.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblMsg.setForeground(new java.awt.Color(220, 53, 69));
        lblMsg.setText(" ");

        btnLogin.setBackground(new java.awt.Color(200, 16, 46));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("LOGIN");
        btnLogin.setBorderPainted(false);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        lblNoAcct.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblNoAcct.setForeground(new java.awt.Color(102, 102, 102));
        lblNoAcct.setText("No account yet?");

        btnRegister.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(200, 16, 46));
        btnRegister.setText("Register here");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/banner.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(lblMsg))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPass, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPass, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNoAcct, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSub, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(lblTitle)
                .addGap(6, 6, 6)
                .addComponent(lblSub)
                .addGap(24, 24, 24)
                .addComponent(lblUser)
                .addGap(6, 6, 6)
                .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(lblPass)
                .addGap(6, 6, 6)
                .addComponent(tfPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblNoAcct))
                    .addComponent(btnRegister)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMsg))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPassActionPerformed
        doLogin();
    }//GEN-LAST:event_tfPassActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        dispose();
        new RegisterFrame().setVisible(true);
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        doLogin();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void doLogin() {
        String user = tfUser.getText().trim();
        String pass = new String(tfPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            setMsg("Please fill in all fields.", false);
            return;
        }
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT id,username,email,full_name,role FROM users WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("username"),
                                  rs.getString("email"), rs.getString("full_name"),
                                  rs.getString("role"));
                setMsg("Login successful!", true);
                dispose();
                if ("admin".equalsIgnoreCase(u.getRole())) {
                    new AdminDashboardFrame(u).setVisible(true);
                } else {
                    new DashboardFrame(u).setVisible(true);
                }
            } else {
                setMsg("Incorrect username or password.", false);
                tfPass.setText("");
            }
        } catch (SQLException ex) {
            setMsg("DB Error: " + ex.getMessage(), false);
        }
    }

    private void setMsg(String text, boolean ok) {
        lblMsg.setText(text);
        lblMsg.setForeground(ok ? new java.awt.Color(40, 167, 69)
                                : new java.awt.Color(220, 53, 69));
    }

  public static void main(String[] args) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblMsg;
    private javax.swing.JLabel lblNoAcct;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPasswordField tfPass;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
}
