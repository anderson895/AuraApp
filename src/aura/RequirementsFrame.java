package aura;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA – Requirements Submission Tracker
 */
public class RequirementsFrame extends JFrame {

    private final User user;

    // Requirement checkboxes
    private JCheckBox cbForm138, cbGoodMoral, cbBirthCert;
    private JCheckBox cbIdPhoto, cbMedical, cbTranscript, cbHonDissmissal;

    private JTextArea taRemarks;
    private JLabel    lblStatus;

    // Requirement metadata
    private static final String[][] REQS = {
        {"Form 138 (Report Card)",          "Photocopy of latest Report Card"},
        {"Certificate of Good Moral",       "Original Certificate of Good Moral Character"},
        {"PSA Birth Certificate",           "Original or certified true copy of Birth Certificate"},
        {"2x2 ID Photos (4 pcs)",           "Recent ID photos with white background"},
        {"Medical Certificate",             "Issued by a licensed physician within 6 months"},
        {"Transcript of Records",           "Official Transcript (for transferees)"},
        {"Honorable Dismissal",             "Required for transferees from other schools"}
    };

    public RequirementsFrame(User user) {
        this.user = user;
        setTitle("AURA – Requirements Checklist");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        buildUI();
        loadExisting();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);
        setContentPane(root);

