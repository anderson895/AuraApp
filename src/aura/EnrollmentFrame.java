package aura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * AURA - Student: enroll subjects.
 * Gated behind admission status = Accepted.
 */
public class EnrollmentFrame extends javax.swing.JFrame {

    private User user;
    private String admissionStatus = "Pending";
    private String program = "";
    private String schoolYear = "";

    private DefaultTableModel availModel, enrolledModel;

    public EnrollmentFrame() {
        this(UIHelper.guestUser());
    }

    public EnrollmentFrame(User user) {
        this.user = user;
        loadAdmission();
        initComponents();
        setLocationRelativeTo(null);
        setTitle("AURA - Subject Enrollment");
        lblUser.setText("Student: " + user.getFullName());
        setupTables();
        applyGateStyle();
        loadData();
        UIHelper.flattenButtons(getContentPane());
    }

    private void loadAdmission() {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT status,program,school_year FROM admission_forms WHERE user_id=?")) {
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                admissionStatus = rs.getString("status");
                program    = rs.getString("program");
                schoolYear = rs.getString("school_year");
            }
        } catch (SQLException ignored) {}
    }

    private void setupTables() {
        availModel = new DefaultTableModel(
            new Object[]{"ID", "Code", "Title", "Units", "Program", "Year", "Sem"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        enrolledModel = new DefaultTableModel(
            new Object[]{"EnrollID", "Code", "Title", "Units", "Enrolled At"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblAvailable.setModel(availModel);
        tblEnrolled.setModel(enrolledModel);
        tblAvailable.setFont(UIHelper.F_BODY);
        tblEnrolled.setFont(UIHelper.F_BODY);
        tblAvailable.getTableHeader().setFont(UIHelper.F_LABEL);
        tblEnrolled.getTableHeader().setFont(UIHelper.F_LABEL);
        tblAvailable.setSelectionBackground(new java.awt.Color(0xFDE6EA));
        tblEnrolled.setSelectionBackground(new java.awt.Color(0xFDE6EA));
    }

    private void applyGateStyle() {
        lblGate.setText(buildGateText());
        lblGate.setBorder(new EmptyBorder(6, 10, 6, 10));
        if ("Accepted".equalsIgnoreCase(admissionStatus)) {
            lblGate.setBackground(new java.awt.Color(0xD4EDDA));
            lblGate.setForeground(new java.awt.Color(0x155724));
        } else {
            lblGate.setBackground(new java.awt.Color(0xFFF3CD));
            lblGate.setForeground(new java.awt.Color(0x856404));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblGate = new javax.swing.JLabel();
        lblAvailable = new javax.swing.JLabel();
        lblEnrolled = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAvailable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEnrolled = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnEnroll = new javax.swing.JButton();
        btnDrop = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AURA - Subject Enrollment");

        pnlTop.setBackground(new java.awt.Color(200, 16, 46));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Subject Enrollment");

        lblUser.setForeground(new java.awt.Color(255, 210, 210));
        lblUser.setText("Student:");

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                .addComponent(lblUser)
                .addGap(20, 20, 20))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(lblUser))
                .addGap(12, 12, 12))
        );

        lblGate.setText("Admission Status: Pending");
        lblGate.setOpaque(true);

        lblAvailable.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblAvailable.setText("Available Subjects");

        lblEnrolled.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEnrolled.setText("My Enrolled Subjects");

        tblAvailable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Code", "Title", "Units", "Program", "Year", "Sem"
            }
        ));
        tblAvailable.setRowHeight(22);
        jScrollPane1.setViewportView(tblAvailable);

        tblEnrolled.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EnrollID", "Code", "Title", "Units", "Enrolled At"
            }
        ));
        tblEnrolled.setRowHeight(22);
        jScrollPane2.setViewportView(tblEnrolled);

        lblStatus.setText(" ");

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnEnroll.setBackground(new java.awt.Color(200, 16, 46));
        btnEnroll.setForeground(new java.awt.Color(255, 255, 255));
        btnEnroll.setText("Enroll Selected");
        btnEnroll.setBorderPainted(false);
        btnEnroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrollActionPerformed(evt);
            }
        });

        btnDrop.setText("Drop Selected");
        btnDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDropActionPerformed(evt);
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
                    .addComponent(lblGate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAvailable)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEnrolled)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnroll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDrop)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAvailable)
                    .addComponent(lblEnrolled))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(btnRefresh)
                    .addComponent(btnEnroll)
                    .addComponent(btnDrop)
                    .addComponent(btnBack))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadData();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnEnrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrollActionPerformed
        enroll();
    }//GEN-LAST:event_btnEnrollActionPerformed

    private void btnDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDropActionPerformed
        drop();
    }//GEN-LAST:event_btnDropActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private String buildGateText() {
        if ("Accepted".equalsIgnoreCase(admissionStatus)) {
            return "Admission Status: ACCEPTED. You may enroll subjects for " +
                   (program == null ? "" : program) +
                   (schoolYear == null ? "" : " (" + schoolYear + ")");
        }
        return "Admission Status: " + admissionStatus +
               ". You can still try to enroll, but normally you should wait for admin approval.";
    }

    private void loadData() {
        if (availModel == null || enrolledModel == null) return;
        availModel.setRowCount(0);
        enrolledModel.setRowCount(0);

        List<Integer> enrolledIds = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                 "SELECT e.id, s.id AS sid, s.code, s.title, s.units, e.enrolled_at " +
                 "FROM enrollments e JOIN subjects s ON s.id=e.subject_id " +
                 "WHERE e.user_id=? ORDER BY s.code")) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    enrolledIds.add(rs.getInt("sid"));
                    enrolledModel.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("code"), rs.getString("title"),
                        rs.getInt("units"), rs.getString("enrolled_at")
                    });
                }
            }
            String sql = "SELECT id,code,title,units,program,year_level,semester FROM subjects";
            boolean useProgram = program != null && !program.isEmpty();
            if (useProgram) sql += " WHERE program=?";
            sql += " ORDER BY code";

            try (PreparedStatement ps = c.prepareStatement(sql)) {
                if (useProgram) ps.setString(1, program);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int sid = rs.getInt("id");
                    if (enrolledIds.contains(sid)) continue;
                    availModel.addRow(new Object[]{
                        sid, rs.getString("code"), rs.getString("title"),
                        rs.getInt("units"), rs.getString("program"),
                        rs.getString("year_level"), rs.getString("semester")
                    });
                }
            }

            int units = 0;
            for (int i = 0; i < enrolledModel.getRowCount(); i++)
                units += (int) enrolledModel.getValueAt(i, 3);
            lblStatus.setText("Enrolled: " + enrolledModel.getRowCount() +
                              " subject(s), " + units + " unit(s).");
        } catch (SQLException ex) {
            lblStatus.setForeground(UIHelper.DANGER);
            lblStatus.setText("DB Error: " + ex.getMessage());
        }
    }

    private void enroll() {
        if (user == null || user.getId() <= 0) {
            JOptionPane.showMessageDialog(this,
                "You must log in as a student first before enrolling.",
                "No session", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!"Accepted".equalsIgnoreCase(admissionStatus)) {
            int r = JOptionPane.showConfirmDialog(this,
                "Your admission status is \"" + admissionStatus + "\".\n" +
                "You normally need an Accepted admission to enroll.\n\n" +
                "Continue enrolling anyway?",
                "Admission not accepted", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (r != JOptionPane.YES_OPTION) return;
        }

        int[] rows = tblAvailable.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select one or more subjects to enroll.");
            return;
        }

        int ok = 0, dup = 0, fail = 0;
        String lastError = null;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "INSERT INTO enrollments (user_id,subject_id,school_year,semester) VALUES (?,?,?,?)")) {
            for (int r : rows) {
                int sid = (int) availModel.getValueAt(r, 0);
                Object semObj = availModel.getValueAt(r, 6);
                String sem = semObj == null ? "" : semObj.toString();
                try {
                    ps.setInt(1, user.getId());
                    ps.setInt(2, sid);
                    ps.setString(3, schoolYear == null ? "" : schoolYear);
                    ps.setString(4, sem);
                    ps.executeUpdate();
                    ok++;
                } catch (SQLIntegrityConstraintViolationException dupEx) {
                    dup++;
                } catch (SQLException ex) {
                    fail++;
                    lastError = ex.getMessage();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(),
                "Enrollment failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("Enrolled ").append(ok).append(" subject(s).");
        if (dup  > 0) msg.append("\nSkipped ").append(dup).append(" (already enrolled).");
        if (fail > 0) msg.append("\nFailed ").append(fail).append(" (").append(lastError).append(").");
        JOptionPane.showMessageDialog(this, msg.toString(),
            ok > 0 ? "Success" : "Done",
            ok > 0 ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        loadData();
    }

    private void drop() {
        int row = tblEnrolled.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an enrolled subject to drop.");
            return;
        }
        int enrollId = (int) enrolledModel.getValueAt(row, 0);
        String code = (String) enrolledModel.getValueAt(row, 1);
        int r = JOptionPane.showConfirmDialog(this, "Drop subject " + code + "?",
            "Confirm Drop", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "DELETE FROM enrollments WHERE id=? AND user_id=?")) {
            ps.setInt(1, enrollId);
            ps.setInt(2, user.getId());
            ps.executeUpdate();
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        java.awt.EventQueue.invokeLater(() ->
            new EnrollmentFrame(UIHelper.guestUser()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDrop;
    private javax.swing.JButton btnEnroll;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAvailable;
    private javax.swing.JLabel lblEnrolled;
    private javax.swing.JLabel lblGate;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTable tblAvailable;
    private javax.swing.JTable tblEnrolled;
    // End of variables declaration//GEN-END:variables
}
