package aura;

import java.awt.*;
import java.net.URL;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA – Registration Screen
 */
public class RegisterFrame extends JFrame {

    private JTextField     tfFullName, tfUsername, tfEmail;
    private JPasswordField tfPass, tfConfirm;
    private JLabel         lblMsg;

    public RegisterFrame() {
        setTitle("AURA – Create Account");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(860, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildLeft(), buildRight());
        split.setDividerLocation(380);
        split.setDividerSize(0);
        split.setEnabled(false);
        setContentPane(split);
    }

    // ─── Left panel ───────────────────────────────────────────
    private JPanel buildLeft() {
        JPanel p = new JPanel(new BorderLayout()) {
            Image bg;
            { URL u = getClass().getClassLoader().getResource("banner.jpg");
              if (u != null) bg = new ImageIcon(u).getImage(); }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                else { g.setColor(UIHelper.RED); g.fillRect(0, 0, getWidth(), getHeight()); }
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(0, 30, 80, 30));

        ImageIcon logoIco = UIHelper.icon("logo.jpg", 90, 90);
        JLabel lbLogo = new JLabel(logoIco);
        lbLogo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbTitle = mkLabel("Join AURA", new Font("Segoe UI", Font.BOLD, 34), Color.WHITE);
        lbTitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbSub = mkLabel("<html><center>Start your admission journey<br>at Taguig City University</center></html>",
                               new Font("Segoe UI", Font.PLAIN, 15), new Color(255, 210, 210));
        lbSub.setAlignmentX(CENTER_ALIGNMENT);

        box.add(Box.createVerticalGlue());
        if (logoIco != null) { box.add(lbLogo); box.add(Box.createVerticalStrut(16)); }
        box.add(lbTitle);
        box.add(Box.createVerticalStrut(10));
        box.add(lbSub);

        p.add(box, BorderLayout.CENTER);
        return p;
    }

    // ─── Right panel ──────────────────────────────────────────
    private JPanel buildRight() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(UIHelper.WHITE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UIHelper.WHITE);
        form.setBorder(new EmptyBorder(20, 44, 20, 44));
        form.setPreferredSize(new Dimension(440, 540));

        JLabel lbHead = mkLabel("Create Account", UIHelper.F_TITLE, UIHelper.RED);
        lbHead.setAlignmentX(CENTER_ALIGNMENT);
        JLabel lbSub = mkLabel("Fill in your details to register", UIHelper.F_SMALL, UIHelper.TEXT_GRAY);
        lbSub.setAlignmentX(CENTER_ALIGNMENT);

        // Fields
        tfFullName = UIHelper.field(24);
        tfUsername  = UIHelper.field(24);
        tfEmail     = UIHelper.field(24);
        tfPass      = UIHelper.passField(24);
        tfConfirm   = UIHelper.passField(24);

        for (JComponent c : new JComponent[]{tfFullName, tfUsername, tfEmail, tfPass, tfConfirm})
            c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        lblMsg = mkLabel(" ", UIHelper.F_SMALL, UIHelper.DANGER);
        lblMsg.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnReg = UIHelper.primaryBtn("  REGISTER  ");
        btnReg.setAlignmentX(CENTER_ALIGNMENT);
        btnReg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnReg.addActionListener(e -> doRegister());

        JPanel backRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        backRow.setOpaque(false);
        backRow.add(mkLabel("Already have an account?", UIHelper.F_SMALL, UIHelper.TEXT_GRAY));
        JButton btnBack = linkBtn("Sign in");
        btnBack.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        backRow.add(btnBack);

        // Assemble
        addRow(form, "Full Name",        tfFullName);
        addRow(form, "Username",         tfUsername);
        addRow(form, "Email Address",    tfEmail);
        addRow(form, "Password",         tfPass);
        addRow(form, "Confirm Password", tfConfirm);

        form.add(Box.createVerticalStrut(4));
        form.add(lblMsg);
        form.add(Box.createVerticalStrut(6));
        form.add(btnReg);
        form.add(Box.createVerticalStrut(14));
        form.add(backRow);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(UIHelper.WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(lbHead);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(lbSub);
        wrapper.add(Box.createVerticalStrut(18));
        wrapper.add(form);

        outer.add(wrapper);
        return outer;
    }

    private void addRow(JPanel parent, String labelText, JComponent field) {
        JLabel lbl = mkLabel(labelText, UIHelper.F_LABEL, UIHelper.TEXT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(3));
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));
    }

    // ─── Register logic ───────────────────────────────────────
    private void doRegister() {
        String fullName = tfFullName.getText().trim();
        String username = tfUsername.getText().trim();
        String email    = tfEmail.getText().trim();
        String pass     = new String(tfPass.getPassword());
        String confirm  = new String(tfConfirm.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            setMsg("Please fill in all fields.", false); return;
        }
        if (!pass.equals(confirm)) {
            setMsg("Passwords do not match.", false); return;
        }
        if (pass.length() < 6) {
            setMsg("Password must be at least 6 characters.", false); return;
        }
        if (!email.contains("@")) {
            setMsg("Please enter a valid email.", false); return;
        }

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "INSERT INTO users (username, password, email, full_name) VALUES (?,?,?,?)")) {
            ps.setString(1, username);
            ps.setString(2, pass);
            ps.setString(3, email);
            ps.setString(4, fullName);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,
                "✅ Account created successfully!\nYou can now log in.",
                "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame().setVisible(true);
        } catch (SQLIntegrityConstraintViolationException ex) {
            setMsg("Username or email already exists.", false);
        } catch (SQLException ex) {
            setMsg("DB Error: " + ex.getMessage(), false);
        }
    }

    private void setMsg(String text, boolean ok) {
        lblMsg.setText(text);
        lblMsg.setForeground(ok ? UIHelper.GREEN : UIHelper.DANGER);
    }

    private static JLabel mkLabel(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }

    private static JButton linkBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(UIHelper.F_SMALL); b.setForeground(UIHelper.RED);
        b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(0,0,0,0));
        return b;
    }
}
