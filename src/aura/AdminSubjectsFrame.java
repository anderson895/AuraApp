package aura;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * AURA - Admin: manage courses / subjects catalog.
 */
public class AdminSubjectsFrame extends javax.swing.JFrame {

    private User admin;
    private DefaultTableModel model;

    public AdminSubjectsFrame() {
        this(UIHelper.guestAdmin());
    }

    public AdminSubjectsFrame(User admin) {
        this.admin = admin;
        initComponents();
        setLocationRelativeTo(null);
        setupTable();
        loadData();
        UIHelper.flattenButtons(getContentPane());
    }

    private void setupTable() {
        model = new DefaultTableModel(new Object[]{
            "ID", "Code", "Title", "Units", "Program", "Year", "Semester"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSubjects.setModel(model);
        tblSubjects.setFont(UIHelper.F_BODY);
        tblSubjects.getTableHeader().setFont(UIHelper.F_LABEL);
        tblSubjects.setSelectionBackground(new java.awt.Color(0xFDE6EA));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSubjects = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AURA - Admin: Subjects");

        pnlTop.setBackground(new java.awt.Color(200, 16, 46));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Manage Courses / Subjects");

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

        jScrollPane1.setViewportView(tblSubjects);

        lblStatus.setText(" ");

        btnAdd.setBackground(new java.awt.Color(200, 16, 46));
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
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
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete)
                    .addComponent(btnBack))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        openDialog(null);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int row = tblSubjects.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row."); return; }
        openDialog((int) model.getValueAt(row, 0));
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        deleteSubject();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void loadData() {
        if (model == null) return;
        model.setRowCount(0);
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT id,code,title,units,program,year_level,semester FROM subjects ORDER BY code");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("code"), rs.getString("title"),
                    rs.getInt("units"), rs.getString("program"),
                    rs.getString("year_level"), rs.getString("semester")
                });
            }
            lblStatus.setText(model.getRowCount() + " subject(s).");
        } catch (SQLException ex) {
            lblStatus.setForeground(UIHelper.DANGER);
            lblStatus.setText("DB Error: " + ex.getMessage());
        }
    }

    private void openDialog(Integer id) {
        JTextField tfCode    = UIHelper.field(10);
        JTextField tfTitle   = UIHelper.field(24);
        JTextField tfUnits   = UIHelper.field(4);
        JTextField tfProgram = UIHelper.field(24);
        JComboBox<String> cbYear = UIHelper.combo(new String[]{
            "1st Year", "2nd Year", "3rd Year", "4th Year"});
        JComboBox<String> cbSem  = UIHelper.combo(new String[]{
            "1st Sem", "2nd Sem", "Summer"});
        tfUnits.setText("3");

        if (id != null) {
            try (Connection c = DatabaseConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("SELECT * FROM subjects WHERE id=?")) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    tfCode.setText(rs.getString("code"));
                    tfTitle.setText(rs.getString("title"));
                    tfUnits.setText(String.valueOf(rs.getInt("units")));
                    tfProgram.setText(rs.getString("program"));
                    cbYear.setSelectedItem(rs.getString("year_level"));
                    cbSem.setSelectedItem(rs.getString("semester"));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
                return;
            }
        }

        JPanel p = new JPanel(new GridLayout(0, 2, 10, 8));
        p.add(new JLabel("Code *"));       p.add(tfCode);
        p.add(new JLabel("Title *"));      p.add(tfTitle);
        p.add(new JLabel("Units"));        p.add(tfUnits);
        p.add(new JLabel("Program"));      p.add(tfProgram);
        p.add(new JLabel("Year Level"));   p.add(cbYear);
        p.add(new JLabel("Semester"));     p.add(cbSem);

        int ok = JOptionPane.showConfirmDialog(this, p,
            id == null ? "Add Subject" : "Edit Subject",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return;

        String code = tfCode.getText().trim();
        String title = tfTitle.getText().trim();
        if (code.isEmpty() || title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Code and Title are required.");
            return;
        }
        int units;
        try { units = Integer.parseInt(tfUnits.getText().trim()); }
        catch (NumberFormatException ex) { units = 3; }

        try (Connection c = DatabaseConnection.getConnection()) {
            if (id == null) {
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO subjects (code,title,units,program,year_level,semester) VALUES (?,?,?,?,?,?)");
                ps.setString(1, code); ps.setString(2, title); ps.setInt(3, units);
                ps.setString(4, tfProgram.getText().trim());
                ps.setString(5, (String) cbYear.getSelectedItem());
                ps.setString(6, (String) cbSem.getSelectedItem());
                ps.executeUpdate();
            } else {
                PreparedStatement ps = c.prepareStatement(
                    "UPDATE subjects SET code=?,title=?,units=?,program=?,year_level=?,semester=? WHERE id=?");
                ps.setString(1, code); ps.setString(2, title); ps.setInt(3, units);
                ps.setString(4, tfProgram.getText().trim());
                ps.setString(5, (String) cbYear.getSelectedItem());
                ps.setString(6, (String) cbSem.getSelectedItem());
                ps.setInt(7, id);
                ps.executeUpdate();
            }
            loadData();
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Subject code already exists.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    private void deleteSubject() {
        int row = tblSubjects.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row."); return; }
        int id = (int) model.getValueAt(row, 0);
        String title = (String) model.getValueAt(row, 2);
        int r = JOptionPane.showConfirmDialog(this, "Delete subject \"" + title + "\"?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM subjects WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        java.awt.EventQueue.invokeLater(() ->
            new AdminSubjectsFrame(UIHelper.guestAdmin()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTable tblSubjects;
    // End of variables declaration//GEN-END:variables
}
