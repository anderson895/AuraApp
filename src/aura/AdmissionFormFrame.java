package aura;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA – Admission Application Form.
 * All visual structure lives in AdmissionFormFrame.form (drag-and-drop in the
 * NetBeans GUI Builder). customInit() only adds runtime polish that the form
 * editor cannot easily express (per-user defaults, button hover, section
 * panel borders, etc.).
 */
public class AdmissionFormFrame extends javax.swing.JFrame {

    private final User user;

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
        setSize(820, 700);
        setLocationRelativeTo(null);

        lblUser.setText("Applicant: " + user.getFullName());

        // Runtime-only field defaults
        tfEmail.setText(user.getEmail());
        tfSchoolYear.setText(LocalDate.now().getYear() + "-" + (LocalDate.now().getYear() + 1));

        // Birthdate dropdowns: populate years, then days (recomputed on month/year change)
        populateBirthYears();
        populateBirthDays();
        java.awt.event.ActionListener dayUpdater = e -> populateBirthDays();
        cbBirthMonth.addActionListener(dayUpdater);
        cbBirthYear.addActionListener(dayUpdater);

        // Section card borders (kept out of the .form so the designer shows
        // clean panels without the extra wrapper styling)
        Border card = new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(14, 18, 18, 18));
        pnlPersonal.setBorder(card);
        pnlContact.setBorder(card);
        pnlAcademic.setBorder(card);
        pnlGuardian.setBorder(card);

        bottomBar.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));

        scrollBody.getVerticalScrollBar().setUnitIncrement(14);

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

    // ─── Save / load ──────────────────────────────────────────

    private void save(boolean submit) {
        if (tfLastName.getText().trim().isEmpty() || tfFirstName.getText().trim().isEmpty()) {
            setMsg("Last name and first name are required.", false); return;
        }
        if (composeBirthdate().isEmpty()) {
            setMsg("Please select a complete date of birth.", false); return;
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
                    "Application submitted successfully!\n" +
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
        ps.setString(4, composeBirthdate());
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
                setBirthdate(rs.getString("birthdate"));
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
                cbCivilStatus.setSelectedItem(rs.getString("civil_status"));
                cbProgram.setSelectedItem(rs.getString("program"));
                setMsg("Existing form loaded. Status: " + rs.getString("status"), true);
            }
        } catch (SQLException ex) {
            // No saved form yet
        }
    }

    private void setMsg(String text, boolean ok) {
        lblStatus.setText(text);
        lblStatus.setForeground(ok ? UIHelper.GREEN : UIHelper.DANGER);
    }

    private String coalesce(String s) { return s != null ? s : ""; }

    // ─── Birthdate dropdown helpers ─────────────────────────────

    private void populateBirthYears() {
        int currentYear = LocalDate.now().getYear();
        javax.swing.DefaultComboBoxModel<String> model = new javax.swing.DefaultComboBoxModel<>();
        model.addElement("-- Year --");
        for (int y = currentYear - 10; y >= currentYear - 80; y--) {
            model.addElement(String.valueOf(y));
        }
        cbBirthYear.setModel(model);
    }

    private void populateBirthDays() {
        int month = cbBirthMonth.getSelectedIndex(); // 1–12, 0 if not selected
        int year = 0;
        Object yearItem = cbBirthYear.getSelectedItem();
        if (yearItem != null) {
            try { year = Integer.parseInt(yearItem.toString()); } catch (NumberFormatException ignored) {}
        }

        int maxDays;
        switch (month) {
            case 4: case 6: case 9: case 11: maxDays = 30; break;
            case 2:
                boolean leap = year > 0 && ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
                maxDays = leap ? 29 : 28;
                break;
            default: maxDays = 31;
        }

        int prevDay = cbBirthDay.getSelectedIndex();
        javax.swing.DefaultComboBoxModel<String> model = new javax.swing.DefaultComboBoxModel<>();
        model.addElement("-- Day --");
        for (int d = 1; d <= maxDays; d++) {
            model.addElement(String.valueOf(d));
        }
        cbBirthDay.setModel(model);
        if (prevDay > 0 && prevDay <= maxDays) {
            cbBirthDay.setSelectedIndex(prevDay);
        }
    }

    private String composeBirthdate() {
        int monthIdx = cbBirthMonth.getSelectedIndex();
        int dayIdx = cbBirthDay.getSelectedIndex();
        int yearIdx = cbBirthYear.getSelectedIndex();
        if (monthIdx <= 0 || dayIdx <= 0 || yearIdx <= 0) return "";
        return String.format("%s-%02d-%02d", cbBirthYear.getSelectedItem(), monthIdx, dayIdx);
    }

    private void setBirthdate(String iso) {
        if (iso == null || iso.length() < 10) {
            cbBirthMonth.setSelectedIndex(0);
            cbBirthYear.setSelectedIndex(0);
            populateBirthDays();
            return;
        }
        try {
            int year  = Integer.parseInt(iso.substring(0, 4));
            int month = Integer.parseInt(iso.substring(5, 7));
            int day   = Integer.parseInt(iso.substring(8, 10));
            String yStr = String.valueOf(year);
            for (int i = 0; i < cbBirthYear.getItemCount(); i++) {
                if (yStr.equals(cbBirthYear.getItemAt(i))) {
                    cbBirthYear.setSelectedIndex(i);
                    break;
                }
            }
            cbBirthMonth.setSelectedIndex(month);
            populateBirthDays();
            if (day >= 1 && day < cbBirthDay.getItemCount()) {
                cbBirthDay.setSelectedIndex(day);
            }
        } catch (NumberFormatException ignored) {}
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topBar = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        scrollBody = new javax.swing.JScrollPane();
        pnlBody = new javax.swing.JPanel();
        pnlPersonal = new javax.swing.JPanel();
        lblPersonalHdr = new javax.swing.JLabel();
        lblLastName = new javax.swing.JLabel();
        tfLastName = new javax.swing.JTextField();
        lblFirstName = new javax.swing.JLabel();
        tfFirstName = new javax.swing.JTextField();
        lblMiddleName = new javax.swing.JLabel();
        tfMiddleName = new javax.swing.JTextField();
        lblGender = new javax.swing.JLabel();
        cbGender = new javax.swing.JComboBox<>();
        lblBirthdate = new javax.swing.JLabel();
        pnlBirthdate = new javax.swing.JPanel();
        cbBirthMonth = new javax.swing.JComboBox<>();
        cbBirthDay = new javax.swing.JComboBox<>();
        cbBirthYear = new javax.swing.JComboBox<>();
        lblBirthplace = new javax.swing.JLabel();
        tfBirthplace = new javax.swing.JTextField();
        lblNationality = new javax.swing.JLabel();
        tfNationality = new javax.swing.JTextField();
        lblReligion = new javax.swing.JLabel();
        tfReligion = new javax.swing.JTextField();
        lblCivilStatus = new javax.swing.JLabel();
        cbCivilStatus = new javax.swing.JComboBox<>();
        pnlContact = new javax.swing.JPanel();
        lblContactHdr = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        tfAddress = new javax.swing.JTextField();
        lblContact = new javax.swing.JLabel();
        tfContact = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        tfEmail = new javax.swing.JTextField();
        pnlAcademic = new javax.swing.JPanel();
        lblAcademicHdr = new javax.swing.JLabel();
        lblProgram = new javax.swing.JLabel();
        cbProgram = new javax.swing.JComboBox<>();
        lblPrevSchool = new javax.swing.JLabel();
        tfPrevSchool = new javax.swing.JTextField();
        lblPrevStrand = new javax.swing.JLabel();
        tfPrevStrand = new javax.swing.JTextField();
        lblSchoolYear = new javax.swing.JLabel();
        tfSchoolYear = new javax.swing.JTextField();
        pnlGuardian = new javax.swing.JPanel();
        lblGuardianHdr = new javax.swing.JLabel();
        lblGuardian = new javax.swing.JLabel();
        tfGuardian = new javax.swing.JTextField();
        lblGuardianContact = new javax.swing.JLabel();
        tfGuardianContact = new javax.swing.JTextField();
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

        pnlPersonal.setBackground(new java.awt.Color(255, 255, 255));

        lblPersonalHdr.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblPersonalHdr.setForeground(new java.awt.Color(200, 16, 46));
        lblPersonalHdr.setText("Personal Information");

        lblLastName.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblLastName.setText("Last Name *");

        lblFirstName.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblFirstName.setText("First Name *");

        lblMiddleName.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblMiddleName.setText("Middle Name");

        lblGender.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblGender.setText("Gender *");

        cbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female", "Other" }));

        lblBirthdate.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblBirthdate.setText("Date of Birth *");

        pnlBirthdate.setOpaque(false);

        cbBirthMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
            "-- Month --", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        }));
        cbBirthDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Day --" }));
        cbBirthYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Year --" }));

        javax.swing.GroupLayout pnlBirthdateLayout = new javax.swing.GroupLayout(pnlBirthdate);
        pnlBirthdate.setLayout(pnlBirthdateLayout);
        pnlBirthdateLayout.setHorizontalGroup(
            pnlBirthdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBirthdateLayout.createSequentialGroup()
                .addComponent(cbBirthMonth, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(cbBirthDay, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(cbBirthYear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlBirthdateLayout.setVerticalGroup(
            pnlBirthdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBirthdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbBirthMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbBirthDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbBirthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblBirthplace.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblBirthplace.setText("Place of Birth");

        lblNationality.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblNationality.setText("Nationality");

        tfNationality.setText("Filipino");

        lblReligion.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblReligion.setText("Religion");

        lblCivilStatus.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCivilStatus.setText("Civil Status");

        cbCivilStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Single", "Married", "Widowed", "Separated" }));

        javax.swing.GroupLayout pnlPersonalLayout = new javax.swing.GroupLayout(pnlPersonal);
        pnlPersonal.setLayout(pnlPersonalLayout);
        pnlPersonalLayout.setHorizontalGroup(
            pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPersonalLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPersonalHdr)
                    .addGroup(pnlPersonalLayout.createSequentialGroup()
                        .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLastName)
                            .addComponent(tfLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(lblMiddleName)
                            .addComponent(tfMiddleName, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(lblBirthdate)
                            .addComponent(pnlBirthdate, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(lblNationality)
                            .addComponent(tfNationality, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(lblCivilStatus)
                            .addComponent(cbCivilStatus, 0, 280, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFirstName)
                            .addComponent(tfFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(lblGender)
                            .addComponent(cbGender, 0, 280, Short.MAX_VALUE)
                            .addComponent(lblBirthplace)
                            .addComponent(tfBirthplace, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(lblReligion)
                            .addComponent(tfReligion, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        pnlPersonalLayout.setVerticalGroup(
            pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPersonalLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblPersonalHdr)
                .addGap(14, 14, 14)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLastName)
                    .addComponent(lblFirstName))
                .addGap(3, 3, 3)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMiddleName)
                    .addComponent(lblGender))
                .addGap(3, 3, 3)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfMiddleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBirthdate)
                    .addComponent(lblBirthplace))
                .addGap(3, 3, 3)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pnlBirthdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfBirthplace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNationality)
                    .addComponent(lblReligion))
                .addGap(3, 3, 3)
                .addGroup(pnlPersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNationality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfReligion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(lblCivilStatus)
                .addGap(3, 3, 3)
                .addComponent(cbCivilStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnlContact.setBackground(new java.awt.Color(255, 255, 255));

        lblContactHdr.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblContactHdr.setForeground(new java.awt.Color(200, 16, 46));
        lblContactHdr.setText("Contact Information");

        lblAddress.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblAddress.setText("Home Address *");

        lblContact.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblContact.setText("Contact Number *");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEmail.setText("Email Address *");

        javax.swing.GroupLayout pnlContactLayout = new javax.swing.GroupLayout(pnlContact);
        pnlContact.setLayout(pnlContactLayout);
        pnlContactLayout.setHorizontalGroup(
            pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContactLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblContactHdr)
                    .addComponent(lblAddress)
                    .addComponent(tfAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGroup(pnlContactLayout.createSequentialGroup()
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblContact)
                            .addComponent(tfContact, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEmail)
                            .addComponent(tfEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        pnlContactLayout.setVerticalGroup(
            pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContactLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblContactHdr)
                .addGap(14, 14, 14)
                .addComponent(lblAddress)
                .addGap(3, 3, 3)
                .addComponent(tfAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContact)
                    .addComponent(lblEmail))
                .addGap(3, 3, 3)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnlAcademic.setBackground(new java.awt.Color(255, 255, 255));

        lblAcademicHdr.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblAcademicHdr.setForeground(new java.awt.Color(200, 16, 46));
        lblAcademicHdr.setText("Academic Information");

        lblProgram.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblProgram.setText("Program Applying For *");

        cbProgram.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
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
        }));

        lblPrevSchool.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPrevSchool.setText("Previous School");

        lblPrevStrand.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPrevStrand.setText("Previous Strand / Track");

        lblSchoolYear.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblSchoolYear.setText("School Year");

        javax.swing.GroupLayout pnlAcademicLayout = new javax.swing.GroupLayout(pnlAcademic);
        pnlAcademic.setLayout(pnlAcademicLayout);
        pnlAcademicLayout.setHorizontalGroup(
            pnlAcademicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAcademicLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlAcademicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAcademicHdr)
                    .addComponent(lblProgram)
                    .addComponent(cbProgram, 0, 500, Short.MAX_VALUE)
                    .addGroup(pnlAcademicLayout.createSequentialGroup()
                        .addGroup(pnlAcademicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPrevSchool)
                            .addComponent(tfPrevSchool, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(lblSchoolYear)
                            .addComponent(tfSchoolYear, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addGroup(pnlAcademicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPrevStrand)
                            .addComponent(tfPrevStrand, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        pnlAcademicLayout.setVerticalGroup(
            pnlAcademicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAcademicLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblAcademicHdr)
                .addGap(14, 14, 14)
                .addComponent(lblProgram)
                .addGap(3, 3, 3)
                .addComponent(cbProgram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnlAcademicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrevSchool)
                    .addComponent(lblPrevStrand))
                .addGap(3, 3, 3)
                .addGroup(pnlAcademicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPrevSchool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPrevStrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(lblSchoolYear)
                .addGap(3, 3, 3)
                .addComponent(tfSchoolYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnlGuardian.setBackground(new java.awt.Color(255, 255, 255));

        lblGuardianHdr.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblGuardianHdr.setForeground(new java.awt.Color(200, 16, 46));
        lblGuardianHdr.setText("Guardian / Parent Information");

        lblGuardian.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblGuardian.setText("Guardian / Parent Name");

        lblGuardianContact.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblGuardianContact.setText("Guardian Contact No.");

        javax.swing.GroupLayout pnlGuardianLayout = new javax.swing.GroupLayout(pnlGuardian);
        pnlGuardian.setLayout(pnlGuardianLayout);
        pnlGuardianLayout.setHorizontalGroup(
            pnlGuardianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGuardianLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlGuardianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGuardianHdr)
                    .addGroup(pnlGuardianLayout.createSequentialGroup()
                        .addGroup(pnlGuardianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGuardian)
                            .addComponent(tfGuardian, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addGroup(pnlGuardianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGuardianContact)
                            .addComponent(tfGuardianContact, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        pnlGuardianLayout.setVerticalGroup(
            pnlGuardianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGuardianLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblGuardianHdr)
                .addGap(14, 14, 14)
                .addGroup(pnlGuardianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGuardian)
                    .addComponent(lblGuardianContact))
                .addGap(3, 3, 3)
                .addGroup(pnlGuardianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfGuardian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfGuardianContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlBodyLayout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(pnlBodyLayout);
        pnlBodyLayout.setHorizontalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlPersonal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlContact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlAcademic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlGuardian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        pnlBodyLayout.setVerticalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(pnlPersonal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(pnlContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(pnlAcademic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(pnlGuardian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JComboBox<String> cbBirthDay;
    private javax.swing.JComboBox<String> cbBirthMonth;
    private javax.swing.JComboBox<String> cbBirthYear;
    private javax.swing.JComboBox<String> cbCivilStatus;
    private javax.swing.JComboBox<String> cbGender;
    private javax.swing.JComboBox<String> cbProgram;
    private javax.swing.JLabel lblAcademicHdr;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblBirthdate;
    private javax.swing.JLabel lblBirthplace;
    private javax.swing.JLabel lblCivilStatus;
    private javax.swing.JLabel lblContact;
    private javax.swing.JLabel lblContactHdr;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblGuardian;
    private javax.swing.JLabel lblGuardianContact;
    private javax.swing.JLabel lblGuardianHdr;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblMiddleName;
    private javax.swing.JLabel lblNationality;
    private javax.swing.JLabel lblPersonalHdr;
    private javax.swing.JLabel lblPrevSchool;
    private javax.swing.JLabel lblPrevStrand;
    private javax.swing.JLabel lblProgram;
    private javax.swing.JLabel lblReligion;
    private javax.swing.JLabel lblSchoolYear;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlAcademic;
    private javax.swing.JPanel pnlBirthdate;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlContact;
    private javax.swing.JPanel pnlGuardian;
    private javax.swing.JPanel pnlPersonal;
    private javax.swing.JScrollPane scrollBody;
    private javax.swing.JTextField tfAddress;
    private javax.swing.JTextField tfBirthplace;
    private javax.swing.JTextField tfContact;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfFirstName;
    private javax.swing.JTextField tfGuardian;
    private javax.swing.JTextField tfGuardianContact;
    private javax.swing.JTextField tfLastName;
    private javax.swing.JTextField tfMiddleName;
    private javax.swing.JTextField tfNationality;
    private javax.swing.JTextField tfPrevSchool;
    private javax.swing.JTextField tfPrevStrand;
    private javax.swing.JTextField tfReligion;
    private javax.swing.JTextField tfSchoolYear;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables
}
