package aura;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA - Admin: simple reports dashboard (counts per status + totals).
 */
public class AdminReportsFrame extends JFrame {

    private final User admin;
    private JPanel statsPanel;
    private JTextArea taDetails;

    public AdminReportsFrame(User admin) {
        this.admin = admin;
        setTitle("AURA - Admin: Reports");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(780, 560);
        setLocationRelativeTo(null);
        buildUI();
        loadReports();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIHelper.RED);
        top.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lb = new JLabel("Reports Overview");
        lb.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lb.setForeground(Color.WHITE);
        top.add(lb, BorderLayout.WEST);
        root.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(12, 12));
        body.setBackground(UIHelper.BG);
        body.setBorder(new EmptyBorder(16, 20, 16, 20));

        statsPanel = new JPanel(new GridLayout(1, 4, 14, 0));
        statsPanel.setOpaque(false);
        body.add(statsPanel, BorderLayout.NORTH);

        taDetails = new JTextArea();
        taDetails.setEditable(false);
        taDetails.setFont(new Font("Consolas", Font.PLAIN, 13));
        taDetails.setBackground(UIHelper.WHITE);
        taDetails.setBorder(new EmptyBorder(12, 14, 12, 14));
        JScrollPane sp = new JScrollPane(taDetails);
        sp.setBorder(new LineBorder(UIHelper.BORDER, 1));
        body.add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBackground(UIHelper.BG);
        JButton btnRefresh = UIHelper.primaryBtn("Refresh");
        JButton btnBack    = UIHelper.outlineBtn("Back");
        btnRefresh.addActionListener(e -> loadReports());
        btnBack.addActionListener(e -> dispose());
        bottom.add(btnRefresh);
        bottom.add(btnBack);
        body.add(bottom, BorderLayout.SOUTH);

        root.add(body, BorderLayout.CENTER);
    }

    private void loadReports() {
        statsPanel.removeAll();
        StringBuilder details = new StringBuilder();

        try (Connection c = DatabaseConnection.getConnection()) {
            int totalStudents = scalar(c, "SELECT COUNT(*) FROM users WHERE role='student'");
            int totalApps     = scalar(c, "SELECT COUNT(*) FROM admission_forms");
            int accepted      = scalar(c, "SELECT COUNT(*) FROM admission_forms WHERE status='Accepted'");
            int enrollments   = scalar(c, "SELECT COUNT(*) FROM enrollments");

            statsPanel.add(statCard("Students",     String.valueOf(totalStudents)));
            statsPanel.add(statCard("Applications", String.valueOf(totalApps)));
            statsPanel.add(statCard("Accepted",     String.valueOf(accepted)));
            statsPanel.add(statCard("Enrollments",  String.valueOf(enrollments)));

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
        statsPanel.revalidate();
        statsPanel.repaint();
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
}
