package aura;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA - Admin Dashboard (4 modules)
 */
public class AdminDashboardFrame extends javax.swing.JFrame {

    private final User currentUser;

    public AdminDashboardFrame() {
        this(UIHelper.guestAdmin());
    }

    public AdminDashboardFrame(User user) {
        this.currentUser = user;
        initComponents();
        customInit();
        UIHelper.flattenButtons(getContentPane());
    }

    private void customInit() {
        setSize(860, 600);
        setLocationRelativeTo(null);

        // Paint a banner image (with dark overlay) behind the header's components.
        installBannerBackground(pnlHeader);
        pnlHeader.setPreferredSize(new Dimension(860, 120));

        lblUserName.setText(currentUser.getFullName());

        // Rounded white pill for logout
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(new CompoundBorder(
            new LineBorder(Color.WHITE, 1, true),
            new EmptyBorder(4, 12, 4, 12)));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Style the 4 main cards with hover effect
        applyCard(btnApplicants);
        applyCard(btnStudents);
        applyCard(btnSubjects);
        applyCard(btnReports);

        pnlFooter.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));
    }

    /** Replace a plain JPanel with a banner-painting version by installing a paint delegate. */
    private void installBannerBackground(final JPanel target) {
        URL u = getClass().getClassLoader().getResource("banner.jpg");
        final Image bg = (u != null) ? new ImageIcon(u).getImage() : null;
        target.putClientProperty("bannerImage", bg);
        // Swap the panel's UI delegate is risky; simpler: override via a repaint hook.
        target.addHierarchyListener(e -> target.repaint());
        target.setOpaque(true);
        // Can't override paintComponent on an existing JPanel, so wrap by installing a
        // custom component as the FIRST child that paints the banner and overlay.
        JComponent banner = new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, target.getWidth(), target.getHeight(), this);
                else { g.setColor(UIHelper.RED); g.fillRect(0, 0, target.getWidth(), target.getHeight()); }
                g.setColor(new Color(0, 0, 0, 140));
                g.fillRect(0, 0, target.getWidth(), target.getHeight());
            }
        };
        banner.setBounds(0, 0, 1, 1);
        target.add(banner, 0);
        target.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                banner.setBounds(0, 0, target.getWidth(), target.getHeight());
            }
        });
    }

    private void applyCard(JButton b) {
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(UIHelper.RED_DARK); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(UIHelper.RED); }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        pnlCenter = new javax.swing.JPanel();
        btnApplicants = new javax.swing.JButton();
        btnStudents = new javax.swing.JButton();
        btnSubjects = new javax.swing.JButton();
        btnReports = new javax.swing.JButton();
        pnlFooter = new javax.swing.JPanel();
        lblFooter = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AURA - Admin Dashboard");
        setResizable(false);

        pnlHeader.setBackground(new java.awt.Color(200, 16, 46));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("AURA Admin Panel");

        lblSub.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblSub.setForeground(new java.awt.Color(255, 210, 210));
        lblSub.setText("Manage applicants, students, subjects and reports");

        lblUserName.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblUserName.setForeground(new java.awt.Color(255, 255, 255));
        lblUserName.setText("Administrator");

        btnLogout.setBackground(new java.awt.Color(255, 255, 255));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(200, 16, 46));
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                .addComponent(lblUserName)
                .addGap(16, 16, 16)
                .addComponent(btnLogout)
                .addGap(24, 24, 24))
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblSub)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(lblUserName)
                    .addComponent(btnLogout))
                .addGap(4, 4, 4)
                .addComponent(lblSub)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCenter.setBackground(new java.awt.Color(244, 244, 244));

        btnApplicants.setBackground(new java.awt.Color(200, 16, 46));
        btnApplicants.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        btnApplicants.setForeground(new java.awt.Color(255, 255, 255));
        btnApplicants.setText("Applicants");
        btnApplicants.setBorderPainted(false);
        btnApplicants.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplicantsActionPerformed(evt);
            }
        });

        btnStudents.setBackground(new java.awt.Color(200, 16, 46));
        btnStudents.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        btnStudents.setForeground(new java.awt.Color(255, 255, 255));
        btnStudents.setText("Manage Students");
        btnStudents.setBorderPainted(false);
        btnStudents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStudentsActionPerformed(evt);
            }
        });

        btnSubjects.setBackground(new java.awt.Color(200, 16, 46));
        btnSubjects.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        btnSubjects.setForeground(new java.awt.Color(255, 255, 255));
        btnSubjects.setText("Courses / Subjects");
        btnSubjects.setBorderPainted(false);
        btnSubjects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubjectsActionPerformed(evt);
            }
        });

        btnReports.setBackground(new java.awt.Color(200, 16, 46));
        btnReports.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        btnReports.setForeground(new java.awt.Color(255, 255, 255));
        btnReports.setText("Reports");
        btnReports.setBorderPainted(false);
        btnReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCenterLayout = new javax.swing.GroupLayout(pnlCenter);
        pnlCenter.setLayout(pnlCenterLayout);
        pnlCenterLayout.setHorizontalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCenterLayout.createSequentialGroup()
                        .addComponent(btnApplicants, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                        .addGap(20, 20, 20)
                        .addComponent(btnStudents, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                    .addGroup(pnlCenterLayout.createSequentialGroup()
                        .addComponent(btnSubjects, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                        .addGap(20, 20, 20)
                        .addComponent(btnReports, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)))
                .addGap(40, 40, 40))
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnApplicants, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSubjects, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReports, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pnlFooter.setBackground(new java.awt.Color(238, 238, 238));

        lblFooter.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblFooter.setForeground(new java.awt.Color(102, 102, 102));
        lblFooter.setText("AURA Portal - Taguig City University - Admin");

        javax.swing.GroupLayout pnlFooterLayout = new javax.swing.GroupLayout(pnlFooter);
        pnlFooter.setLayout(pnlFooterLayout);
        pnlFooterLayout.setHorizontalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFooterLayout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addComponent(lblFooter)
                .addContainerGap(200, Short.MAX_VALUE))
        );
        pnlFooterLayout.setVerticalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFooterLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblFooter)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlCenter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlCenter, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addComponent(pnlFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int r = JOptionPane.showConfirmDialog(this, "Logout from AURA?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnApplicantsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplicantsActionPerformed
        new AdminApplicantsFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_btnApplicantsActionPerformed

    private void btnStudentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudentsActionPerformed
        new AdminStudentsFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_btnStudentsActionPerformed

    private void btnSubjectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubjectsActionPerformed
        new AdminSubjectsFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_btnSubjectsActionPerformed

    private void btnReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsActionPerformed
        new AdminReportsFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_btnReportsActionPerformed

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        EventQueue.invokeLater(() ->
            new AdminDashboardFrame(UIHelper.guestAdmin()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApplicants;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnStudents;
    private javax.swing.JButton btnSubjects;
    private javax.swing.JLabel lblFooter;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables
}
