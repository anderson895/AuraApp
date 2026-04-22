package aura;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * AURA - Admin: manage courses / subjects catalog.
 */
public class AdminSubjectsFrame extends JFrame {

    private final User admin;
    private DefaultTableModel model;
    private JTable table;
    private JLabel lblStatus;

    public AdminSubjectsFrame(User admin) {
        this.admin = admin;
        setTitle("AURA - Admin: Subjects");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 540);
        setLocationRelativeTo(null);
        buildUI();
        loadData();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIHelper.RED);
        top.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lb = new JLabel("Manage Courses / Subjects");
        lb.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lb.setForeground(Color.WHITE);
        top.add(lb, BorderLayout.WEST);
        root.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setBackground(UIHelper.BG);
        body.setBorder(new EmptyBorder(14, 20, 14, 20));

        model = new DefaultTableModel(new Object[]{
                "ID", "Code", "Title", "Units", "Program", "Year", "Semester"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(UIHelper.F_BODY);
        table.getTableHeader().setFont(UIHelper.F_LABEL);
        table.setSelectionBackground(new Color(0xFDE6EA));
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(UIHelper.BORDER, 1));
        body.add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBackground(UIHelper.BG);
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UIHelper.F_SMALL);
        lblStatus.setForeground(UIHelper.TEXT_GRAY);
        bottom.add(lblStatus);

        JButton btnAdd    = UIHelper.primaryBtn("Add");
        JButton btnEdit   = UIHelper.outlineBtn("Edit");
        JButton btnDelete = UIHelper.outlineBtn("Delete");
        JButton btnBack   = UIHelper.outlineBtn("Back");

        btnAdd.addActionListener(e -> openDialog(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row."); return; }
            openDialog((int) model.getValueAt(row, 0));
        });
        btnDelete.addActionListener(e -> deleteSubject());
        btnBack.addActionListener(e -> dispose());

        bottom.add(btnAdd);
        bottom.add(btnEdit);
        bottom.add(btnDelete);
        bottom.add(btnBack);

        body.add(bottom, BorderLayout.SOUTH);
        root.add(body, BorderLayout.CENTER);
    }

    private void loadData() {
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
        int row = table.getSelectedRow();
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
}
