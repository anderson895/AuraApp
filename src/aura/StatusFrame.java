package aura;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

/**
 * AURA - Student: view admission / requirements / enrollment status
 * using proper GUI components (cards, labels, table) instead of a
 * plain text area.
 */
public class StatusFrame extends javax.swing.JFrame {

    private final User user;
    private DefaultTableModel subjectsModel;

    public StatusFrame() {
        this(UIHelper.guestUser());
    }

    public StatusFrame(User user) {
        this.user = user;
        initComponents();
        customInit();
        loadStatus();
        UIHelper.flattenButtons(getContentPane());
    }

    private void customInit() {
        setSize(820, 700);
        setLocationRelativeTo(null);
        setTitle("AURA - My Status (" + user.getFullName() + ")");

        lblUser.setText("Account: " + user.getUsername() + " | " + user.getEmail());

        Border card = new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(14, 18, 18, 18));
        pnlAdmission.setBorder(card);
        pnlRequirements.setBorder(card);
        pnlEnrolled.setBorder(card);

        bottomBar.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));
        scrollBody.getVerticalScrollBar().setUnitIncrement(14);

        subjectsModel = new DefaultTableModel(
            new Object[] { "Code", "Title", "Units" }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSubjects.setModel(subjectsModel);
        tblSubjects.setRowHeight(24);
        tblSubjects.setFont(UIHelper.F_BODY);
        tblSubjects.getTableHeader().setFont(UIHelper.F_LABEL);
        tblSubjects.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblSubjects.getColumnModel().getColumn(1).setPreferredWidth(380);
        tblSubjects.getColumnModel().getColumn(2).setPreferredWidth(60);

        applyPrimary(btnRefresh);
        applyOutline(btnBack);
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

    private void loadStatus() {
        // Reset to defaults so refresh always reflects DB state
        valStatus.setText("Not yet submitted");
        valStatus.setForeground(UIHelper.TEXT_GRAY);
        valProgram.setText("—");
        valSchoolYear.setText("—");
        valSubmitted.setText("—");
        valRemarks.setText("—");

        setReq(valForm138,    0);
        setReq(valGoodMoral,  0);
        setReq(valBirthCert,  0);
        setReq(valIdPhoto,    0);
        setReq(valMedical,    0);
        setReq(valTranscript, 0);
        setReq(valHonorable,  0);
        valReqUpdated.setText("Not yet submitted");

        subjectsModel.setRowCount(0);
        valTotal.setText("Total: 0 subject(s), 0 unit(s)");

        if (user == null) return;

        try (Connection c = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT status,program,school_year,admin_remarks,submitted_at " +
                "FROM admission_forms WHERE user_id=?")) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String st = rs.getString("status");
                    valStatus.setText(st == null ? "—" : st);
                    valStatus.setForeground(statusColor(st));
                    valProgram.setText(str(rs.getString("program")));
                    valSchoolYear.setText(str(rs.getString("school_year")));
                    valSubmitted.setText(str(rs.getString("submitted_at")));
                    String rem = rs.getString("admin_remarks");
                    valRemarks.setText(rem == null || rem.isEmpty() ? "—" : rem);
                }
            }
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT req_form138,req_good_moral,req_birth_cert,req_id_photo," +
                "req_medical_cert,req_transcript,req_honorable_dismissal,updated_at " +
                "FROM requirements WHERE user_id=?")) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    setReq(valForm138,    rs.getInt("req_form138"));
                    setReq(valGoodMoral,  rs.getInt("req_good_moral"));
                    setReq(valBirthCert,  rs.getInt("req_birth_cert"));
                    setReq(valIdPhoto,    rs.getInt("req_id_photo"));
                    setReq(valMedical,    rs.getInt("req_medical_cert"));
                    setReq(valTranscript, rs.getInt("req_transcript"));
                    setReq(valHonorable,  rs.getInt("req_honorable_dismissal"));
                    valReqUpdated.setText(str(rs.getString("updated_at")));
                }
            }
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT s.code,s.title,s.units FROM enrollments e " +
                "JOIN subjects s ON s.id=e.subject_id WHERE e.user_id=? ORDER BY s.code")) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                int n = 0, units = 0;
                while (rs.next()) {
                    n++;
                    int u = rs.getInt("units");
                    units += u;
                    subjectsModel.addRow(new Object[] {
                        rs.getString("code"), rs.getString("title"), u });
                }
                valTotal.setText("Total: " + n + " subject(s), " + units + " unit(s)");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "DB Error: " + ex.getMessage(),
                "Status", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setReq(JLabel l, int v) {
        if (v == 1) {
            l.setText("Ready");
            l.setForeground(UIHelper.GREEN);
        } else {
            l.setText("Pending");
            l.setForeground(UIHelper.DANGER);
        }
    }

    private Color statusColor(String s) {
        if (s == null) return UIHelper.TEXT_GRAY;
        switch (s.toLowerCase()) {
            case "approved":     return UIHelper.GREEN;
            case "rejected":     return UIHelper.DANGER;
            case "under review": return new Color(0xF59E0B);
            default:             return UIHelper.TEXT;
        }
    }

    private String str(String s) { return (s == null || s.isEmpty()) ? "—" : s; }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topBar = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        scrollBody = new javax.swing.JScrollPane();
        pnlBody = new javax.swing.JPanel();
        pnlAdmission = new javax.swing.JPanel();
        lblAdmissionHdr = new javax.swing.JLabel();
        lblStatusKey = new javax.swing.JLabel();
        valStatus = new javax.swing.JLabel();
        lblProgramKey = new javax.swing.JLabel();
        valProgram = new javax.swing.JLabel();
        lblSchoolYearKey = new javax.swing.JLabel();
        valSchoolYear = new javax.swing.JLabel();
        lblSubmittedKey = new javax.swing.JLabel();
        valSubmitted = new javax.swing.JLabel();
        lblRemarksKey = new javax.swing.JLabel();
        valRemarks = new javax.swing.JLabel();
        pnlRequirements = new javax.swing.JPanel();
        lblRequirementsHdr = new javax.swing.JLabel();
        lblForm138Key = new javax.swing.JLabel();
        valForm138 = new javax.swing.JLabel();
        lblGoodMoralKey = new javax.swing.JLabel();
        valGoodMoral = new javax.swing.JLabel();
        lblBirthCertKey = new javax.swing.JLabel();
        valBirthCert = new javax.swing.JLabel();
        lblIdPhotoKey = new javax.swing.JLabel();
        valIdPhoto = new javax.swing.JLabel();
        lblMedicalKey = new javax.swing.JLabel();
        valMedical = new javax.swing.JLabel();
        lblTranscriptKey = new javax.swing.JLabel();
        valTranscript = new javax.swing.JLabel();
        lblHonorableKey = new javax.swing.JLabel();
        valHonorable = new javax.swing.JLabel();
        lblReqUpdatedKey = new javax.swing.JLabel();
        valReqUpdated = new javax.swing.JLabel();
        pnlEnrolled = new javax.swing.JPanel();
        lblEnrolledHdr = new javax.swing.JLabel();
        scrollSubjects = new javax.swing.JScrollPane();
        tblSubjects = new javax.swing.JTable();
        valTotal = new javax.swing.JLabel();
        bottomBar = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AURA - My Status");

        topBar.setBackground(new java.awt.Color(200, 16, 46));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("My Status");

        lblUser.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblUser.setForeground(new java.awt.Color(255, 210, 210));
        lblUser.setText("Account:");

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

        // ── Admission Form card ──
        pnlAdmission.setBackground(new java.awt.Color(255, 255, 255));

        lblAdmissionHdr.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblAdmissionHdr.setForeground(new java.awt.Color(200, 16, 46));
        lblAdmissionHdr.setText("Admission Form");

        configureKey(lblStatusKey, "Status:");
        configureKey(lblProgramKey, "Program:");
        configureKey(lblSchoolYearKey, "School Year:");
        configureKey(lblSubmittedKey, "Submitted:");
        configureKey(lblRemarksKey, "Admin Note:");
        configureValue(valStatus);
        configureValue(valProgram);
        configureValue(valSchoolYear);
        configureValue(valSubmitted);
        configureValue(valRemarks);

        javax.swing.GroupLayout pnlAdmissionLayout = new javax.swing.GroupLayout(pnlAdmission);
        pnlAdmission.setLayout(pnlAdmissionLayout);
        pnlAdmissionLayout.setHorizontalGroup(
            pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdmissionLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAdmissionHdr)
                    .addGroup(pnlAdmissionLayout.createSequentialGroup()
                        .addGroup(pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatusKey)
                            .addComponent(lblProgramKey)
                            .addComponent(lblSchoolYearKey)
                            .addComponent(lblSubmittedKey)
                            .addComponent(lblRemarksKey))
                        .addGap(18, 18, 18)
                        .addGroup(pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(valStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                            .addComponent(valProgram, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                            .addComponent(valSchoolYear, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                            .addComponent(valSubmitted, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                            .addComponent(valRemarks, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        pnlAdmissionLayout.setVerticalGroup(
            pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdmissionLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblAdmissionHdr)
                .addGap(14, 14, 14)
                .addGroup(pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatusKey).addComponent(valStatus))
                .addGap(8, 8, 8)
                .addGroup(pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProgramKey).addComponent(valProgram))
                .addGap(8, 8, 8)
                .addGroup(pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSchoolYearKey).addComponent(valSchoolYear))
                .addGap(8, 8, 8)
                .addGroup(pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSubmittedKey).addComponent(valSubmitted))
                .addGap(8, 8, 8)
                .addGroup(pnlAdmissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRemarksKey).addComponent(valRemarks))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        // ── Requirements card ──
        pnlRequirements.setBackground(new java.awt.Color(255, 255, 255));

        lblRequirementsHdr.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblRequirementsHdr.setForeground(new java.awt.Color(200, 16, 46));
        lblRequirementsHdr.setText("Requirements");

        configureKey(lblForm138Key,    "Form 138:");
        configureKey(lblGoodMoralKey,  "Good Moral:");
        configureKey(lblBirthCertKey,  "Birth Certificate:");
        configureKey(lblIdPhotoKey,    "ID Photo (2x2):");
        configureKey(lblMedicalKey,    "Medical Cert:");
        configureKey(lblTranscriptKey, "Transcript:");
        configureKey(lblHonorableKey,  "Honorable Dismissal:");
        configureKey(lblReqUpdatedKey, "Last Updated:");
        configureValue(valForm138);
        configureValue(valGoodMoral);
        configureValue(valBirthCert);
        configureValue(valIdPhoto);
        configureValue(valMedical);
        configureValue(valTranscript);
        configureValue(valHonorable);
        configureValue(valReqUpdated);

        javax.swing.GroupLayout pnlReqLayout = new javax.swing.GroupLayout(pnlRequirements);
        pnlRequirements.setLayout(pnlReqLayout);
        pnlReqLayout.setHorizontalGroup(
            pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReqLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRequirementsHdr)
                    .addGroup(pnlReqLayout.createSequentialGroup()
                        .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblForm138Key)
                            .addComponent(lblGoodMoralKey)
                            .addComponent(lblBirthCertKey)
                            .addComponent(lblIdPhotoKey)
                            .addComponent(lblMedicalKey)
                            .addComponent(lblTranscriptKey)
                            .addComponent(lblHonorableKey)
                            .addComponent(lblReqUpdatedKey))
                        .addGap(18, 18, 18)
                        .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(valForm138, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addComponent(valGoodMoral, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addComponent(valBirthCert, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addComponent(valIdPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addComponent(valMedical, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addComponent(valTranscript, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addComponent(valHonorable, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addComponent(valReqUpdated, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        pnlReqLayout.setVerticalGroup(
            pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReqLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblRequirementsHdr)
                .addGap(14, 14, 14)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblForm138Key).addComponent(valForm138))
                .addGap(8, 8, 8)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGoodMoralKey).addComponent(valGoodMoral))
                .addGap(8, 8, 8)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBirthCertKey).addComponent(valBirthCert))
                .addGap(8, 8, 8)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPhotoKey).addComponent(valIdPhoto))
                .addGap(8, 8, 8)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMedicalKey).addComponent(valMedical))
                .addGap(8, 8, 8)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTranscriptKey).addComponent(valTranscript))
                .addGap(8, 8, 8)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHonorableKey).addComponent(valHonorable))
                .addGap(14, 14, 14)
                .addGroup(pnlReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReqUpdatedKey).addComponent(valReqUpdated))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        // ── Enrolled Subjects card ──
        pnlEnrolled.setBackground(new java.awt.Color(255, 255, 255));

        lblEnrolledHdr.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblEnrolledHdr.setForeground(new java.awt.Color(200, 16, 46));
        lblEnrolledHdr.setText("Enrolled Subjects");

        scrollSubjects.setViewportView(tblSubjects);

        valTotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        valTotal.setForeground(new java.awt.Color(26, 26, 26));
        valTotal.setText("Total: 0 subject(s), 0 unit(s)");

        javax.swing.GroupLayout pnlEnrolledLayout = new javax.swing.GroupLayout(pnlEnrolled);
        pnlEnrolled.setLayout(pnlEnrolledLayout);
        pnlEnrolledLayout.setHorizontalGroup(
            pnlEnrolledLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnrolledLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlEnrolledLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEnrolledHdr)
                    .addComponent(scrollSubjects, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addComponent(valTotal))
                .addGap(18, 18, 18))
        );
        pnlEnrolledLayout.setVerticalGroup(
            pnlEnrolledLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnrolledLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblEnrolledHdr)
                .addGap(14, 14, 14)
                .addComponent(scrollSubjects, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(valTotal)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        // ── Body assembly ──
        javax.swing.GroupLayout pnlBodyLayout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(pnlBodyLayout);
        pnlBodyLayout.setHorizontalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlAdmission, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlRequirements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEnrolled, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        pnlBodyLayout.setVerticalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(pnlAdmission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(pnlRequirements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(pnlEnrolled, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        scrollBody.setViewportView(pnlBody);

        // ── Bottom bar ──
        bottomBar.setBackground(new java.awt.Color(255, 255, 255));

        btnRefresh.setBackground(new java.awt.Color(200, 16, 46));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setText("Refresh");
        btnRefresh.setBorderPainted(false);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(200, 16, 46));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bottomBarLayout = new javax.swing.GroupLayout(bottomBar);
        bottomBar.setLayout(bottomBarLayout);
        bottomBarLayout.setHorizontalGroup(
            bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomBarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(10, 10, 10)
                .addComponent(btnRefresh)
                .addGap(16, 16, 16))
        );
        bottomBarLayout.setVerticalGroup(
            bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomBarLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnRefresh))
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

    private void configureKey(JLabel l, String text) {
        l.setFont(UIHelper.F_LABEL);
        l.setForeground(UIHelper.TEXT);
        l.setText(text);
    }

    private void configureValue(JLabel l) {
        l.setFont(UIHelper.F_BODY);
        l.setForeground(UIHelper.TEXT);
        l.setText("—");
    }

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadStatus();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    public static void main(String[] args) {
        UIHelper.applyNimbus();
        java.awt.EventQueue.invokeLater(() ->
            new StatusFrame(UIHelper.guestUser()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JPanel bottomBar;
    private javax.swing.JLabel lblAdmissionHdr;
    private javax.swing.JLabel lblBirthCertKey;
    private javax.swing.JLabel lblEnrolledHdr;
    private javax.swing.JLabel lblForm138Key;
    private javax.swing.JLabel lblGoodMoralKey;
    private javax.swing.JLabel lblHonorableKey;
    private javax.swing.JLabel lblIdPhotoKey;
    private javax.swing.JLabel lblMedicalKey;
    private javax.swing.JLabel lblProgramKey;
    private javax.swing.JLabel lblRemarksKey;
    private javax.swing.JLabel lblReqUpdatedKey;
    private javax.swing.JLabel lblRequirementsHdr;
    private javax.swing.JLabel lblSchoolYearKey;
    private javax.swing.JLabel lblStatusKey;
    private javax.swing.JLabel lblSubmittedKey;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTranscriptKey;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlAdmission;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlEnrolled;
    private javax.swing.JPanel pnlRequirements;
    private javax.swing.JScrollPane scrollBody;
    private javax.swing.JScrollPane scrollSubjects;
    private javax.swing.JTable tblSubjects;
    private javax.swing.JPanel topBar;
    private javax.swing.JLabel valBirthCert;
    private javax.swing.JLabel valForm138;
    private javax.swing.JLabel valGoodMoral;
    private javax.swing.JLabel valHonorable;
    private javax.swing.JLabel valIdPhoto;
    private javax.swing.JLabel valMedical;
    private javax.swing.JLabel valProgram;
    private javax.swing.JLabel valRemarks;
    private javax.swing.JLabel valReqUpdated;
    private javax.swing.JLabel valSchoolYear;
    private javax.swing.JLabel valStatus;
    private javax.swing.JLabel valSubmitted;
    private javax.swing.JLabel valTotal;
    private javax.swing.JLabel valTranscript;
    // End of variables declaration//GEN-END:variables
}
