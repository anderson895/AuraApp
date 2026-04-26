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

    private static final String[][] REQS = {
        {"form138",             "Form 138 (Report Card)",        "Photocopy of latest Report Card"},
        {"good_moral",          "Certificate of Good Moral",     "Original Certificate of Good Moral Character"},
        {"birth_cert",          "PSA Birth Certificate",         "Original or certified true copy of Birth Certificate"},
        {"id_photo",            "2x2 ID Photos (4 pcs)",         "Recent ID photos with white background"},
        {"medical_cert",        "Medical Certificate",           "Issued by a licensed physician within 6 months"},
        {"transcript",          "Transcript of Records",         "Official Transcript (for transferees)"},
        {"honorable_dismissal", "Honorable Dismissal",           "Required for transferees from other schools"}
    };

    private JCheckBox[] cbs;
    private JLabel[]    lblFiles;
    private final String[] filePaths = new String[REQS.length];

    public RequirementsFrame() {
        this(UIHelper.guestUser());
    }

    public RequirementsFrame(User user) {
        this.user = user;
        initComponents();
        customInit();
        loadExisting();
        UIHelper.flattenButtons(getContentPane());
    }

    private void customInit() {
        setSize(820, 760);
        setLocationRelativeTo(null);

        lblUser.setText("Student: " + user.getFullName());

        pnlInfoBanner.setBorder(new CompoundBorder(
            new LineBorder(new Color(0xFFC107), 1, true),
            new EmptyBorder(8, 12, 8, 12)));

        pnlRequirements.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(16, 20, 16, 20)));
        lblReqHeader.setBorder(new MatteBorder(0, 0, 2, 0, UIHelper.RED));

        pnlRemarks.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(4, 4, 4, 4)));
        scrollRemarks.setBorder(new LineBorder(UIHelper.BORDER, 1));

        bottomBar.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));

        applyPrimary(btnSave);
        applyPrimary(btnSubmit);
        applyOutline(btnBack);

        scrollBody.getVerticalScrollBar().setUnitIncrement(14);

        cbs = new JCheckBox[]{ cbReq1, cbReq2, cbReq3, cbReq4, cbReq5, cbReq6, cbReq7 };
        lblFiles = new JLabel[]{ lblFile1, lblFile2, lblFile3, lblFile4, lblFile5, lblFile6, lblFile7 };
        JButton[] uploads = { btnUpload1, btnUpload2, btnUpload3, btnUpload4, btnUpload5, btnUpload6, btnUpload7 };
        JButton[] views   = { btnView1,   btnView2,   btnView3,   btnView4,   btnView5,   btnView6,   btnView7   };
        JButton[] clears  = { btnClear1,  btnClear2,  btnClear3,  btnClear4,  btnClear5,  btnClear6,  btnClear7  };

        for (int i = 0; i < REQS.length; i++) {
            final int idx = i;
            styleRowButton(uploads[i], false);
            styleRowButton(views[i],   false);
            styleRowButton(clears[i],  true);

            uploads[i].addActionListener(e -> chooseFile(REQS[idx][0], idx));
            views[i].addActionListener(e -> {
                if (filePaths[idx] == null || filePaths[idx].isEmpty()) {
                    setMsg("No file uploaded yet for: " + REQS[idx][1], false);
                    return;
                }
                UIHelper.openDocument(this, new File(filePaths[idx]));
            });
            clears[i].addActionListener(e -> {
                filePaths[idx] = null;
                lblFiles[idx].setText("(no file uploaded)");
                lblFiles[idx].setForeground(UIHelper.TEXT_GRAY);
            });
        }
    }

    private void styleRowButton(JButton b, boolean muted) {
        Color edge = muted ? UIHelper.BORDER : UIHelper.RED;
        Color fg   = muted ? UIHelper.TEXT_GRAY : UIHelper.RED;
        b.setBackground(UIHelper.WHITE);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new CompoundBorder(new LineBorder(edge, 1), new EmptyBorder(4, 10, 4, 10)));
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
        lblReqHeader = new javax.swing.JLabel();
        pnlReq1 = new javax.swing.JPanel();
        cbReq1 = new javax.swing.JCheckBox();
        lblTitle1 = new javax.swing.JLabel();
        lblDesc1 = new javax.swing.JLabel();
        lblFile1 = new javax.swing.JLabel();
        btnUpload1 = new javax.swing.JButton();
        btnView1 = new javax.swing.JButton();
        btnClear1 = new javax.swing.JButton();
        sepReq1 = new javax.swing.JSeparator();
        pnlReq2 = new javax.swing.JPanel();
        cbReq2 = new javax.swing.JCheckBox();
        lblTitle2 = new javax.swing.JLabel();
        lblDesc2 = new javax.swing.JLabel();
        lblFile2 = new javax.swing.JLabel();
        btnUpload2 = new javax.swing.JButton();
        btnView2 = new javax.swing.JButton();
        btnClear2 = new javax.swing.JButton();
        sepReq2 = new javax.swing.JSeparator();
        pnlReq3 = new javax.swing.JPanel();
        cbReq3 = new javax.swing.JCheckBox();
        lblTitle3 = new javax.swing.JLabel();
        lblDesc3 = new javax.swing.JLabel();
        lblFile3 = new javax.swing.JLabel();
        btnUpload3 = new javax.swing.JButton();
        btnView3 = new javax.swing.JButton();
        btnClear3 = new javax.swing.JButton();
        sepReq3 = new javax.swing.JSeparator();
        pnlReq4 = new javax.swing.JPanel();
        cbReq4 = new javax.swing.JCheckBox();
        lblTitle4 = new javax.swing.JLabel();
        lblDesc4 = new javax.swing.JLabel();
        lblFile4 = new javax.swing.JLabel();
        btnUpload4 = new javax.swing.JButton();
        btnView4 = new javax.swing.JButton();
        btnClear4 = new javax.swing.JButton();
        sepReq4 = new javax.swing.JSeparator();
        pnlReq5 = new javax.swing.JPanel();
        cbReq5 = new javax.swing.JCheckBox();
        lblTitle5 = new javax.swing.JLabel();
        lblDesc5 = new javax.swing.JLabel();
        lblFile5 = new javax.swing.JLabel();
        btnUpload5 = new javax.swing.JButton();
        btnView5 = new javax.swing.JButton();
        btnClear5 = new javax.swing.JButton();
        sepReq5 = new javax.swing.JSeparator();
        pnlReq6 = new javax.swing.JPanel();
        cbReq6 = new javax.swing.JCheckBox();
        lblTitle6 = new javax.swing.JLabel();
        lblDesc6 = new javax.swing.JLabel();
        lblFile6 = new javax.swing.JLabel();
        btnUpload6 = new javax.swing.JButton();
        btnView6 = new javax.swing.JButton();
        btnClear6 = new javax.swing.JButton();
        sepReq6 = new javax.swing.JSeparator();
        pnlReq7 = new javax.swing.JPanel();
        cbReq7 = new javax.swing.JCheckBox();
        lblTitle7 = new javax.swing.JLabel();
        lblDesc7 = new javax.swing.JLabel();
        lblFile7 = new javax.swing.JLabel();
        btnUpload7 = new javax.swing.JButton();
        btnView7 = new javax.swing.JButton();
        btnClear7 = new javax.swing.JButton();
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

        lblReqHeader.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblReqHeader.setForeground(new java.awt.Color(200, 16, 46));
        lblReqHeader.setText("Required Documents");

        pnlReq1.setOpaque(false);
        cbReq1.setOpaque(false);
        lblTitle1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTitle1.setForeground(new java.awt.Color(26, 26, 26));
        lblTitle1.setText("Form 138 (Report Card)");
        lblDesc1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDesc1.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc1.setText("Photocopy of latest Report Card");
        lblFile1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblFile1.setForeground(new java.awt.Color(102, 102, 102));
        lblFile1.setText("(no file uploaded)");
        btnUpload1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnUpload1.setText("Upload");
        btnView1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnView1.setText("View");
        btnClear1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnClear1.setText("Clear");
        layoutReqRow(pnlReq1, cbReq1, lblTitle1, lblDesc1, lblFile1,
            btnUpload1, btnView1, btnClear1, sepReq1);

        pnlReq2.setOpaque(false);
        cbReq2.setOpaque(false);
        lblTitle2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTitle2.setForeground(new java.awt.Color(26, 26, 26));
        lblTitle2.setText("Certificate of Good Moral");
        lblDesc2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDesc2.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc2.setText("Original Certificate of Good Moral Character");
        lblFile2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblFile2.setForeground(new java.awt.Color(102, 102, 102));
        lblFile2.setText("(no file uploaded)");
        btnUpload2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnUpload2.setText("Upload");
        btnView2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnView2.setText("View");
        btnClear2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnClear2.setText("Clear");
        layoutReqRow(pnlReq2, cbReq2, lblTitle2, lblDesc2, lblFile2,
            btnUpload2, btnView2, btnClear2, sepReq2);

        pnlReq3.setOpaque(false);
        cbReq3.setOpaque(false);
        lblTitle3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTitle3.setForeground(new java.awt.Color(26, 26, 26));
        lblTitle3.setText("PSA Birth Certificate");
        lblDesc3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDesc3.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc3.setText("Original or certified true copy of Birth Certificate");
        lblFile3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblFile3.setForeground(new java.awt.Color(102, 102, 102));
        lblFile3.setText("(no file uploaded)");
        btnUpload3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnUpload3.setText("Upload");
        btnView3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnView3.setText("View");
        btnClear3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnClear3.setText("Clear");
        layoutReqRow(pnlReq3, cbReq3, lblTitle3, lblDesc3, lblFile3,
            btnUpload3, btnView3, btnClear3, sepReq3);

        pnlReq4.setOpaque(false);
        cbReq4.setOpaque(false);
        lblTitle4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTitle4.setForeground(new java.awt.Color(26, 26, 26));
        lblTitle4.setText("2x2 ID Photos (4 pcs)");
        lblDesc4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDesc4.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc4.setText("Recent ID photos with white background");
        lblFile4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblFile4.setForeground(new java.awt.Color(102, 102, 102));
        lblFile4.setText("(no file uploaded)");
        btnUpload4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnUpload4.setText("Upload");
        btnView4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnView4.setText("View");
        btnClear4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnClear4.setText("Clear");
        layoutReqRow(pnlReq4, cbReq4, lblTitle4, lblDesc4, lblFile4,
            btnUpload4, btnView4, btnClear4, sepReq4);

        pnlReq5.setOpaque(false);
        cbReq5.setOpaque(false);
        lblTitle5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTitle5.setForeground(new java.awt.Color(26, 26, 26));
        lblTitle5.setText("Medical Certificate");
        lblDesc5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDesc5.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc5.setText("Issued by a licensed physician within 6 months");
        lblFile5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblFile5.setForeground(new java.awt.Color(102, 102, 102));
        lblFile5.setText("(no file uploaded)");
        btnUpload5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnUpload5.setText("Upload");
        btnView5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnView5.setText("View");
        btnClear5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnClear5.setText("Clear");
        layoutReqRow(pnlReq5, cbReq5, lblTitle5, lblDesc5, lblFile5,
            btnUpload5, btnView5, btnClear5, sepReq5);

        pnlReq6.setOpaque(false);
        cbReq6.setOpaque(false);
        lblTitle6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTitle6.setForeground(new java.awt.Color(26, 26, 26));
        lblTitle6.setText("Transcript of Records");
        lblDesc6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDesc6.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc6.setText("Official Transcript (for transferees)");
        lblFile6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblFile6.setForeground(new java.awt.Color(102, 102, 102));
        lblFile6.setText("(no file uploaded)");
        btnUpload6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnUpload6.setText("Upload");
        btnView6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnView6.setText("View");
        btnClear6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnClear6.setText("Clear");
        layoutReqRow(pnlReq6, cbReq6, lblTitle6, lblDesc6, lblFile6,
            btnUpload6, btnView6, btnClear6, sepReq6);

        pnlReq7.setOpaque(false);
        cbReq7.setOpaque(false);
        lblTitle7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTitle7.setForeground(new java.awt.Color(26, 26, 26));
        lblTitle7.setText("Honorable Dismissal");
        lblDesc7.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDesc7.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc7.setText("Required for transferees from other schools");
        lblFile7.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblFile7.setForeground(new java.awt.Color(102, 102, 102));
        lblFile7.setText("(no file uploaded)");
        btnUpload7.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnUpload7.setText("Upload");
        btnView7.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnView7.setText("View");
        btnClear7.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnClear7.setText("Clear");
        layoutReqRow(pnlReq7, cbReq7, lblTitle7, lblDesc7, lblFile7,
            btnUpload7, btnView7, btnClear7, null);

        javax.swing.GroupLayout pnlRequirementsLayout = new javax.swing.GroupLayout(pnlRequirements);
        pnlRequirements.setLayout(pnlRequirementsLayout);
        pnlRequirementsLayout.setHorizontalGroup(
            pnlRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRequirementsLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReqHeader)
                    .addComponent(pnlReq1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlReq2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlReq3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlReq4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlReq5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlReq6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlReq7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        pnlRequirementsLayout.setVerticalGroup(
            pnlRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRequirementsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblReqHeader)
                .addGap(14, 14, 14)
                .addComponent(pnlReq1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlReq2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlReq3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlReq4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlReq5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlReq6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlReq7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

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

    /**
     * Lays out a single requirement row to match the .form XML exactly.
     * Why: the per-row GroupLayout is identical for all 7 rows; the .form
     * already declares the layout per panel, so this helper produces the
     * same runtime layout the NetBeans-generated code would.
     */
    private void layoutReqRow(JPanel row, JCheckBox cb,
            JLabel lbTitle, JLabel lbDesc, JLabel lbFile,
            JButton btnUp, JButton btnVw, JButton btnClr,
            JSeparator sep) {
        GroupLayout gl = new GroupLayout(row);
        row.setLayout(gl);

        gl.setHorizontalGroup(
            gl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl.createSequentialGroup()
                .addComponent(cb)
                .addGap(12, 12, 12)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lbTitle)
                    .addComponent(lbDesc)
                    .addComponent(lbFile))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnUp)
                .addGap(4, 4, 4)
                .addComponent(btnVw)
                .addGap(4, 4, 4)
                .addComponent(btnClr))
        );

        GroupLayout.SequentialGroup vMain = gl.createSequentialGroup()
            .addGap(6, 6, 6)
            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(cb)
                .addGroup(gl.createSequentialGroup()
                    .addComponent(lbTitle)
                    .addGap(2, 2, 2)
                    .addComponent(lbDesc)
                    .addGap(2, 2, 2)
                    .addComponent(lbFile))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUp)
                    .addComponent(btnVw)
                    .addComponent(btnClr)))
            .addGap(6, 6, 6);

        if (sep != null) {
            vMain.addComponent(sep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        }

        gl.setVerticalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(vMain));
    }

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
    private javax.swing.JButton btnClear1;
    private javax.swing.JButton btnClear2;
    private javax.swing.JButton btnClear3;
    private javax.swing.JButton btnClear4;
    private javax.swing.JButton btnClear5;
    private javax.swing.JButton btnClear6;
    private javax.swing.JButton btnClear7;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnUpload1;
    private javax.swing.JButton btnUpload2;
    private javax.swing.JButton btnUpload3;
    private javax.swing.JButton btnUpload4;
    private javax.swing.JButton btnUpload5;
    private javax.swing.JButton btnUpload6;
    private javax.swing.JButton btnUpload7;
    private javax.swing.JButton btnView1;
    private javax.swing.JButton btnView2;
    private javax.swing.JButton btnView3;
    private javax.swing.JButton btnView4;
    private javax.swing.JButton btnView5;
    private javax.swing.JButton btnView6;
    private javax.swing.JButton btnView7;
    private javax.swing.JPanel bottomBar;
    private javax.swing.JCheckBox cbReq1;
    private javax.swing.JCheckBox cbReq2;
    private javax.swing.JCheckBox cbReq3;
    private javax.swing.JCheckBox cbReq4;
    private javax.swing.JCheckBox cbReq5;
    private javax.swing.JCheckBox cbReq6;
    private javax.swing.JCheckBox cbReq7;
    private javax.swing.JLabel lblDesc1;
    private javax.swing.JLabel lblDesc2;
    private javax.swing.JLabel lblDesc3;
    private javax.swing.JLabel lblDesc4;
    private javax.swing.JLabel lblDesc5;
    private javax.swing.JLabel lblDesc6;
    private javax.swing.JLabel lblDesc7;
    private javax.swing.JLabel lblFile1;
    private javax.swing.JLabel lblFile2;
    private javax.swing.JLabel lblFile3;
    private javax.swing.JLabel lblFile4;
    private javax.swing.JLabel lblFile5;
    private javax.swing.JLabel lblFile6;
    private javax.swing.JLabel lblFile7;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblRemarks;
    private javax.swing.JLabel lblReqHeader;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitle1;
    private javax.swing.JLabel lblTitle2;
    private javax.swing.JLabel lblTitle3;
    private javax.swing.JLabel lblTitle4;
    private javax.swing.JLabel lblTitle5;
    private javax.swing.JLabel lblTitle6;
    private javax.swing.JLabel lblTitle7;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlInfoBanner;
    private javax.swing.JPanel pnlRemarks;
    private javax.swing.JPanel pnlReq1;
    private javax.swing.JPanel pnlReq2;
    private javax.swing.JPanel pnlReq3;
    private javax.swing.JPanel pnlReq4;
    private javax.swing.JPanel pnlReq5;
    private javax.swing.JPanel pnlReq6;
    private javax.swing.JPanel pnlReq7;
    private javax.swing.JPanel pnlRequirements;
    private javax.swing.JScrollPane scrollBody;
    private javax.swing.JScrollPane scrollRemarks;
    private javax.swing.JSeparator sepReq1;
    private javax.swing.JSeparator sepReq2;
    private javax.swing.JSeparator sepReq3;
    private javax.swing.JSeparator sepReq4;
    private javax.swing.JSeparator sepReq5;
    private javax.swing.JSeparator sepReq6;
    private javax.swing.JTextArea taRemarks;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables
}
