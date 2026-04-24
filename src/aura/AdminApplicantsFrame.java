package aura;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 * AURA - Admin: review applicants, approve or reject admission.
 */
public class AdminApplicantsFrame extends javax.swing.JFrame {

    private User admin;
    private DefaultTableModel model;

    public AdminApplicantsFrame() {
        this(UIHelper.guestAdmin());
    }

    public AdminApplicantsFrame(User admin) {
        this.admin = admin;
        initComponents();
        setLocationRelativeTo(null);
        setupTable();
        loadData();
    }

    private void setupTable() {
        model = new DefaultTableModel(new Object[]{
            "ID", "User ID", "Full Name", "Program", "Contact", "Status", "Submitted"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblApplicants.setModel(model);
        tblApplicants.setFont(UIHelper.F_BODY);
        tblApplicants.getTableHeader().setFont(UIHelper.F_LABEL);
        tblApplicants.setSelectionBackground(new java.awt.Color(0xFDE6EA));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblFilter = new javax.swing.JLabel();
        cbFilter = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblApplicants = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        btnView = new javax.swing.JButton();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AURA - Admin: Applicants");

        pnlTop.setBackground(new java.awt.Color(200, 16, 46));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Applicants - Admission Review");

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

        lblFilter.setText("Filter by status:");

        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Pending", "Under Review", "Accepted", "Rejected" }));
        cbFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFilterActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(tblApplicants);

        lblStatus.setText(" ");

        btnView.setText("View Details");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        btnApprove.setBackground(new java.awt.Color(200, 16, 46));
        btnApprove.setForeground(new java.awt.Color(255, 255, 255));
        btnApprove.setText("Approve");
        btnApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApproveActionPerformed(evt);
            }
        });

        btnReject.setBackground(new java.awt.Color(200, 16, 46));
        btnReject.setForeground(new java.awt.Color(255, 255, 255));
        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFilter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh)
                        .addGap(0, 400, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                        .addComponent(btnView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnApprove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReject)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFilter)
                    .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(btnView)
                    .addComponent(btnApprove)
                    .addComponent(btnReject)
                    .addComponent(btnBack))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterActionPerformed
        loadData();
    }//GEN-LAST:event_cbFilterActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadData();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        viewDetails();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        updateStatus("Accepted");
    }//GEN-LAST:event_btnApproveActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        updateStatus("Rejected");
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void loadData() {
        if (model == null) return;
        model.setRowCount(0);
        String filter = (String) cbFilter.getSelectedItem();
        String sql = "SELECT id,user_id,CONCAT(last_name,', ',first_name,' ',IFNULL(middle_name,'')) AS name," +
                     "program,contact_number,status,submitted_at FROM admission_forms";
        if (filter != null && !filter.equals("All")) {
            sql += " WHERE status='" + filter.replace("'", "") + "'";
        }
        sql += " ORDER BY submitted_at DESC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("program"),
                    rs.getString("contact_number"),
                    rs.getString("status"),
                    rs.getString("submitted_at")
                });
            }
            lblStatus.setText("Loaded " + model.getRowCount() + " record(s).");
        } catch (SQLException ex) {
            lblStatus.setForeground(UIHelper.DANGER);
            lblStatus.setText("DB Error: " + ex.getMessage());
        }
    }

    private int selectedId() {
        int row = tblApplicants.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an applicant first.",
                "No selection", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) model.getValueAt(row, 0);
    }

    private void updateStatus(String newStatus) {
        int id = selectedId(); if (id < 0) return;
        String remarks = JOptionPane.showInputDialog(this,
            "Remarks for applicant (optional):", newStatus, JOptionPane.PLAIN_MESSAGE);
        if (remarks == null) return;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "UPDATE admission_forms SET status=?, admin_remarks=? WHERE id=?")) {
            ps.setString(1, newStatus);
            ps.setString(2, remarks);
            ps.setInt(3, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Status updated to: " + newStatus);
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewDetails() {
        int id = selectedId(); if (id < 0) return;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT * FROM admission_forms WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Name: ").append(rs.getString("last_name")).append(", ")
                  .append(rs.getString("first_name")).append(" ")
                  .append(str(rs.getString("middle_name"))).append("\n");
                sb.append("Birthdate: ").append(str(rs.getString("birthdate"))).append("\n");
                sb.append("Gender: ").append(str(rs.getString("gender"))).append("\n");
                sb.append("Civil Status: ").append(str(rs.getString("civil_status"))).append("\n");
                sb.append("Nationality: ").append(str(rs.getString("nationality"))).append("\n");
                sb.append("Religion: ").append(str(rs.getString("religion"))).append("\n");
                sb.append("Address: ").append(str(rs.getString("address"))).append("\n");
                sb.append("Contact: ").append(str(rs.getString("contact_number"))).append("\n");
                sb.append("Email: ").append(str(rs.getString("email"))).append("\n\n");
                sb.append("Program: ").append(str(rs.getString("program"))).append("\n");
                sb.append("Previous School: ").append(str(rs.getString("previous_school"))).append("\n");
                sb.append("Strand: ").append(str(rs.getString("previous_strand"))).append("\n");
                sb.append("School Year: ").append(str(rs.getString("school_year"))).append("\n\n");
                sb.append("Guardian: ").append(str(rs.getString("guardian_name")))
                  .append(" (").append(str(rs.getString("guardian_contact"))).append(")\n\n");
                sb.append("Status: ").append(rs.getString("status")).append("\n");
                sb.append("Admin Remarks: ").append(str(rs.getString("admin_remarks"))).append("\n");

                JTextArea ta = new JTextArea(sb.toString());
                ta.setEditable(false);
                ta.setFont(UIHelper.F_BODY);
                JScrollPane sp = new JScrollPane(ta);
                sp.setPreferredSize(new Dimension(460, 360));
                JOptionPane.showMessageDialog(this, sp, "Applicant Details",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    private String str(String s) { return s == null ? "" : s; }

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        java.awt.EventQueue.invokeLater(() ->
            new AdminApplicantsFrame(UIHelper.guestAdmin()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReject;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFilter;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTable tblApplicants;
    // End of variables declaration//GEN-END:variables
}
