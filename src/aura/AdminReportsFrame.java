package aura;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * AURA - Admin: simple reports dashboard (counts per status + totals).
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
        pnlStats.setLayout(new GridLayout(1, 4, 14, 0));
        pnlStats.setOpaque(false);
        loadReports();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        pnlStats = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDetails = new javax.swing.JTextArea();
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

        javax.swing.GroupLayout pnlStatsLayout = new javax.swing.GroupLayout(pnlStats);
        pnlStats.setLayout(pnlStatsLayout);
        pnlStatsLayout.setHorizontalGroup(
            pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlStatsLayout.setVerticalGroup(
            pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        taDetails.setEditable(false);
        taDetails.setColumns(20);
        taDetails.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
        taDetails.setRows(5);
        jScrollPane1.setViewportView(taDetails);

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
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
                .addComponent(pnlStats, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
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
        pnlStats.removeAll();
        StringBuilder details = new StringBuilder();

        try (Connection c = DatabaseConnection.getConnection()) {
            int totalStudents = scalar(c, "SELECT COUNT(*) FROM users WHERE role='student'");
            int totalApps     = scalar(c, "SELECT COUNT(*) FROM admission_forms");
            int accepted      = scalar(c, "SELECT COUNT(*) FROM admission_forms WHERE status='Accepted'");
            int enrollments   = scalar(c, "SELECT COUNT(*) FROM enrollments");

            pnlStats.add(statCard("Students",     String.valueOf(totalStudents)));
            pnlStats.add(statCard("Applications", String.valueOf(totalApps)));
            pnlStats.add(statCard("Accepted",     String.valueOf(accepted)));
            pnlStats.add(statCard("Enrollments",  String.valueOf(enrollments)));

            details.append("=== Admission Status Breakdown ===\n");
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT status, COUNT(*) c FROM admission_forms GROUP BY status ORDER BY status");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    details.append(String.format("  %-15s : %d%n",
                        rs.getString("status"), rs.getInt("c")));
                }
            }

            details.append("\n=== Top 10 Programs Applied ===\n");
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT program, COUNT(*) c FROM admission_forms WHERE program IS NOT NULL " +
                "GROUP BY program ORDER BY c DESC LIMIT 10");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    details.append(String.format("  %-55s : %d%n",
                        trim(rs.getString("program"), 55), rs.getInt("c")));
                }
            }

            details.append("\n=== Subjects with Most Enrollments ===\n");
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT s.code, s.title, COUNT(e.id) c FROM subjects s " +
                "LEFT JOIN enrollments e ON e.subject_id=s.id " +
                "GROUP BY s.id ORDER BY c DESC LIMIT 10");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    details.append(String.format("  %-8s %-40s : %d%n",
                        rs.getString("code"), trim(rs.getString("title"), 40), rs.getInt("c")));
                }
            }

            details.append("\n=== Recent Applications (last 10) ===\n");
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT CONCAT(last_name,', ',first_name) AS name, program, status, submitted_at " +
                "FROM admission_forms ORDER BY submitted_at DESC LIMIT 10");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    details.append(String.format("  [%s] %-25s  %-12s  %s%n",
                        rs.getString("submitted_at"),
                        trim(rs.getString("name"), 25),
                        rs.getString("status"),
                        trim(str(rs.getString("program")), 30)));
                }
            }

        } catch (SQLException ex) {
            details.append("DB Error: ").append(ex.getMessage());
        }

        taDetails.setText(details.toString());
        taDetails.setCaretPosition(0);
        pnlStats.revalidate();
        pnlStats.repaint();
    }

    private int scalar(Connection c, String sql) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private JPanel statCard(String title, String value) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UIHelper.WHITE);
        p.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(14, 18, 14, 18)));

        JLabel lbVal = new JLabel(value);
        lbVal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lbVal.setForeground(UIHelper.RED);

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(UIHelper.F_SMALL);
        lbTitle.setForeground(UIHelper.TEXT_GRAY);

        p.add(lbVal);
        p.add(lbTitle);
        return p;
    }

    private String str(String s) { return s == null ? "" : s; }
    private String trim(String s, int n) {
        if (s == null) return "";
        return s.length() > n ? s.substring(0, n - 1) + "..." : s;
    }

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        java.awt.EventQueue.invokeLater(() ->
            new AdminReportsFrame(UIHelper.guestAdmin()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlStats;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTextArea taDetails;
    // End of variables declaration//GEN-END:variables
}
