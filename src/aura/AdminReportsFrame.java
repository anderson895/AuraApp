package aura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 * AURA - Admin: reports dashboard. Header stat cards plus a JTabbedPane
 * with one JTable per breakdown (status, programs, subjects, recent apps).
 */
public class AdminReportsFrame extends javax.swing.JFrame {

    private User admin;

    public AdminReportsFrame() {
        this(UIHelper.guestAdmin());
    }

    public AdminReportsFrame(User admin) {
        this.admin = admin;
        initComponents();
        setLocationRelativeTo(null);
        loadReports();
        UIHelper.flattenButtons(getContentPane());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        pnlStats = new javax.swing.JPanel();
        pnlCard1 = new javax.swing.JPanel();
        lblVal1 = new javax.swing.JLabel();
        lblName1 = new javax.swing.JLabel();
        pnlCard2 = new javax.swing.JPanel();
        lblVal2 = new javax.swing.JLabel();
        lblName2 = new javax.swing.JLabel();
        pnlCard3 = new javax.swing.JPanel();
        lblVal3 = new javax.swing.JLabel();
        lblName3 = new javax.swing.JLabel();
        pnlCard4 = new javax.swing.JPanel();
        lblVal4 = new javax.swing.JLabel();
        lblName4 = new javax.swing.JLabel();
        tabsDetails = new javax.swing.JTabbedPane();
        spStatus = new javax.swing.JScrollPane();
        tblStatus = new javax.swing.JTable();
        spPrograms = new javax.swing.JScrollPane();
        tblPrograms = new javax.swing.JTable();
        spSubjects = new javax.swing.JScrollPane();
        tblSubjects = new javax.swing.JTable();
        spRecent = new javax.swing.JScrollPane();
        tblRecent = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AURA - Admin: Reports");

        pnlTop.setBackground(new java.awt.Color(200, 16, 46));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Reports Overview");

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitle)
                .addContainerGap(200, Short.MAX_VALUE))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblTitle)
                .addGap(12, 12, 12))
        );

        pnlStats.setOpaque(false);

        pnlCard1.setBackground(new java.awt.Color(255, 255, 255));
        pnlCard1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        lblVal1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblVal1.setForeground(new java.awt.Color(200, 16, 46));
        lblVal1.setText("0");

        lblName1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblName1.setForeground(new java.awt.Color(102, 102, 102));
        lblName1.setText("Students");

        javax.swing.GroupLayout pnlCard1Layout = new javax.swing.GroupLayout(pnlCard1);
        pnlCard1.setLayout(pnlCard1Layout);
        pnlCard1Layout.setHorizontalGroup(
            pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard1Layout.createSequentialGroup()
                .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVal1)
                    .addComponent(lblName1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCard1Layout.setVerticalGroup(
            pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard1Layout.createSequentialGroup()
                .addComponent(lblVal1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblName1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCard2.setBackground(new java.awt.Color(255, 255, 255));
        pnlCard2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        lblVal2.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblVal2.setForeground(new java.awt.Color(200, 16, 46));
        lblVal2.setText("0");

        lblName2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblName2.setForeground(new java.awt.Color(102, 102, 102));
        lblName2.setText("Applications");

        javax.swing.GroupLayout pnlCard2Layout = new javax.swing.GroupLayout(pnlCard2);
        pnlCard2.setLayout(pnlCard2Layout);
        pnlCard2Layout.setHorizontalGroup(
            pnlCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard2Layout.createSequentialGroup()
                .addGroup(pnlCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVal2)
                    .addComponent(lblName2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCard2Layout.setVerticalGroup(
            pnlCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard2Layout.createSequentialGroup()
                .addComponent(lblVal2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblName2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCard3.setBackground(new java.awt.Color(255, 255, 255));
        pnlCard3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        lblVal3.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblVal3.setForeground(new java.awt.Color(200, 16, 46));
        lblVal3.setText("0");

        lblName3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblName3.setForeground(new java.awt.Color(102, 102, 102));
        lblName3.setText("Accepted");

        javax.swing.GroupLayout pnlCard3Layout = new javax.swing.GroupLayout(pnlCard3);
        pnlCard3.setLayout(pnlCard3Layout);
        pnlCard3Layout.setHorizontalGroup(
            pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard3Layout.createSequentialGroup()
                .addGroup(pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVal3)
                    .addComponent(lblName3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCard3Layout.setVerticalGroup(
            pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard3Layout.createSequentialGroup()
                .addComponent(lblVal3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblName3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCard4.setBackground(new java.awt.Color(255, 255, 255));
        pnlCard4.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        lblVal4.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblVal4.setForeground(new java.awt.Color(200, 16, 46));
        lblVal4.setText("0");

        lblName4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblName4.setForeground(new java.awt.Color(102, 102, 102));
        lblName4.setText("Enrollments");

        javax.swing.GroupLayout pnlCard4Layout = new javax.swing.GroupLayout(pnlCard4);
        pnlCard4.setLayout(pnlCard4Layout);
        pnlCard4Layout.setHorizontalGroup(
            pnlCard4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard4Layout.createSequentialGroup()
                .addGroup(pnlCard4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVal4)
                    .addComponent(lblName4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCard4Layout.setVerticalGroup(
            pnlCard4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard4Layout.createSequentialGroup()
                .addComponent(lblVal4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblName4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlStatsLayout = new javax.swing.GroupLayout(pnlStats);
        pnlStats.setLayout(pnlStatsLayout);
        pnlStatsLayout.setHorizontalGroup(
            pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatsLayout.createSequentialGroup()
                .addComponent(pnlCard1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addGap(14, 14, 14)
                .addComponent(pnlCard2, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addGap(14, 14, 14)
                .addComponent(pnlCard3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addGap(14, 14, 14)
                .addComponent(pnlCard4, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
        );
        pnlStatsLayout.setVerticalGroup(
            pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlCard2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlCard3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlCard4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabsDetails.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        tblStatus.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] { "Status", "Count" }
        ) {
            Class[] types = new Class[] { java.lang.String.class, java.lang.Integer.class };
            boolean[] canEdit = new boolean[] { false, false };
            public Class getColumnClass(int columnIndex) { return types[columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        });
        spStatus.setViewportView(tblStatus);
        tabsDetails.addTab("Status Breakdown", spStatus);

        tblPrograms.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] { "Program", "Applications" }
        ) {
            Class[] types = new Class[] { java.lang.String.class, java.lang.Integer.class };
            boolean[] canEdit = new boolean[] { false, false };
            public Class getColumnClass(int columnIndex) { return types[columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        });
        spPrograms.setViewportView(tblPrograms);
        tabsDetails.addTab("Top Programs", spPrograms);

        tblSubjects.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] { "Code", "Title", "Enrollments" }
        ) {
            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.Integer.class };
            boolean[] canEdit = new boolean[] { false, false, false };
            public Class getColumnClass(int columnIndex) { return types[columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        });
        spSubjects.setViewportView(tblSubjects);
        tabsDetails.addTab("Top Subjects", spSubjects);

        tblRecent.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] { "Submitted", "Name", "Status", "Program" }
        ) {
            boolean[] canEdit = new boolean[] { false, false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        });
        spRecent.setViewportView(tblRecent);
        tabsDetails.addTab("Recent Applications", spRecent);

        btnRefresh.setBackground(new java.awt.Color(200, 16, 46));
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlStats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabsDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 400, Short.MAX_VALUE)
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlStats, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabsDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh)
                    .addComponent(btnBack))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadReports();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void loadReports() {
        DefaultTableModel mStatus   = (DefaultTableModel) tblStatus.getModel();
        DefaultTableModel mPrograms = (DefaultTableModel) tblPrograms.getModel();
        DefaultTableModel mSubjects = (DefaultTableModel) tblSubjects.getModel();
        DefaultTableModel mRecent   = (DefaultTableModel) tblRecent.getModel();

        mStatus.setRowCount(0);
        mPrograms.setRowCount(0);
        mSubjects.setRowCount(0);
        mRecent.setRowCount(0);

        try (Connection c = DatabaseConnection.getConnection()) {
            int totalStudents = scalar(c, "SELECT COUNT(*) FROM users WHERE role='student'");
            int totalApps     = scalar(c, "SELECT COUNT(*) FROM admission_forms");
            int accepted      = scalar(c, "SELECT COUNT(*) FROM admission_forms WHERE status='Accepted'");
            int enrollments   = scalar(c, "SELECT COUNT(*) FROM enrollments");

            lblVal1.setText(String.valueOf(totalStudents));
            lblVal2.setText(String.valueOf(totalApps));
            lblVal3.setText(String.valueOf(accepted));
            lblVal4.setText(String.valueOf(enrollments));

            try (PreparedStatement ps = c.prepareStatement(
                "SELECT status, COUNT(*) c FROM admission_forms GROUP BY status ORDER BY status");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mStatus.addRow(new Object[] { rs.getString("status"), rs.getInt("c") });
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                "SELECT program, COUNT(*) c FROM admission_forms WHERE program IS NOT NULL " +
                "GROUP BY program ORDER BY c DESC LIMIT 10");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mPrograms.addRow(new Object[] { rs.getString("program"), rs.getInt("c") });
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                "SELECT s.code, s.title, COUNT(e.id) c FROM subjects s " +
                "LEFT JOIN enrollments e ON e.subject_id=s.id " +
                "GROUP BY s.id ORDER BY c DESC LIMIT 10");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mSubjects.addRow(new Object[] {
                        rs.getString("code"), rs.getString("title"), rs.getInt("c")
                    });
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                "SELECT CONCAT(last_name,', ',first_name) AS name, program, status, submitted_at " +
                "FROM admission_forms ORDER BY submitted_at DESC LIMIT 10");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mRecent.addRow(new Object[] {
                        rs.getString("submitted_at"),
                        rs.getString("name"),
                        rs.getString("status"),
                        str(rs.getString("program"))
                    });
                }
            }

        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Reports", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private int scalar(Connection c, String sql) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private String str(String s) { return s == null ? "" : s; }

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        java.awt.EventQueue.invokeLater(() ->
            new AdminReportsFrame(UIHelper.guestAdmin()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel lblName1;
    private javax.swing.JLabel lblName2;
    private javax.swing.JLabel lblName3;
    private javax.swing.JLabel lblName4;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVal1;
    private javax.swing.JLabel lblVal2;
    private javax.swing.JLabel lblVal3;
    private javax.swing.JLabel lblVal4;
    private javax.swing.JPanel pnlCard1;
    private javax.swing.JPanel pnlCard2;
    private javax.swing.JPanel pnlCard3;
    private javax.swing.JPanel pnlCard4;
    private javax.swing.JPanel pnlStats;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JScrollPane spPrograms;
    private javax.swing.JScrollPane spRecent;
    private javax.swing.JScrollPane spStatus;
    private javax.swing.JScrollPane spSubjects;
    private javax.swing.JTabbedPane tabsDetails;
    private javax.swing.JTable tblPrograms;
    private javax.swing.JTable tblRecent;
    private javax.swing.JTable tblStatus;
    private javax.swing.JTable tblSubjects;
    // End of variables declaration//GEN-END:variables
}
