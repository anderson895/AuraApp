package aura;

import javax.swing.JOptionPane;

/**
 * AURA - Student Dashboard
 */
public class DashboardFrame extends javax.swing.JFrame {

    private User currentUser;

    public DashboardFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public DashboardFrame(User user) {
        this.currentUser = user;
        initComponents();
        setLocationRelativeTo(null);
        lblWelcome.setText("Welcome, " + user.getFullName());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblWelcome = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        lblSub = new javax.swing.JLabel();
        btnAdmission = new javax.swing.JButton();
        btnRequirements = new javax.swing.JButton();
        btnEnroll = new javax.swing.JButton();
        btnStatus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AURA - Dashboard");

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(200, 16, 46));
        lblWelcome.setText("AURA Student Portal");

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        lblSub.setForeground(new java.awt.Color(102, 102, 102));
        lblSub.setText("Taguig City University - Enrollment and Admission");

        btnAdmission.setBackground(new java.awt.Color(200, 16, 46));
        btnAdmission.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdmission.setForeground(new java.awt.Color(255, 255, 255));
        btnAdmission.setText("Admission Form");
        btnAdmission.setBorderPainted(false);
        btnAdmission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmissionActionPerformed(evt);
            }
        });

        btnRequirements.setBackground(new java.awt.Color(200, 16, 46));
        btnRequirements.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRequirements.setForeground(new java.awt.Color(255, 255, 255));
        btnRequirements.setText("Requirements");
        btnRequirements.setBorderPainted(false);
        btnRequirements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRequirementsActionPerformed(evt);
            }
        });

        btnEnroll.setBackground(new java.awt.Color(200, 16, 46));
        btnEnroll.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEnroll.setForeground(new java.awt.Color(255, 255, 255));
        btnEnroll.setText("Enroll Subjects");
        btnEnroll.setBorderPainted(false);
        btnEnroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrollActionPerformed(evt);
            }
        });

        btnStatus.setBackground(new java.awt.Color(200, 16, 46));
        btnStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnStatus.setForeground(new java.awt.Color(255, 255, 255));
        btnStatus.setText("View Status");
        btnStatus.setBorderPainted(false);
        btnStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblWelcome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogout))
                    .addComponent(lblSub)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdmission, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRequirements, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEnroll, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWelcome)
                    .addComponent(btnLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSub)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdmission, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRequirements, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEnroll, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int r = JOptionPane.showConfirmDialog(this, "Logout from AURA?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) { dispose(); new LoginFrame().setVisible(true); }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnAdmissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmissionActionPerformed
        new AdmissionFormFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_btnAdmissionActionPerformed

    private void btnRequirementsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRequirementsActionPerformed
        new RequirementsFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_btnRequirementsActionPerformed

    private void btnEnrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrollActionPerformed
        new EnrollmentFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_btnEnrollActionPerformed

    private void btnStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatusActionPerformed
        new StatusFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_btnStatusActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdmission;
    private javax.swing.JButton btnEnroll;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRequirements;
    private javax.swing.JButton btnStatus;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblWelcome;
    // End of variables declaration//GEN-END:variables
}
