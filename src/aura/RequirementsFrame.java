package aura;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * AURA – Requirements Submission Tracker
 * Lets the student tick off and upload each required document
 * (2x2 photo, birth cert, etc.). Files are copied into uploads/<user_id>/.
 */
public class RequirementsFrame extends javax.swing.JFrame {

    private final User user;

    // Database column suffixes (req_<key>, file_<key>) and display info
    // {key, title, description}
    private static final String[][] REQS = {
        {"form138",             "Form 138 (Report Card)",        "Photocopy of latest Report Card"},
        {"good_moral",          "Certificate of Good Moral",     "Original Certificate of Good Moral Character"},
        {"birth_cert",          "PSA Birth Certificate",         "Original or certified true copy of Birth Certificate"},
        {"id_photo",            "2x2 ID Photos (4 pcs)",         "Recent ID photos with white background"},
        {"medical_cert",        "Medical Certificate",           "Issued by a licensed physician within 6 months"},
        {"transcript",          "Transcript of Records",         "Official Transcript (for transferees)"},
        {"honorable_dismissal", "Honorable Dismissal",           "Required for transferees from other schools"}
    };

    private final JCheckBox[] cbs       = new JCheckBox[REQS.length];
    private final JLabel[]    lblFiles  = new JLabel[REQS.length];
    private final String[]    filePaths = new String[REQS.length];

    public RequirementsFrame() {
        this(UIHelper.guestUser());
    }

    public RequirementsFrame(User user) {
        this.user = user;
        initComponents();
        customInit();
        loadExisting();
    }

