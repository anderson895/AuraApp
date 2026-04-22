package aura;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA – Dashboard / Main Menu
 * Shows after successful login.
 */
public class DashboardFrame extends JFrame {

    private final User currentUser;

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("AURA – Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
    }

    // ─── Header ───────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            Image bg;
            { URL u = getClass().getClassLoader().getResource("banner.jpg");
              if (u != null) bg = new ImageIcon(u).getImage(); }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                else { g.setColor(UIHelper.RED); g.fillRect(0, 0, getWidth(), getHeight()); }
                g.setColor(new Color(0, 0, 0, 130));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(820, 130));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        left.setOpaque(false);
        ImageIcon logo = UIHelper.icon("logo.jpg", 72, 72);
        if (logo != null) left.add(new JLabel(logo));

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel lbAura = new JLabel("AURA Portal");
        lbAura.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbAura.setForeground(Color.WHITE);

        JLabel lbTCU = new JLabel("Taguig City University – Enrollment & Admission");
        lbTCU.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbTCU.setForeground(new Color(255, 210, 210));

        titles.add(Box.createVerticalGlue());
        titles.add(lbAura);
        titles.add(lbTCU);
        titles.add(Box.createVerticalGlue());
        left.add(titles);

        // Right: user info + logout
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        right.setOpaque(false);
        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));

        JLabel lbName = new JLabel(currentUser.getFullName());
        lbName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbName.setForeground(Color.WHITE);
        lbName.setAlignmentX(RIGHT_ALIGNMENT);

        JLabel lbRole = new JLabel(currentUser.getRole().toUpperCase());
        lbRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbRole.setForeground(new Color(255, 200, 200));
        lbRole.setAlignmentX(RIGHT_ALIGNMENT);

        userInfo.add(Box.createVerticalGlue());
        userInfo.add(lbName);
        userInfo.add(lbRole);
        userInfo.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(UIHelper.F_SMALL);
        btnLogout.setForeground(UIHelper.RED);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(new CompoundBorder(
            new LineBorder(Color.WHITE, 1, true),
            new EmptyBorder(4, 12, 4, 12)));
        btnLogout.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "Logout from AURA?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) { dispose(); new LoginFrame().setVisible(true); }
        });

        right.add(userInfo);
        right.add(btnLogout);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ─── Center: menu cards ───────────────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(UIHelper.BG);
        center.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel grid = new JPanel(new GridLayout(1, 2, 24, 0));
        grid.setBackground(UIHelper.BG);

        // Card 1 – Admission Form
        grid.add(menuCard(
            "📋  Admission Form",
            "Fill out your personal information and academic background to apply for admission.",
            "Open Form",
            e -> new AdmissionFormFrame(currentUser).setVisible(true)
        ));

        // Card 2 – Requirements
        grid.add(menuCard(
            "📁  Requirements",
            "Track and submit your required documents for enrollment (Form 138, Good Moral, etc.).",
            "Open Requirements",
            e -> new RequirementsFrame(currentUser).setVisible(true)
        ));

        center.add(grid);
        return center;
    }

    private JPanel menuCard(String title, String desc, String btnText,
                            java.awt.event.ActionListener action) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UIHelper.WHITE);
        p.setBorder(new CompoundBorder(
            new LineBorder(new Color(0xDDDDDD), 1, true),
            new EmptyBorder(28, 28, 28, 28)));

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lbTitle.setForeground(UIHelper.RED);
        lbTitle.setAlignmentX(LEFT_ALIGNMENT);

        JTextArea lbDesc = new JTextArea(desc);
        lbDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbDesc.setForeground(UIHelper.TEXT_GRAY);
        lbDesc.setEditable(false);
        lbDesc.setOpaque(false);
        lbDesc.setLineWrap(true);
        lbDesc.setWrapStyleWord(true);
        lbDesc.setAlignmentX(LEFT_ALIGNMENT);

        JButton btn = UIHelper.primaryBtn(btnText);
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addActionListener(action);

        p.add(lbTitle);
        p.add(Box.createVerticalStrut(12));
        p.add(lbDesc);
        p.add(Box.createVerticalGlue());
        p.add(Box.createVerticalStrut(20));
        p.add(btn);
        return p;
    }

    // ─── Footer ───────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.CENTER));
        f.setBackground(new Color(0xEEEEEE));
        f.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));
        JLabel lbl = new JLabel("AURA Portal © 2025  –  Taguig City University  •  Truth · Competence · Unity");
        lbl.setFont(UIHelper.F_SMALL);
        lbl.setForeground(UIHelper.TEXT_GRAY);
        f.add(lbl);
        return f;
    }
}
