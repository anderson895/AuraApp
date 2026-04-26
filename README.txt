╔═══════════════════════════════════════════════════════════════╗
║     AURA – Automated Enrollment & Admission Portal           ║
║     Taguig City University                                    ║
╚═══════════════════════════════════════════════════════════════╝

HOW TO SET UP
═════════════

STEP 1 – Set up the Database
─────────────────────────────
1. Open XAMPP Control Panel → Start "Apache" and "MySQL"
2. Open your browser → go to: http://localhost/phpmyadmin
3. Click the "SQL" tab at the top
4. Open the file: database/aura_db.sql
5. Copy all the SQL text and paste it into phpMyAdmin
6. Click "Go" to run the script
7. You should see a new database called "aura_db"

   Default admin login:
     Username : admin
     Password : admin123


STEP 2 – Add MySQL Connector to NetBeans
─────────────────────────────────────────
You already have mysql-connector-j-9.3.0.jar on your machine.
NetBeans just needs to know about it:

1. Open the Aura project in NetBeans
2. Right-click "Libraries" in the Projects panel → Add JAR/Folder
3. Browse to:
   D:\Installer\mysql-connector-j-9.3.0\mysql-connector-j-9.3.0.jar
4. Click OK

   (Your project.properties already has this path configured,
    so it may already work without this step.)


STEP 3 – Run the Project
─────────────────────────
1. In NetBeans, right-click the Aura project → "Clean and Build"
2. Then press F6 (or click Run)
3. The AURA login window should appear


STEP 4 – Using the App
───────────────────────
Flow:  Register → Login → Dashboard → Admission Form / Requirements

1. REGISTER – Click "Register here" on the login screen
2. LOGIN    – Enter your username and password
3. DASHBOARD – Choose between:
     Admission Form  – Fill out personal & academic details
     Requirements    – Check off your submitted documents
4. You can Save drafts or Submit when ready


PROJECT STRUCTURE
═════════════════
Aura/
├── src/aura/
│   ├── Aura.java               ← Main entry point
│   ├── DatabaseConnection.java ← MySQL connection
│   ├── User.java               ← Session model
│   ├── UIHelper.java           ← Shared UI (colors, fonts, buttons)
│   ├── LoginFrame.java         ← Login screen
│   ├── RegisterFrame.java      ← Registration screen
│   ├── DashboardFrame.java     ← Main menu (after login)
│   ├── AdmissionFormFrame.java ← Admission form
│   └── RequirementsFrame.java  ← Requirements checklist
├── src/
│   ├── banner.jpg              ← TCU campus banner (classpath)
│   └── logo.jpg                ← TCU seal/logo (classpath)
├── database/
│   └── aura_db.sql             ← Run this in phpMyAdmin
└── nbproject/                  ← NetBeans project config


TROUBLESHOOTING
═══════════════
• "DB Error: Communications link failure"
  → XAMPP MySQL is not running. Start it from XAMPP Control Panel.

• "DB Error: Unknown database 'aura_db'"
  → You haven't run the SQL script yet. Follow STEP 1 above.

• "MySQL driver not found"
  → Add the mysql-connector-j.jar to project libraries. See STEP 2.

• Images not showing (logo/banner appear blank)
  → Make sure banner.jpg and logo.jpg are inside the src/ folder
    (not src/aura/). They should be at the root of the source path.
