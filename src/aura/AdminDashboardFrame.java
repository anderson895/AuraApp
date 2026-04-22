package aura;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA - Admin Dashboard (4 modules)
 */
public class AdminDashboardFrame extends JFrame {

    private final User currentUser;

    public AdminDashboardFrame(User user) {
        this.currentUser = user;
        setTitle("AURA - Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(860, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            Image bg;
            { URL u = getClass().getClassLoader().getResource("banner.jpg");
              if (u != null) bg = new ImageIcon(u).getImage(); }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                else { g.setColor(UIHelper.RED); g.fillRect(0, 0, getWidth(), getHeight()); }
                g.setColor(new Color(0, 0, 0, 140));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(860, 120));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        left.setOpaque(false);
        ImageIcon logo = UIHelper.icon("logo.jpg", 68, 68);
        if (logo != null) left.add(new JLabel(logo));

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel lbTitle = new JLabel("AURA Admin Panel");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbTitle.setForeground(Color.WHITE);

        JLabel lbSub = new JLabel("Manage applicants, students, subjects and reports");
        lbSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbSub.setForeground(new Color(255, 210, 210));

        titles.add(Box.createVerticalGlue());
        titles.add(lbTitle);
        titles.add(lbSub);
        titles.add(Box.createVerticalGlue());
        left.add(titles);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        right.setOpaque(false);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel lbName = new JLabel(currentUser.getFullName());
        lbName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbName.setForeground(Color.WHITE);
        lbName.setAlignmentX(RIGHT_ALIGNMENT);
        JLabel lbRole = new JLabel("ADMINISTRATOR");
        lbRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbRole.setForeground(new Color(255, 200, 200));
        lbRole.setAlignmentX(RIGHT_ALIGNMENT);
        info.add(Box.createVerticalGlue());
        info.add(lbName);
        info.add(lbRole);
        info.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(UIHelper.F_SMALL);
        btnLogout.setForeground(UIHelper.RED);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(new CompoundBorder(
            new LineBorder(Color.WHITE, 1, true),
            new EmptyBorder(4, 12, 4, 12)));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "Logout from AURA?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) { dispose(); new LoginFrame().setVisible(true); }
        });

        right.add(info);
        right.add(btnLogout);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(UIHelper.BG);
        center.setBorder(new EmptyBorder(24, 40, 24, 40));

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(UIHelper.BG);

        grid.add(card("Applicants",
            "Review, approve or reject admission applications from students.",
            "Open Applicants",
            e -> new AdminApplicantsFrame(currentUser).setVisible(true)));

        grid.add(card("Students",
            "View and manage registered student accounts.",
            "Manage Students",
            e -> new AdminStudentsFrame(currentUser).setVisible(true)));

        grid.add(card("Courses / Subjects",
            "Add, edit or remove subjects in the course catalog.",
            "Manage Subjects",
            e -> new AdminSubjectsFrame(currentUser).setVisible(true)));

        grid.add(card("Reports",
            "Summary of applicants, approvals, enrollments and activity.",
            "View Reports",
            e -> new AdminReportsFrame(currentUser).setVisible(true)));

        center.add(grid);
        return center;
    }

    private JPanel card(String title, String desc, String btnText,
                        java.awt.event.ActionListener action) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UIHelper.WHITE);
        p.setBorder(new CompoundBorder(
            new LineBorder(new Color(0xDDDDDD), 1, true),
            new EmptyBorder(22, 24, 22, 24)));

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lbTitle.setForeground(UIHelper.RED);
        lbTitle.setAlignmentX(LEFT_ALIGNMENT);

        JTextArea lbDesc = new JTextArea(desc);
        lbDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
        p.add(Box.createVerticalStrut(10));
        p.add(lbDesc);
        p.add(Box.createVerticalGlue());
        p.add(Box.createVerticalStrut(16));
        p.add(btn);
        return p;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.CENTER));
        f.setBackground(new Color(0xEEEEEE));
        f.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));
        JLabel lbl = new JLabel("AURA Portal - Taguig City University - Admin");
        lbl.setFont(UIHelper.F_SMALL);
        lbl.setForeground(UIHelper.TEXT_GRAY);
        f.add(lbl);
        return f;
    }
}
