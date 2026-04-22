package aura;

import javax.swing.*;

/**
 * AURA – Automated Enrollment & Admission Portal
 * Taguig City University
 * Entry point – launches the Login screen.
 */
public class Aura {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            if (!DatabaseConnection.testConnection()) {
                JOptionPane.showMessageDialog(null,
                    "⚠  Cannot connect to MySQL.\n\n" +
                    "Make sure:\n" +
                    "  1. XAMPP is running\n" +
                    "  2. MySQL service is started\n" +
                    "  3. You imported database/aura_db.sql in phpMyAdmin\n\n" +
                    "The app will still open but login will not work.",
                    "Database Warning", JOptionPane.WARNING_MESSAGE);
            }
            new LoginFrame().setVisible(true);
        });
    }
}
