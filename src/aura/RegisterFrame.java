package aura;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 * AURA - Registration Screen
 */
public class RegisterFrame extends javax.swing.JFrame {

    public RegisterFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        lblFullName = new javax.swing.JLabel();
        tfFullName = new javax.swing.JTextField();
        lblUsername = new javax.swing.JLabel();
        tfUsername = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        tfEmail = new javax.swing.JTextField();
        lblPass = new javax.swing.JLabel();
        tfPass = new javax.swing.JPasswordField();
        lblConfirm = new javax.swing.JLabel();
        tfConfirm = new javax.swing.JPasswordField();
        lblMsg = new javax.swing.JLabel();
        btnRegister = new javax.swing.JButton();
        lblHaveAcct = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AURA - Create Account");
        setResizable(false);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(200, 16, 46));
        lblTitle.setText("Create Account");

        lblSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblSub.setForeground(new java.awt.Color(102, 102, 102));
        lblSub.setText("Fill in your details to register");

        lblFullName.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblFullName.setText("Full Name");

        lblUsername.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblUsername.setText("Username");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEmail.setText("Email Address");

        lblPass.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPass.setText("Password");

        lblConfirm.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblConfirm.setText("Confirm Password");

        lblMsg.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblMsg.setForeground(new java.awt.Color(220, 53, 69));
        lblMsg.setText(" ");

        btnRegister.setBackground(new java.awt.Color(200, 16, 46));
        btnRegister.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(255, 255, 255));
        btnRegister.setText("REGISTER");
        btnRegister.setBorderPainted(false);
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        lblHaveAcct.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblHaveAcct.setForeground(new java.awt.Color(102, 102, 102));
        lblHaveAcct.setText("Already have an account?");

        btnBack.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnBack.setForeground(new java.awt.Color(200, 16, 46));
        btnBack.setText("Sign in");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addComponent(lblSub)
                    .addComponent(lblFullName)
                    .addComponent(tfFullName, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(lblUsername)
                    .addComponent(tfUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(lblEmail)
                    .addComponent(tfEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(lblPass)
                    .addComponent(tfPass, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(lblConfirm)
                    .addComponent(tfConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(lblMsg)
                    .addComponent(btnRegister, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHaveAcct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack)))
                .addGap(60, 60, 60))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSub)
                .addGap(14, 14, 14)
                .addComponent(lblFullName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(lblUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(lblEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(lblPass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(lblConfirm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(lblMsg)
                .addGap(8, 8, 8)
                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHaveAcct)
                    .addComponent(btnBack))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        doRegister();
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new LoginFrame().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void doRegister() {
        String fullName = tfFullName.getText().trim();
        String username = tfUsername.getText().trim();
        String email    = tfEmail.getText().trim();
        String pass     = new String(tfPass.getPassword());
        String confirm  = new String(tfConfirm.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            setMsg("Please fill in all fields.", false); return;
        }
        if (!pass.equals(confirm)) { setMsg("Passwords do not match.", false); return; }
        if (pass.length() < 6)     { setMsg("Password must be at least 6 characters.", false); return; }
        if (!email.contains("@"))  { setMsg("Please enter a valid email.", false); return; }

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "INSERT INTO users (username, password, email, full_name) VALUES (?,?,?,?)")) {
            ps.setString(1, username);
            ps.setString(2, pass);
            ps.setString(3, email);
            ps.setString(4, fullName);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,
                "Account created successfully! You can now log in.",
                "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame().setVisible(true);
        } catch (SQLIntegrityConstraintViolationException ex) {
            setMsg("Username or email already exists.", false);
        } catch (SQLException ex) {
            setMsg("DB Error: " + ex.getMessage(), false);
        }
    }

    private void setMsg(String text, boolean ok) {
        lblMsg.setText(text);
        lblMsg.setForeground(ok ? new java.awt.Color(40, 167, 69)
                                : new java.awt.Color(220, 53, 69));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel lblConfirm;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFullName;
    private javax.swing.JLabel lblHaveAcct;
    private javax.swing.JLabel lblMsg;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField tfConfirm;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfFullName;
    private javax.swing.JPasswordField tfPass;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
}
