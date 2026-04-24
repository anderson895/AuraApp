package aura;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA – Admission Application Form
 */
public class AdmissionFormFrame extends javax.swing.JFrame {

    private final User user;

    // Personal
    private JTextField tfLastName, tfFirstName, tfMiddleName;
    private JTextField tfBirthdate, tfBirthplace, tfNationality, tfReligion;
    private JComboBox<String> cbGender, cbCivilStatus;

    // Contact
    private JTextField tfAddress, tfContact, tfEmail;

    // Academic
    private JComboBox<String> cbProgram;
    private JTextField tfPrevSchool, tfPrevStrand, tfSchoolYear;

    // Guardian
    private JTextField tfGuardian, tfGuardianContact;

    public AdmissionFormFrame() {
        this(UIHelper.guestUser());
    }

    public AdmissionFormFrame(User user) {
        this.user = user;
        initComponents();
        customInit();
        loadExisting();
        UIHelper.flattenButtons(getContentPane());
    }

    private void customInit() {
        setSize(800, 680);
        setLocationRelativeTo(null);

        lblUser.setText("Applicant: " + user.getFullName());

        // Populate the scrollable body with section cards
        pnlBody.setBorder(new EmptyBorder(16, 20, 16, 20));
        pnlBody.add(buildPersonalSection());
        pnlBody.add(Box.createVerticalStrut(14));
        pnlBody.add(buildContactSection());
        pnlBody.add(Box.createVerticalStrut(14));
        pnlBody.add(buildAcademicSection());
        pnlBody.add(Box.createVerticalStrut(14));
        pnlBody.add(buildGuardianSection());

        scrollBody.getVerticalScrollBar().setUnitIncrement(14);

        bottomBar.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));

        applyPrimary(btnSave);
        applyPrimary(btnSubmit);
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

    // ─── Section builders ─────────────────────────────────────

    private JPanel buildPersonalSection() {
        JPanel p = sectionPanel("Personal Information");

        tfLastName   = UIHelper.field(16);
        tfFirstName  = UIHelper.field(16);
        tfMiddleName = UIHelper.field(16);
        tfBirthdate  = UIHelper.field(12);
        tfBirthdate.setToolTipText("Format: YYYY-MM-DD");
        tfBirthplace = UIHelper.field(20);
        tfNationality = UIHelper.field(14);
        tfNationality.setText("Filipino");
        tfReligion   = UIHelper.field(14);

        cbGender      = UIHelper.combo(new String[]{"Male", "Female", "Other"});
        cbCivilStatus = UIHelper.combo(new String[]{"Single", "Married", "Widowed", "Separated"});

        JPanel grid = grid2();
        grid.add(row("Last Name *",    tfLastName));
        grid.add(row("First Name *",   tfFirstName));
        grid.add(row("Middle Name",    tfMiddleName));
        grid.add(row("Gender *",       cbGender));
        grid.add(row("Date of Birth * (YYYY-MM-DD)", tfBirthdate));
        grid.add(row("Place of Birth", tfBirthplace));
        grid.add(row("Nationality",    tfNationality));
        grid.add(row("Religion",       tfReligion));
        grid.add(row("Civil Status",   cbCivilStatus));

        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildContactSection() {
        JPanel p = sectionPanel("Contact Information");

        tfAddress = UIHelper.field(30);
        tfContact = UIHelper.field(16);
        tfEmail   = UIHelper.field(22);
        tfEmail.setText(user.getEmail());

        JPanel grid = grid2();
        grid.add(row("Home Address *",  tfAddress));
        grid.add(row("Contact Number *", tfContact));
        grid.add(row("Email Address *",  tfEmail));

        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildAcademicSection() {
        JPanel p = sectionPanel("Academic Information");

        cbProgram = UIHelper.combo(new String[]{
            "-- Select Program --",
            "Bachelor of Science in Information Technology",
            "Bachelor of Science in Computer Science",
            "Bachelor of Science in Business Administration",
            "Bachelor of Science in Accountancy",
            "Bachelor of Science in Criminology",
            "Bachelor of Science in Nursing",
            "Bachelor of Science in Education",
            "Bachelor of Science in Engineering",
            "Bachelor of Arts in Communication",
            "Other"
        });
        tfPrevSchool  = UIHelper.field(24);
        tfPrevStrand  = UIHelper.field(16);
        tfSchoolYear  = UIHelper.field(10);
        tfSchoolYear.setText(LocalDate.now().getYear() + "-" + (LocalDate.now().getYear() + 1));

        JPanel grid = grid2();
        grid.add(row("Program Applying For *", cbProgram));
        grid.add(row("Previous School",        tfPrevSchool));
        grid.add(row("Previous Strand / Track", tfPrevStrand));
        grid.add(row("School Year",             tfSchoolYear));

        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildGuardianSection() {
        JPanel p = sectionPanel("Guardian / Parent Information");

        tfGuardian        = UIHelper.field(24);
        tfGuardianContact = UIHelper.field(16);

        JPanel grid = grid2();
        grid.add(row("Guardian / Parent Name", tfGuardian));
        grid.add(row("Guardian Contact No.",   tfGuardianContact));

        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    // ─── Save / load ──────────────────────────────────────────

    private void save(boolean submit) {
        if (tfLastName.getText().trim().isEmpty() || tfFirstName.getText().trim().isEmpty()) {
            setMsg("Last name and first name are required.", false); return;
        }
        if (tfBirthdate.getText().trim().isEmpty()) {
            setMsg("Date of birth is required (YYYY-MM-DD).", false); return;
        }
        if (tfAddress.getText().trim().isEmpty() || tfContact.getText().trim().isEmpty()) {
            setMsg("Address and contact number are required.", false); return;
        }
        if (cbProgram.getSelectedIndex() == 0) {
            setMsg("Please select a program.", false); return;
        }

        String status = submit ? "Under Review" : "Pending";

        try (Connection c = DatabaseConnection.getConnection()) {
            PreparedStatement chk = c.prepareStatement(
                "SELECT id FROM admission_forms WHERE user_id=?");
            chk.setInt(1, user.getId());
            ResultSet rs = chk.executeQuery();

            if (rs.next()) {
                PreparedStatement upd = c.prepareStatement(
                    "UPDATE admission_forms SET last_name=?,first_name=?,middle_name=?," +
                    "birthdate=?,birthplace=?,gender=?,civil_status=?,nationality=?,religion=?," +
                    "address=?,contact_number=?,email=?,program=?,previous_school=?," +
                    "previous_strand=?,school_year=?,guardian_name=?,guardian_contact=?,status=? " +
                    "WHERE user_id=?");
                fillParams(upd, status);
                upd.setInt(20, user.getId());
                upd.executeUpdate();
            } else {
                PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO admission_forms (last_name,first_name,middle_name,birthdate," +
                    "birthplace,gender,civil_status,nationality,religion,address,contact_number," +
                    "email,program,previous_school,previous_strand,school_year,guardian_name," +
                    "guardian_contact,status,user_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                fillParams(ins, status);
                ins.setInt(20, user.getId());
                ins.executeUpdate();
            }

            if (submit) {
                JOptionPane.showMessageDialog(this,
                    "✅ Application submitted successfully!\n" +
                    "Status: Under Review\n" +
                    "Please complete your requirements next.",
                    "Submitted", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                setMsg("Draft saved successfully.", true);
            }

        } catch (SQLException ex) {
            setMsg("DB Error: " + ex.getMessage(), false);
        }
    }

    private void fillParams(PreparedStatement ps, String status) throws SQLException {
        ps.setString(1, tfLastName.getText().trim());
        ps.setString(2, tfFirstName.getText().trim());
        ps.setString(3, tfMiddleName.getText().trim());
        ps.setString(4, tfBirthdate.getText().trim());
        ps.setString(5, tfBirthplace.getText().trim());
        ps.setString(6, (String) cbGender.getSelectedItem());
        ps.setString(7, (String) cbCivilStatus.getSelectedItem());
        ps.setString(8, tfNationality.getText().trim());
        ps.setString(9, tfReligion.getText().trim());
        ps.setString(10, tfAddress.getText().trim());
        ps.setString(11, tfContact.getText().trim());
        ps.setString(12, tfEmail.getText().trim());
        ps.setString(13, (String) cbProgram.getSelectedItem());
        ps.setString(14, tfPrevSchool.getText().trim());
        ps.setString(15, tfPrevStrand.getText().trim());
        ps.setString(16, tfSchoolYear.getText().trim());
        ps.setString(17, tfGuardian.getText().trim());
        ps.setString(18, tfGuardianContact.getText().trim());
        ps.setString(19, status);
    }

    private void loadExisting() {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT * FROM admission_forms WHERE user_id=?")) {
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tfLastName.setText(rs.getString("last_name"));
                tfFirstName.setText(rs.getString("first_name"));
                tfMiddleName.setText(coalesce(rs.getString("middle_name")));
                tfBirthdate.setText(coalesce(rs.getString("birthdate")));
                tfBirthplace.setText(coalesce(rs.getString("birthplace")));
                tfNationality.setText(coalesce(rs.getString("nationality")));
                tfReligion.setText(coalesce(rs.getString("religion")));
                tfAddress.setText(coalesce(rs.getString("address")));
                tfContact.setText(coalesce(rs.getString("contact_number")));
                tfEmail.setText(coalesce(rs.getString("email")));
                tfPrevSchool.setText(coalesce(rs.getString("previous_school")));
                tfPrevStrand.setText(coalesce(rs.getString("previous_strand")));
                tfSchoolYear.setText(coalesce(rs.getString("school_year")));
                tfGuardian.setText(coalesce(rs.getString("guardian_name")));
                tfGuardianContact.setText(coalesce(rs.getString("guardian_contact")));
                cbGender.setSelectedItem(rs.getString("gender"));
                cbProgram.setSelectedItem(rs.getString("program"));
                setMsg("Existing form loaded. Status: " + rs.getString("status"), true);
            }
        } catch (SQLException ex) {
            // No saved form yet
        }
    }

    // ─── Layout helpers ───────────────────────────────────────

    private JPanel sectionPanel(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(UIHelper.WHITE);
        p.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(14, 18, 18, 18)));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.add(UIHelper.sectionLabel(title), BorderLayout.NORTH);
        return p;
    }

    private JPanel grid2() {
        JPanel g = new JPanel(new GridLayout(0, 2, 14, 10));
        g.setOpaque(false);
        return g;
    }

    private JPanel row(String labelText, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UIHelper.F_LABEL);
        lbl.setForeground(UIHelper.TEXT);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void setMsg(String text, boolean ok) {
        lblStatus.setText(text);
        lblStatus.setForeground(ok ? UIHelper.GREEN : UIHelper.DANGER);
    }

    private String coalesce(String s) { return s != null ? s : ""; }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topBar = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        scrollBody = new javax.swing.JScrollPane();
        pnlBody = new javax.swing.JPanel();
        bottomBar = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AURA – Admission Application Form");

        topBar.setBackground(new java.awt.Color(200, 16, 46));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Admission Application Form");

        lblUser.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblUser.setForeground(new java.awt.Color(255, 210, 210));
        lblUser.setText("Applicant: ");

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
        pnlBody.setLayout(new javax.swing.BoxLayout(pnlBody, javax.swing.BoxLayout.Y_AXIS));
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
        btnSave.setText("Save Draft");
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnSubmit.setBackground(new java.awt.Color(200, 16, 46));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("Submit");
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
            new AdmissionFormFrame(UIHelper.guestUser()).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JPanel bottomBar;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JScrollPane scrollBody;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables
}
