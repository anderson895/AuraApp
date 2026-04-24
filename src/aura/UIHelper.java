package aura;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA – Shared UI helpers (TCU red/white theme).
 */
public final class UIHelper {

    // ── Brand colors ─────────────────────────────────────────
    public static final Color RED        = new Color(0xC8102E);
    public static final Color RED_DARK   = new Color(0x9B0B22);
    public static final Color WHITE      = Color.WHITE;
    public static final Color BG         = new Color(0xF4F4F4);
    public static final Color TEXT       = new Color(0x1A1A1A);
    public static final Color TEXT_GRAY  = new Color(0x666666);
    public static final Color BORDER     = new Color(0xCCCCCC);
    public static final Color GREEN      = new Color(0x28A745);
    public static final Color DANGER     = new Color(0xDC3545);

    // ── Fonts ────────────────────────────────────────────────
    public static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD,  26);
    public static final Font F_SECTION = new Font("Segoe UI", Font.BOLD,  15);
    public static final Font F_LABEL   = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font F_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font F_BTN     = new Font("Segoe UI", Font.BOLD,  14);

    private UIHelper() {}

    // ── Factories ─────────────────────────────────────────────

    public static JButton primaryBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(F_BTN);
        b.setBackground(RED);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 24, 10, 24));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(RED_DARK); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(RED); }
        });
        return b;
    }

    public static JButton outlineBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(F_BTN);
        b.setBackground(WHITE);
        b.setForeground(RED);
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(RED, 2));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new CompoundBorder(new LineBorder(RED, 2), new EmptyBorder(8, 22, 8, 22)));
        return b;
    }

    public static JTextField field(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(F_BODY);
        f.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(6, 9, 6, 9)));
        return f;
    }

    public static JPasswordField passField(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setFont(F_BODY);
        f.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(6, 9, 6, 9)));
        return f;
    }

    public static JTextArea textArea(int rows, int cols) {
        JTextArea a = new JTextArea(rows, cols);
        a.setFont(F_BODY);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(6, 9, 6, 9));
        return a;
    }

    public static <T> JComboBox<T> combo(T[] items) {
        JComboBox<T> c = new JComboBox<>(items);
        c.setFont(F_BODY);
        c.setBackground(WHITE);
        return c;
    }

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_SECTION);
        l.setForeground(RED);
        l.setBorder(new MatteBorder(0, 0, 2, 0, RED));
        return l;
    }

    /** Load an image from classpath and scale it. Returns null if missing. */
    public static ImageIcon icon(String name, int w, int h) {
        URL url = UIHelper.class.getClassLoader().getResource(name);
        if (url == null) return null;
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /** Wrap a component in a titled card panel. */
    public static JPanel card(String title, JComponent content) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(WHITE);
        p.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(16, 20, 16, 20)));
        if (title != null && !title.isEmpty()) {
            p.add(sectionLabel(title), BorderLayout.NORTH);
        }
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    /** Apply Nimbus look and feel. Used by every frame's main() so that
     *  running any single .java file directly opens with the same theme. */
    public static void applyNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    /** A throw-away guest User used when a frame is launched directly
     *  (NetBeans "Run File") without going through Login. */
    public static User guestUser() {
        return new User(0, "guest", "guest@aura.local", "Guest User", "student");
    }

    /** Same idea as guestUser() but with admin role for admin frames. */
    public static User guestAdmin() {
        return new User(0, "admin", "admin@aura.local", "Guest Admin", "admin");
    }

    /** Walk the component tree and force every JButton to be borderless. */
    public static void flattenButtons(Container root) {
        if (root == null) return;
        for (Component c : root.getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                b.setBorderPainted(false);
                b.setFocusPainted(false);
            }
            if (c instanceof Container) {
                flattenButtons((Container) c);
            }
        }
    }

    /** Open an uploaded document. Images render in a Swing dialog; anything
     *  else (PDF etc.) is handed to the OS via Desktop.open(). */
    public static void openDocument(Component parent, File file) {
        if (file == null || !file.exists()) {
            JOptionPane.showMessageDialog(parent,
                "File not found:\n" + (file == null ? "(null)" : file.getAbsolutePath()),
                "Missing file", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = file.getName().toLowerCase();
        boolean isImage = name.endsWith(".jpg") || name.endsWith(".jpeg")
                       || name.endsWith(".png") || name.endsWith(".gif")
                       || name.endsWith(".bmp");
        if (isImage) {
            showImageDialog(parent, file);
            return;
        }
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(parent,
                    "Cannot open on this platform. File is at:\n" + file.getAbsolutePath(),
                    "Open file", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent,
                "Unable to open file:\n" + ex.getMessage(),
                "Open failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showImageDialog(Component parent, File file) {
        ImageIcon raw = new ImageIcon(file.getAbsolutePath());
        int w = raw.getIconWidth();
        int h = raw.getIconHeight();
        int maxW = 800, maxH = 600;
        if (w > maxW || h > maxH) {
            double s = Math.min(maxW / (double) w, maxH / (double) h);
            w = (int) (w * s);
            h = (int) (h * s);
            raw = new ImageIcon(raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        }
        JLabel pic = new JLabel(raw);
        JScrollPane sp = new JScrollPane(pic);
        sp.setPreferredSize(new Dimension(Math.min(w + 40, maxW + 40),
                                          Math.min(h + 40, maxH + 40)));
        JOptionPane.showMessageDialog(parent, sp, file.getName(),
            JOptionPane.PLAIN_MESSAGE);
    }
}
