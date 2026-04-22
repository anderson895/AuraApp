package aura;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * AURA - Student: enroll subjects.
 * Gated behind admission status = Accepted.
 */
public class EnrollmentFrame extends JFrame {

    private final User user;
    private String admissionStatus = "Pending";
    private String program = "";
    private String schoolYear = "";

    private DefaultTableModel availModel, enrolledModel;
    private JTable availTable, enrolledTable;
    private JLabel lblStatus, lblGate;

    public EnrollmentFrame(User user) {
        this.user = user;
        setTitle("AURA - Subject Enrollment");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(980, 600);
        setLocationRelativeTo(null);
        loadAdmission();
        buildUI();
        loadData();
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

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIHelper.RED);
        top.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lb = new JLabel("Subject Enrollment");
        lb.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lb.setForeground(Color.WHITE);
        JLabel lbUser = new JLabel("Student: " + user.getFullName());
        lbUser.setFont(UIHelper.F_SMALL);
        lbUser.setForeground(new Color(255, 210, 210));
        top.add(lb, BorderLayout.WEST);
        top.add(lbUser, BorderLayout.EAST);
        root.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setBackground(UIHelper.BG);
        body.setBorder(new EmptyBorder(12, 20, 12, 20));

        lblGate = new JLabel(buildGateText());
        lblGate.setFont(UIHelper.F_LABEL);
        lblGate.setBorder(new EmptyBorder(6, 10, 6, 10));
        lblGate.setOpaque(true);
        if ("Accepted".equalsIgnoreCase(admissionStatus)) {
            lblGate.setBackground(new Color(0xD4EDDA));
            lblGate.setForeground(new Color(0x155724));
        } else {
            lblGate.setBackground(new Color(0xFFF3CD));
            lblGate.setForeground(new Color(0x856404));
        }
        body.add(lblGate, BorderLayout.NORTH);

        // Two-pane split: available | enrolled
        JPanel split = new JPanel(new GridLayout(1, 2, 12, 0));
        split.setOpaque(false);

        availModel = new DefaultTableModel(
            new Object[]{"ID", "Code", "Title", "Units", "Program", "Year", "Sem"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        availTable = makeTable(availModel);

        enrolledModel = new DefaultTableModel(
            new Object[]{"EnrollID", "Code", "Title", "Units", "Enrolled At"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        enrolledTable = makeTable(enrolledModel);

        split.add(wrap("Available Subjects", availTable));
        split.add(wrap("My Enrolled Subjects", enrolledTable));
        body.add(split, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBackground(UIHelper.BG);
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UIHelper.F_SMALL);
        lblStatus.setForeground(UIHelper.TEXT_GRAY);
        bottom.add(lblStatus);

        JButton btnEnroll = UIHelper.primaryBtn("Enroll Selected");
        JButton btnDrop   = UIHelper.outlineBtn("Drop Selected");
        JButton btnRefresh = UIHelper.outlineBtn("Refresh");
        JButton btnBack   = UIHelper.outlineBtn("Back");

        boolean accepted = "Accepted".equalsIgnoreCase(admissionStatus);
        btnEnroll.setEnabled(accepted);
        btnDrop.setEnabled(accepted);

        btnEnroll.addActionListener(e -> enroll());
        btnDrop.addActionListener(e -> drop());
        btnRefresh.addActionListener(e -> loadData());
        btnBack.addActionListener(e -> dispose());

        bottom.add(btnRefresh);
        bottom.add(btnEnroll);
        bottom.add(btnDrop);
        bottom.add(btnBack);
        body.add(bottom, BorderLayout.SOUTH);

        root.add(body, BorderLayout.CENTER);
    }

    private String buildGateText() {
        if ("Accepted".equalsIgnoreCase(admissionStatus)) {
            return "Admission Status: ACCEPTED. You may enroll subjects for " +
                   (program == null ? "" : program) +
                   (schoolYear == null ? "" : " (" + schoolYear + ")");
        }
        return "Enrollment is locked. Your admission status is: " + admissionStatus +
               ". Wait for the admin to approve your application.";
    }

    private JTable makeTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(22);
        t.setFont(UIHelper.F_BODY);
        t.getTableHeader().setFont(UIHelper.F_LABEL);
        t.setSelectionBackground(new Color(0xFDE6EA));
        return t;
    }

    private JPanel wrap(String title, JTable table) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(UIHelper.WHITE);
        p.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(10, 10, 10, 10)));
        p.add(UIHelper.sectionLabel(title), BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(UIHelper.BORDER, 1));
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private void loadData() {
        availModel.setRowCount(0);
        enrolledModel.setRowCount(0);

        List<Integer> enrolledIds = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection()) {
            // Enrolled
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
            // Available: prefer matching program if set; else show all
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
        int[] rows = availTable.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select one or more subjects to enroll.");
            return;
        }
        int ok = 0, fail = 0;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "INSERT INTO enrollments (user_id,subject_id,school_year,semester) VALUES (?,?,?,?)")) {
            for (int r : rows) {
                int sid = (int) availModel.getValueAt(r, 0);
                String sem = (String) availModel.getValueAt(r, 6);
                try {
                    ps.setInt(1, user.getId());
                    ps.setInt(2, sid);
                    ps.setString(3, schoolYear == null ? "" : schoolYear);
                    ps.setString(4, sem);
                    ps.executeUpdate();
                    ok++;
                } catch (SQLException ex) { fail++; }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            return;
        }
        JOptionPane.showMessageDialog(this,
            "Enrolled " + ok + " subject(s)." + (fail > 0 ? "\nSkipped " + fail + " (already enrolled?)." : ""));
        loadData();
    }

    private void drop() {
        int row = enrolledTable.getSelectedRow();
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
}
