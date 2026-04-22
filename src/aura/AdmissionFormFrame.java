package aura;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.*;

/**
 * AURA – Admission Application Form
 */
public class AdmissionFormFrame extends JFrame {

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

    private JLabel lblStatus;

    public AdmissionFormFrame(User user) {
        this.user = user;
        setTitle("AURA – Admission Application Form");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 680);
        setLocationRelativeTo(null);
        buildUI();
        loadExisting();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);
        setContentPane(root);

        // ── Top bar ──────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIHelper.RED);
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lbTitle = new JLabel("Admission Application Form");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitle.setForeground(Color.WHITE);

        JLabel lbUser = new JLabel("Applicant: " + user.getFullName());
        lbUser.setFont(UIHelper.F_SMALL);
        lbUser.setForeground(new Color(255, 210, 210));

        topBar.add(lbTitle, BorderLayout.WEST);
        topBar.add(lbUser, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        // ── Scrollable form body ─────────────────────────────
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIHelper.BG);
        body.setBorder(new EmptyBorder(16, 20, 16, 20));

        body.add(buildPersonalSection());
        body.add(Box.createVerticalStrut(14));
        body.add(buildContactSection());
        body.add(Box.createVerticalStrut(14));
        body.add(buildAcademicSection());
        body.add(Box.createVerticalStrut(14));
        body.add(buildGuardianSection());

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        root.add(scroll, BorderLayout.CENTER);

        // ── Bottom bar ───────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        bottom.setBackground(UIHelper.WHITE);
        bottom.setBorder(new MatteBorder(1, 0, 0, 0, UIHelper.BORDER));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(UIHelper.F_SMALL);
        lblStatus.setForeground(UIHelper.DANGER);

        JButton btnSave   = UIHelper.primaryBtn("Save Draft");
        JButton btnSubmit = UIHelper.primaryBtn("Submit");
        JButton btnBack   = UIHelper.outlineBtn("Back");

        btnBack.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> save(false));
        btnSubmit.addActionListener(e -> save(true));

        bottom.add(lblStatus);
        bottom.add(btnBack);
        bottom.add(btnSave);
        bottom.add(btnSubmit);
        root.add(bottom, BorderLayout.SOUTH);
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
        // Basic validation
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
            // Check if record already exists
            PreparedStatement chk = c.prepareStatement(
                "SELECT id FROM admission_forms WHERE user_id=?");
            chk.setInt(1, user.getId());
            ResultSet rs = chk.executeQuery();

            if (rs.next()) {
                // Update
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
                // Insert
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
            // No saved form yet – that's fine
        }
    }

    // ─── Layout helpers ───────────────────────────────────────

    private JPanel sectionPanel(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(UIHelper.WHITE);
        p.setBorder(new CompoundBorder(
            new LineBorder(UIHelper.BORDER, 1, true),
            new EmptyBorder(14, 18, 18, 18)));
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
}