    /** Apply theme + populate the dynamic requirement rows. */
    private void customInit() {
        setSize(820, 680);
        setLocationRelativeTo(null);

        lblUser.setText("Student: " + user.getFullName());

        // Banner border (rounded yellow)
        pnlInfoBanner.setBorder(new CompoundBorder(
            new LineBorder(new Color(0xFFC107), 1, true),
            new EmptyBorder(8, 12, 8, 12)));

        // Requirements panel card styling + dynamic rows
        pnlRequirements.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(16, 20, 16, 20)));
        pnlRequirements.setLayout(new BoxLayout(pnlRequirements, BoxLayout.Y_AXIS));

        JLabel lbHead = UIHelper.sectionLabel("Required Documents");
        lbHead.setAlignmentX(LEFT_ALIGNMENT);
        pnlRequirements.add(lbHead);
        pnlRequirements.add(Box.createVerticalStrut(14));

        for (int i = 0; i < REQS.length; i++) {
            buildReqRow(pnlRequirements, i);
        }

        // Remarks card styling
        pnlRemarks.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(4, 4, 4, 4)));
        scrollRemarks.setBorder(new LineBorder(UIHelper.BORDER, 1));

        // Bottom bar separator
        bottomBar.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));

        // Style buttons via UIHelper (keeps red/white theme + hover effects)
        applyPrimary(btnSave);
        applyPrimary(btnSubmit);
        applyOutline(btnBack);

        scrollBody.getVerticalScrollBar().setUnitIncrement(14);
    }

    private void applyPrimary(JButton b) {
        b.setBackground(UIHelper.RED);
        b.setForeground(UIHelper.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(UIHelper.RED_DARK); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(UIHelper.RED); }
        });
    }

    private void applyOutline(JButton b) {
        b.setBackground(UIHelper.WHITE);
        b.setForeground(UIHelper.RED);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new CompoundBorder(new LineBorder(UIHelper.RED, 2), new EmptyBorder(8, 18, 8, 18)));
    }

    /** Build one requirement row: checkbox, title/desc, file label, upload/clear buttons. */
    private void buildReqRow(JPanel parent, int idx) {
        final String key   = REQS[idx][0];
        final String title = REQS[idx][1];
        final String desc  = REQS[idx][2];

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JCheckBox cb = new JCheckBox();
        cb.setOpaque(false);
        cb.setPreferredSize(new Dimension(24, 24));
        cbs[idx] = cb;

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(UIHelper.F_LABEL);
        lbTitle.setForeground(UIHelper.TEXT);

        JLabel lbDesc = new JLabel(desc);
        lbDesc.setFont(UIHelper.F_SMALL);
        lbDesc.setForeground(UIHelper.TEXT_GRAY);

        JLabel lbFile = new JLabel("(no file uploaded)");
        lbFile.setFont(UIHelper.F_SMALL);
        lbFile.setForeground(UIHelper.TEXT_GRAY);
        lblFiles[idx] = lbFile;

        textBox.add(lbTitle);
        textBox.add(lbDesc);
        textBox.add(lbFile);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        right.setOpaque(false);
        JButton btnUpload = UIHelper.outlineBtn("Upload");
        btnUpload.setFont(UIHelper.F_SMALL);
        btnUpload.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.RED, 1), new EmptyBorder(4, 10, 4, 10)));
        btnUpload.addActionListener(e -> chooseFile(key, idx));

        JButton btnClear = UIHelper.outlineBtn("Clear");
        btnClear.setFont(UIHelper.F_SMALL);
        btnClear.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1), new EmptyBorder(4, 10, 4, 10)));
        btnClear.setForeground(UIHelper.TEXT_GRAY);
        btnClear.addActionListener(e -> {
            filePaths[idx] = null;
            lbFile.setText("(no file uploaded)");
            lbFile.setForeground(UIHelper.TEXT_GRAY);
        });

        right.add(btnUpload);
        right.add(btnClear);

        row.add(cb, BorderLayout.WEST);
        row.add(textBox, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(row, BorderLayout.CENTER);
        wrapper.add(new JSeparator(), BorderLayout.SOUTH);
        wrapper.setAlignmentX(LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        parent.add(wrapper);
    }

    /** Open a file chooser, copy the picked file into uploads/<user_id>/req_<key>.<ext>. */
    private void chooseFile(String key, int idx) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select file for: " + REQS[idx][1]);
        fc.setFileFilter(new FileNameExtensionFilter(
            "Documents (jpg, jpeg, png, pdf)", "jpg", "jpeg", "png", "pdf"));
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File src = fc.getSelectedFile();
        if (src == null || !src.exists()) return;

        try {
            String ext = "";
            int dot = src.getName().lastIndexOf('.');
            if (dot > 0) ext = src.getName().substring(dot);

            Path dir = Paths.get("uploads", String.valueOf(user.getId()));
            Files.createDirectories(dir);
            Path dest = dir.resolve("req_" + key + ext);
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

            filePaths[idx] = dest.toString();
            cbs[idx].setSelected(true);
            lblFiles[idx].setText("File: " + src.getName());
            lblFiles[idx].setForeground(UIHelper.GREEN);
            setMsg("Uploaded: " + src.getName(), true);
        } catch (IOException ex) {
            setMsg("Upload failed: " + ex.getMessage(), false);
        }
    }

    // ─── Save / load ──────────────────────────────────────────

    private void save(boolean submit) {
        try (Connection c = DatabaseConnection.getConnection()) {
            PreparedStatement chk = c.prepareStatement(
                "SELECT id FROM requirements WHERE user_id=?");
            chk.setInt(1, user.getId());
            ResultSet rs = chk.executeQuery();
            boolean exists = rs.next();

            String sql;
            if (exists) {
                sql = "UPDATE requirements SET " +
                      "req_form138=?,req_good_moral=?,req_birth_cert=?,req_id_photo=?," +
                      "req_medical_cert=?,req_transcript=?,req_honorable_dismissal=?," +
                      "file_form138=?,file_good_moral=?,file_birth_cert=?,file_id_photo=?," +
                      "file_medical_cert=?,file_transcript=?,file_honorable_dismissal=?," +
                      "remarks=? WHERE user_id=?";
            } else {
                sql = "INSERT INTO requirements (" +
                      "req_form138,req_good_moral,req_birth_cert,req_id_photo," +
                      "req_medical_cert,req_transcript,req_honorable_dismissal," +
                      "file_form138,file_good_moral,file_birth_cert,file_id_photo," +
                      "file_medical_cert,file_transcript,file_honorable_dismissal," +
                      "remarks,user_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            PreparedStatement ps = c.prepareStatement(sql);
            for (int i = 0; i < REQS.length; i++) {
                ps.setInt(i + 1, cbs[i].isSelected() ? 1 : 0);
            }
            for (int i = 0; i < REQS.length; i++) {
                ps.setString(REQS.length + i + 1, filePaths[i]);
            }
            ps.setString(REQS.length * 2 + 1, taRemarks.getText().trim());
            ps.setInt(REQS.length * 2 + 2, user.getId());
            ps.executeUpdate();

            if (submit) {
                int count = countChecked();
                JOptionPane.showMessageDialog(this,
                    "Requirements submitted!\n\n" +
                    "You have marked " + count + " of " + REQS.length + " documents as ready.\n" +
                    (count < REQS.length ? "Please complete the remaining " + (REQS.length - count) + " document(s)." : ""),
                    "Requirements Submitted", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                setMsg("Saved successfully.", true);
            }

        } catch (SQLException ex) {
            setMsg("DB Error: " + ex.getMessage()
                + " — did you run the latest aura_db.sql to add file columns?", false);
        }
    }

    private void loadExisting() {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT * FROM requirements WHERE user_id=?")) {
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < REQS.length; i++) {
                    cbs[i].setSelected(rs.getInt("req_" + REQS[i][0]) == 1);
                    String path = safeColumn(rs, "file_" + REQS[i][0]);
                    if (path != null && !path.isEmpty()) {
                        filePaths[i] = path;
                        File f = new File(path);
                        lblFiles[i].setText("File: " + f.getName());
                        lblFiles[i].setForeground(UIHelper.GREEN);
                    }
                }
                String rem = rs.getString("remarks");
                if (rem != null) taRemarks.setText(rem);
                setMsg("Existing requirements loaded.", true);
            }
        } catch (SQLException ignored) {}
    }

    private String safeColumn(ResultSet rs, String col) {
        try { return rs.getString(col); }
        catch (SQLException ex) { return null; }
    }

    private int countChecked() {
        int n = 0;
        for (JCheckBox cb : cbs) if (cb.isSelected()) n++;
        return n;
    }

    private void setMsg(String text, boolean ok) {
        lblStatus.setText(text);
        lblStatus.setForeground(ok ? UIHelper.GREEN : UIHelper.DANGER);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topBar = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        scrollBody = new javax.swing.JScrollPane();
        pnlBody = new javax.swing.JPanel();
        pnlInfoBanner = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        pnlRequirements = new javax.swing.JPanel();
        pnlRemarks = new javax.swing.JPanel();
        lblRemarks = new javax.swing.JLabel();
        scrollRemarks = new javax.swing.JScrollPane();
        taRemarks = new javax.swing.JTextArea();
        bottomBar = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AURA – Requirements Checklist");

        topBar.setBackground(new java.awt.Color(200, 16, 46));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Requirements Checklist");

        lblUser.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblUser.setForeground(new java.awt.Color(255, 210, 210));
        lblUser.setText("Student: ");

        javax.swing.GroupLayout topBarLayout = new javax.swing.GroupLayout(topBar);
        topBar.setLayout(topBarLayout);
        topBarLayout.setHorizontalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topBarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                .addComponent(lblUser)
                .addGap(20, 20, 20))
        );
        topBarLayout.setVerticalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topBarLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(lblUser))
                .addGap(12, 12, 12))
        );

        scrollBody.setBorder(null);

        pnlBody.setBackground(new java.awt.Color(244, 244, 244));

        pnlInfoBanner.setBackground(new java.awt.Color(255, 243, 205));

        lblInfo.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblInfo.setForeground(new java.awt.Color(133, 100, 4));
        lblInfo.setText("<html>Tick the box and click <b>Upload</b> for each document. Accepted file types: JPG, PNG, PDF.</html>");

        javax.swing.GroupLayout pnlInfoBannerLayout = new javax.swing.GroupLayout(pnlInfoBanner);
        pnlInfoBanner.setLayout(pnlInfoBannerLayout);
        pnlInfoBannerLayout.setHorizontalGroup(
            pnlInfoBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoBannerLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblInfo)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        pnlInfoBannerLayout.setVerticalGroup(
            pnlInfoBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoBannerLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblInfo)
                .addGap(8, 8, 8))
        );

        pnlRequirements.setBackground(new java.awt.Color(255, 255, 255));
        pnlRequirements.setLayout(new javax.swing.BoxLayout(pnlRequirements, javax.swing.BoxLayout.Y_AXIS));

        pnlRemarks.setBackground(new java.awt.Color(255, 255, 255));

        lblRemarks.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblRemarks.setForeground(new java.awt.Color(200, 16, 46));
        lblRemarks.setText("Remarks / Notes");

        taRemarks.setColumns(40);
        taRemarks.setLineWrap(true);
        taRemarks.setRows(3);
        taRemarks.setWrapStyleWord(true);
        scrollRemarks.setViewportView(taRemarks);

        javax.swing.GroupLayout pnlRemarksLayout = new javax.swing.GroupLayout(pnlRemarks);
        pnlRemarks.setLayout(pnlRemarksLayout);
        pnlRemarksLayout.setHorizontalGroup(
            pnlRemarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRemarksLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlRemarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRemarks)
                    .addComponent(scrollRemarks))
                .addGap(18, 18, 18))
        );
        pnlRemarksLayout.setVerticalGroup(
            pnlRemarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRemarksLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblRemarks)
                .addGap(8, 8, 8)
                .addComponent(scrollRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout pnlBodyLayout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(pnlBodyLayout);
        pnlBodyLayout.setHorizontalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInfoBanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlRequirements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlRemarks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        pnlBodyLayout.setVerticalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(pnlInfoBanner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(pnlRequirements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(pnlRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        scrollBody.setViewportView(pnlBody);

        bottomBar.setBackground(new java.awt.Color(255, 255, 255));

        lblStatus.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(220, 53, 69));
        lblStatus.setText(" ");

        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(200, 16, 46));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(200, 16, 46));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnSubmit.setBackground(new java.awt.Color(200, 16, 46));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("Submit Requirements");
        btnSubmit.setBorderPainted(false);
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bottomBarLayout = new javax.swing.GroupLayout(bottomBar);
        bottomBar.setLayout(bottomBarLayout);
        bottomBarLayout.setHorizontalGroup(
            bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomBarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(10, 10, 10)
                .addComponent(btnSave)
                .addGap(10, 10, 10)
                .addComponent(btnSubmit)
                .addGap(16, 16, 16))
        );
        bottomBarLayout.setVerticalGroup(
            bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomBarLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(btnBack)
                    .addComponent(btnSave)
                    .addComponent(btnSubmit))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bottomBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(topBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(scrollBody, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                .addComponent(bottomBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save(false);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        save(true);
    }//GEN-LAST:event_btnSubmitActionPerformed

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        EventQueue.invokeLater(() ->
            new RequirementsFrame(UIHelper.guestUser()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JPanel bottomBar;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblRemarks;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlInfoBanner;
    private javax.swing.JPanel pnlRemarks;
    private javax.swing.JPanel pnlRequirements;
    private javax.swing.JScrollPane scrollBody;
    private javax.swing.JScrollPane scrollRemarks;
    private javax.swing.JTextArea taRemarks;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables
}
