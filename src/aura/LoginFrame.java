package aura;

import java.awt.*;
import java.net.URL;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA – Login Screen
 */
public class LoginFrame extends JFrame {

    private JTextField     tfUser;
    private JPasswordField tfPass;
    private JLabel         lblMsg;

    public LoginFrame() {
        setTitle("AURA – Automated Enrollment & Admission Portal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(860, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildLeft(), buildRight());
        split.setDividerLocation(400);
        split.setDividerSize(0);
        split.setEnabled(false);
        setContentPane(split);
    }

    // ─── Left panel: banner + overlay text ───────────────────
    private JPanel buildLeft() {
        JPanel p = new JPanel(new BorderLayout()) {
            Image bg;
            { URL u = getClass().getClassLoader().getResource("banner.jpg");
              if (u != null) bg = new ImageIcon(u).getImage(); }

            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                else           { g.setColor(UIHelper.RED); g.fillRect(0, 0, getWidth(), getHeight()); }
                // Dark overlay
                g.setColor(new Color(0, 0, 0, 140));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setBorder(new EmptyBorder(0, 30, 80, 30));

     

     

        p.add(textBox, BorderLayout.CENTER);
        return p;
    }

    // ─── Right panel: login form ──────────────────────────────
    private JPanel buildRight() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(UIHelper.WHITE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UIHelper.WHITE);
        form.setBorder(new EmptyBorder(16, 48, 16, 48));
        form.setPreferredSize(new Dimension(360, 520));

        // Logo
        JLabel logoLbl = new JLabel();
        logoLbl.setAlignmentX(CENTER_ALIGNMENT);
        ImageIcon ico = UIHelper.icon("logo.jpg", 80, 80);
        if (ico != null) logoLbl.setIcon(ico);

        JLabel lbWelcome = label("Welcome Back!", UIHelper.F_TITLE, UIHelper.RED);
        lbWelcome.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbHint = label("Sign in to your AURA account", UIHelper.F_SMALL, UIHelper.TEXT_GRAY);
        lbHint.setAlignmentX(CENTER_ALIGNMENT);

        // Username row
        JLabel lusr = label("Username", UIHelper.F_LABEL, UIHelper.TEXT);
        tfUser = UIHelper.field(22);
        tfUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Password row
        JLabel lpass = label("Password", UIHelper.F_LABEL, UIHelper.TEXT);
        tfPass = UIHelper.passField(22);
        tfPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        tfPass.addActionListener(e -> doLogin());

        // Status message
        lblMsg = label(" ", UIHelper.F_SMALL, UIHelper.DANGER);
        lblMsg.setAlignmentX(CENTER_ALIGNMENT);

        // Login button
        JButton btnLogin = UIHelper.primaryBtn("  LOGIN  ");
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.addActionListener(e -> doLogin());

        // Register link row
        JPanel regRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        regRow.setOpaque(false);
        regRow.add(label("No account yet?", UIHelper.F_SMALL, UIHelper.TEXT_GRAY));
        JButton btnReg = linkBtn("Register here");
        btnReg.addActionListener(e -> { dispose(); new RegisterFrame().setVisible(true); });
        regRow.add(btnReg);

        form.add(logoLbl);
        form.add(Box.createVerticalStrut(8));
        form.add(lbWelcome);
        form.add(Box.createVerticalStrut(4));
        form.add(lbHint);
        form.add(Box.createVerticalStrut(24));
        form.add(lusr);
        form.add(Box.createVerticalStrut(4));
        form.add(tfUser);
        form.add(Box.createVerticalStrut(14));
        form.add(lpass);
        form.add(Box.createVerticalStrut(4));
        form.add(tfPass);
        form.add(Box.createVerticalStrut(6));
        form.add(lblMsg);
        form.add(Box.createVerticalStrut(6));
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(16));
        form.add(regRow);

        outer.add(form);
        return outer;
    }

    // ─── Login logic ─────────────────────────────────────────
    private void doLogin() {
        String user = tfUser.getText().trim();
        String pass = new String(tfPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            setMsg("Please fill in all fields.", false);
            return;
        }
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT id,username,email,full_name,role FROM users WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("username"),
                                  rs.getString("email"), rs.getString("full_name"),
                                  rs.getString("role"));
                setMsg("Login successful!", true);
                dispose();
                new DashboardFrame(u).setVisible(true);
            } else {
                setMsg("Incorrect username or password.", false);
                tfPass.setText("");
            }
        } catch (SQLException ex) {
            setMsg("DB Error: " + ex.getMessage(), false);
        }
    }

    private void setMsg(String text, boolean ok) {
        lblMsg.setText(text);
        lblMsg.setForeground(ok ? UIHelper.GREEN : UIHelper.DANGER);
    }

    // ─── Helpers ─────────────────────────────────────────────
    private static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    private static JButton linkBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(UIHelper.F_SMALL);
        b.setForeground(UIHelper.RED);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(0, 0, 0, 0));
        return b;
    }
}
