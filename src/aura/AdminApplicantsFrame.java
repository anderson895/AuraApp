package aura;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * AURA - Admin: review applicants, approve or reject admission.
 */
public class AdminApplicantsFrame extends JFrame {

    private final User admin;
    private DefaultTableModel model;
    private JTable table;
    private JComboBox<String> cbFilter;
    private JLabel lblStatus;

    public AdminApplicantsFrame(User admin) {
        this.admin = admin;
        setTitle("AURA - Admin: Applicants");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 560);
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
        JLabel lbTitle = new JLabel("Applicants - Admission Review");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitle.setForeground(Color.WHITE);
        top.add(lbTitle, BorderLayout.WEST);
        root.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setBackground(UIHelper.BG);
        body.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterBar.setBackground(UIHelper.BG);
        filterBar.add(new JLabel("Filter by status:"));
        cbFilter = new JComboBox<>(new String[]{"All", "Pending", "Under Review", "Accepted", "Rejected"});
        cbFilter.addActionListener(e -> loadData());
        filterBar.add(cbFilter);

        JButton btnRefresh = UIHelper.outlineBtn("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        filterBar.add(btnRefresh);

        body.add(filterBar, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{
                "ID", "User ID", "Full Name", "Program", "Contact", "Status", "Submitted"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(UIHelper.F_BODY);
        table.getTableHeader().setFont(UIHelper.F_LABEL);
        table.setSelectionBackground(new Color(0xFDE6EA));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(UIHelper.BORDER, 1));
        body.add(scroll, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UIHelper.BG);

        lblStatus = new JLabel(" ");
        lblStatus.setFont(UIHelper.F_SMALL);
        lblStatus.setForeground(UIHelper.TEXT_GRAY);
        btnRow.add(lblStatus);

        JButton btnView    = UIHelper.outlineBtn("View Details");
        JButton btnApprove = UIHelper.primaryBtn("Approve");
        JButton btnReject  = UIHelper.primaryBtn("Reject");
        JButton btnBack    = UIHelper.outlineBtn("Back");

        btnView.addActionListener(e -> viewDetails());
        btnApprove.addActionListener(e -> updateStatus("Accepted"));
        btnReject.addActionListener(e -> updateStatus("Rejected"));
        btnBack.addActionListener(e -> dispose());

        btnRow.add(btnView);
        btnRow.add(btnApprove);
        btnRow.add(btnReject);
        btnRow.add(btnBack);

        body.add(btnRow, BorderLayout.SOUTH);
        root.add(body, BorderLayout.CENTER);
    }

    private void loadData() {
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
        int row = table.getSelectedRow();
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
}
