package aura;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * AURA - Admin: manage students (view list, delete account).
 */
public class AdminStudentsFrame extends JFrame {

    private final User admin;
    private DefaultTableModel model;
    private JTable table;
    private JTextField tfSearch;
    private JLabel lblStatus;

    public AdminStudentsFrame(User admin) {
        this.admin = admin;
        setTitle("AURA - Admin: Students");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(880, 540);
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
        JLabel lb = new JLabel("Manage Students");
        lb.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lb.setForeground(Color.WHITE);
        top.add(lb, BorderLayout.WEST);
        root.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setBackground(UIHelper.BG);
        body.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        search.setBackground(UIHelper.BG);
        search.add(new JLabel("Search (name/username/email):"));
        tfSearch = UIHelper.field(22);
        search.add(tfSearch);
        JButton btnSearch  = UIHelper.outlineBtn("Search");
        JButton btnClear   = UIHelper.outlineBtn("Clear");
        btnSearch.addActionListener(e -> loadData());
        btnClear.addActionListener(e -> { tfSearch.setText(""); loadData(); });
        search.add(btnSearch);
        search.add(btnClear);
        body.add(search, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{
                "ID", "Username", "Full Name", "Email", "Role", "Created"
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

        JButton btnDelete = UIHelper.primaryBtn("Delete Account");
        JButton btnBack   = UIHelper.outlineBtn("Back");
        btnDelete.addActionListener(e -> deleteStudent());
        btnBack.addActionListener(e -> dispose());
        bottom.add(btnDelete);
        bottom.add(btnBack);

        body.add(bottom, BorderLayout.SOUTH);
        root.add(body, BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        String kw = tfSearch.getText().trim();
        String sql = "SELECT id,username,full_name,email,role,created_at FROM users WHERE role='student'";
        if (!kw.isEmpty()) sql += " AND (username LIKE ? OR full_name LIKE ? OR email LIKE ?)";
        sql += " ORDER BY created_at DESC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (!kw.isEmpty()) {
                String like = "%" + kw + "%";
                ps.setString(1, like); ps.setString(2, like); ps.setString(3, like);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("created_at")
                });
            }
            lblStatus.setText(model.getRowCount() + " student(s).");
        } catch (SQLException ex) {
            lblStatus.setForeground(UIHelper.DANGER);
            lblStatus.setText("DB Error: " + ex.getMessage());
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student first.");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 2);
        int r = JOptionPane.showConfirmDialog(this,
            "Delete account for \"" + name + "\"?\nThis also removes their admission form, requirements, and enrollments.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) return;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=? AND role='student'")) {
            ps.setInt(1, id);
            int n = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, n > 0 ? "Deleted." : "Nothing was deleted.");
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }
}