        // ── Top bar ──────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIHelper.RED);
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lbTitle = new JLabel("📁  Requirements Checklist");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitle.setForeground(Color.WHITE);
        JLabel lbUser = new JLabel("Student: " + user.getFullName());
        lbUser.setFont(UIHelper.F_SMALL);
        lbUser.setForeground(new Color(255, 210, 210));
        topBar.add(lbTitle, BorderLayout.WEST);
        topBar.add(lbUser, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        // ── Body ─────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIHelper.BG);
        body.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Info banner
        JPanel infoBanner = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoBanner.setBackground(new Color(0xFFF3CD));
        infoBanner.setBorder(new CompoundBorder(
            new LineBorder(new Color(0xFFC107), 1, true),
            new EmptyBorder(8, 12, 8, 12)));
        JLabel infoLbl = new JLabel(
            "<html>⚠  Check the box for each document you have <b>already prepared</b>. " +
            "Bring the originals on your scheduled admission day.</html>");
        infoLbl.setFont(UIHelper.F_SMALL);
        infoLbl.setForeground(new Color(0x856404));
        infoBanner.add(infoLbl);
        infoBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        body.add(infoBanner);
        body.add(Box.createVerticalStrut(14));

        // Requirements panel
        JPanel reqPanel = new JPanel();
        reqPanel.setLayout(new BoxLayout(reqPanel, BoxLayout.Y_AXIS));
        reqPanel.setBackground(UIHelper.WHITE);
        reqPanel.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(16, 20, 16, 20)));

        JLabel lbHead = UIHelper.sectionLabel("Required Documents");
        lbHead.setAlignmentX(LEFT_ALIGNMENT);
        reqPanel.add(lbHead);
        reqPanel.add(Box.createVerticalStrut(14));

        cbForm138       = reqRow(reqPanel, REQS[0][0], REQS[0][1]);
        cbGoodMoral     = reqRow(reqPanel, REQS[1][0], REQS[1][1]);
        cbBirthCert     = reqRow(reqPanel, REQS[2][0], REQS[2][1]);
        cbIdPhoto       = reqRow(reqPanel, REQS[3][0], REQS[3][1]);
        cbMedical       = reqRow(reqPanel, REQS[4][0], REQS[4][1]);
        cbTranscript    = reqRow(reqPanel, REQS[5][0], REQS[5][1]);
        cbHonDissmissal = reqRow(reqPanel, REQS[6][0], REQS[6][1]);

        body.add(reqPanel);
        body.add(Box.createVerticalStrut(14));

        // Remarks section
        JPanel remPanel = new JPanel(new BorderLayout(0, 8));
        remPanel.setBackground(UIHelper.WHITE);
        remPanel.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(14, 18, 14, 18)));
        remPanel.add(UIHelper.sectionLabel("Remarks / Notes"), BorderLayout.NORTH);
        taRemarks = UIHelper.textArea(3, 40);
        JScrollPane remScroll = new JScrollPane(taRemarks);
        remScroll.setBorder(new LineBorder(UIHelper.BORDER, 1));
        remPanel.add(remScroll, BorderLayout.CENTER);
        body.add(remPanel);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        root.add(scroll, BorderLayout.CENTER);

        // ── Bottom bar ───────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        bottom.setBackground(UIHelper.WHITE);
        bottom.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(UIHelper.F_SMALL);
        lblStatus.setForeground(UIHelper.DANGER);

        JButton btnBack   = UIHelper.outlineBtn("← Back");
        JButton btnSave   = UIHelper.primaryBtn("💾  Save");
        JButton btnSubmit = UIHelper.primaryBtn("✅  Submit Requirements");

        btnBack.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> save(false));
        btnSubmit.addActionListener(e -> save(true));

        bottom.add(lblStatus);
        bottom.add(btnBack);
        bottom.add(btnSave);
        bottom.add(btnSubmit);
        root.add(bottom, BorderLayout.SOUTH);
    }

    /** Creates a checkbox row with title + description. Returns the checkbox. */
    private JCheckBox reqRow(JPanel parent, String title, String desc) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JCheckBox cb = new JCheckBox();
        cb.setOpaque(false);
        cb.setPreferredSize(new Dimension(24, 24));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(UIHelper.F_LABEL);
        lbTitle.setForeground(UIHelper.TEXT);

        JLabel lbDesc = new JLabel(desc);
        lbDesc.setFont(UIHelper.F_SMALL);
        lbDesc.setForeground(UIHelper.TEXT_GRAY);

        textBox.add(lbTitle);
        textBox.add(lbDesc);

        row.add(cb, BorderLayout.WEST);
        row.add(textBox, BorderLayout.CENTER);

        // Divider
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(row, BorderLayout.CENTER);
        wrapper.add(new JSeparator(), BorderLayout.SOUTH);
        wrapper.setAlignmentX(LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        parent.add(wrapper);
        return cb;
    }

    // ─── Save / load ──────────────────────────────────────────

    private void save(boolean submit) {
        try (Connection c = DatabaseConnection.getConnection()) {
            PreparedStatement chk = c.prepareStatement(
                "SELECT id FROM requirements WHERE user_id=?");
            chk.setInt(1, user.getId());
            ResultSet rs = chk.executeQuery();

            String sql;
            if (rs.next()) {
                sql = "UPDATE requirements SET req_form138=?,req_good_moral=?,req_birth_cert=?," +
                      "req_id_photo=?,req_medical_cert=?,req_transcript=?," +
                      "req_honorable_dismissal=?,remarks=? WHERE user_id=?";
            } else {
                sql = "INSERT INTO requirements (req_form138,req_good_moral,req_birth_cert," +
                      "req_id_photo,req_medical_cert,req_transcript,req_honorable_dismissal," +
                      "remarks,user_id) VALUES (?,?,?,?,?,?,?,?,?)";
            }

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, cbForm138.isSelected()       ? 1 : 0);
            ps.setInt(2, cbGoodMoral.isSelected()     ? 1 : 0);
            ps.setInt(3, cbBirthCert.isSelected()     ? 1 : 0);
            ps.setInt(4, cbIdPhoto.isSelected()       ? 1 : 0);
            ps.setInt(5, cbMedical.isSelected()       ? 1 : 0);
            ps.setInt(6, cbTranscript.isSelected()    ? 1 : 0);
            ps.setInt(7, cbHonDissmissal.isSelected() ? 1 : 0);
            ps.setString(8, taRemarks.getText().trim());
            ps.setInt(9, user.getId());
            ps.executeUpdate();

            if (submit) {
                int count = countChecked();
                JOptionPane.showMessageDialog(this,
                    "✅ Requirements submitted!\n\n" +
                    "You have marked " + count + " of 7 documents as ready.\n" +
                    (count < 7 ? "⚠  Please complete the remaining " + (7 - count) + " document(s)." : ""),
                    "Requirements Submitted", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                setMsg("Saved successfully.", true);
            }

        } catch (SQLException ex) {
            setMsg("DB Error: " + ex.getMessage(), false);
        }
    }

    private void loadExisting() {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT * FROM requirements WHERE user_id=?")) {
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cbForm138.setSelected(rs.getInt("req_form138") == 1);
                cbGoodMoral.setSelected(rs.getInt("req_good_moral") == 1);
                cbBirthCert.setSelected(rs.getInt("req_birth_cert") == 1);
                cbIdPhoto.setSelected(rs.getInt("req_id_photo") == 1);
                cbMedical.setSelected(rs.getInt("req_medical_cert") == 1);
                cbTranscript.setSelected(rs.getInt("req_transcript") == 1);
                cbHonDissmissal.setSelected(rs.getInt("req_honorable_dismissal") == 1);
                String rem = rs.getString("remarks");
                if (rem != null) taRemarks.setText(rem);
                setMsg("Existing requirements loaded.", true);
            }
        } catch (SQLException ignored) {}
    }

    private int countChecked() {
        int n = 0;
        for (JCheckBox cb : new JCheckBox[]{cbForm138, cbGoodMoral, cbBirthCert,
                cbIdPhoto, cbMedical, cbTranscript, cbHonDissmissal})
            if (cb.isSelected()) n++;
        return n;
    }

    private void setMsg(String text, boolean ok) {
        lblStatus.setText(text);
        lblStatus.setForeground(ok ? UIHelper.GREEN : UIHelper.DANGER);
    }
}
