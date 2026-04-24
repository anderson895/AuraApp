package aura;

import java.sql.*;

/**
 * AURA - Student: view admission / requirements / enrollment status.
 */
public class StatusFrame extends javax.swing.JFrame {

    private User user;

    public StatusFrame() {
        this(UIHelper.guestUser());
    }

    public StatusFrame(User user) {
        this.user = user;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("AURA - My Status (" + user.getFullName() + ")");
        loadStatus();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taStatus = new javax.swing.JTextArea();
        btnRefresh = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AURA - My Status");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(200, 16, 46));
        lblTitle.setText("My Status");

        taStatus.setEditable(false);
        taStatus.setColumns(20);
        taStatus.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
        taStatus.setRows(5);
        jScrollPane1.setViewportView(taStatus);

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                    .addComponent(lblTitle)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh)
                    .addComponent(btnBack))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadStatus();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void loadStatus() {
        if (user == null) { taStatus.setText("(no session)"); return; }
        StringBuilder sb = new StringBuilder();
        sb.append("Account: ").append(user.getUsername())
          .append(" | ").append(user.getEmail()).append("\n");
        sb.append("============================================================\n\n");

        try (Connection c = DatabaseConnection.getConnection()) {
            sb.append("[1] ADMISSION FORM\n");
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT status,program,school_year,admin_remarks,submitted_at " +
                "FROM admission_forms WHERE user_id=?")) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    sb.append("    Status      : ").append(rs.getString("status")).append("\n");
                    sb.append("    Program     : ").append(str(rs.getString("program"))).append("\n");
                    sb.append("    School Year : ").append(str(rs.getString("school_year"))).append("\n");
                    sb.append("    Submitted   : ").append(rs.getString("submitted_at")).append("\n");
                    String rem = rs.getString("admin_remarks");
                    if (rem != null && !rem.isEmpty())
                        sb.append("    Admin Note  : ").append(rem).append("\n");
                } else {
                    sb.append("    Not yet submitted.\n");
                }
            }
            sb.append("\n[2] REQUIREMENTS\n");
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT req_form138,req_good_moral,req_birth_cert,req_id_photo," +
                "req_medical_cert,req_transcript,req_honorable_dismissal,updated_at " +
                "FROM requirements WHERE user_id=?")) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    sb.append(line("Form 138",          rs.getInt("req_form138")));
                    sb.append(line("Good Moral",        rs.getInt("req_good_moral")));
                    sb.append(line("Birth Certificate", rs.getInt("req_birth_cert")));
                    sb.append(line("ID Photo (2x2)",    rs.getInt("req_id_photo")));
                    sb.append(line("Medical Cert",      rs.getInt("req_medical_cert")));
                    sb.append(line("Transcript",        rs.getInt("req_transcript")));
                    sb.append(line("Honorable Dismissal", rs.getInt("req_honorable_dismissal")));
                    sb.append("    Last Updated: ").append(rs.getString("updated_at")).append("\n");
                } else {
                    sb.append("    Not yet submitted.\n");
                }
            }
            sb.append("\n[3] ENROLLED SUBJECTS\n");
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT s.code,s.title,s.units,e.enrolled_at FROM enrollments e " +
                "JOIN subjects s ON s.id=e.subject_id WHERE e.user_id=? ORDER BY s.code")) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                int n = 0, units = 0;
                while (rs.next()) {
                    n++;
                    units += rs.getInt("units");
                    sb.append(String.format("    %-8s %-45s (%d units)%n",
                        rs.getString("code"), trim(rs.getString("title"), 45), rs.getInt("units")));
                }
                if (n == 0) sb.append("    Not yet enrolled.\n");
                else sb.append(String.format("    Total: %d subject(s), %d unit(s)%n", n, units));
            }
        } catch (SQLException ex) {
            sb.append("\nDB Error: ").append(ex.getMessage());
        }
        taStatus.setText(sb.toString());
        taStatus.setCaretPosition(0);
    }

    private String line(String name, int v) {
        return String.format("    %-22s : %s%n", name, v == 1 ? "[X] Ready" : "[ ] Pending");
    }
    private String str(String s) { return s == null ? "" : s; }
    private String trim(String s, int n) {
        if (s == null) return "";
        return s.length() > n ? s.substring(0, n - 1) + "..." : s;
    }

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        java.awt.EventQueue.invokeLater(() ->
            new StatusFrame(UIHelper.guestUser()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextArea taStatus;
    // End of variables declaration//GEN-END:variables
}
