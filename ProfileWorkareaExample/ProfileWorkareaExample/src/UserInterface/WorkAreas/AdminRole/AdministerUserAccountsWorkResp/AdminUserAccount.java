/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface.WorkAreas.AdminRole.AdministerUserAccountsWorkResp;

import Business.Business;
import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;
import info5100.university.example.Department.Department;
import info5100.university.example.Persona.Faculty.FacultyDirectory;
import info5100.university.example.Persona.Faculty.FacultyProfile;
import info5100.university.example.Persona.Person;
import info5100.university.example.Persona.PersonDirectory;
import info5100.university.example.Persona.StudentDirectory;
import info5100.university.example.Persona.StudentProfile;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
import java.awt.Component; // For refreshing previous panel
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;

/**
 *h
 * @author kal bugrara
 */

public class AdminUserAccount extends javax.swing.JPanel {

    /**
     * Creates new form ManageSuppliersJPanel
     */
    JPanel CardSequencePanel;
    Business business;
    UserAccount accountToEdit; // 存储正在编辑的账户，如果添加新账户则为 null
    boolean isEditMode;

    public AdminUserAccount(Business bz, JPanel jp, UserAccount ua) {

        CardSequencePanel = jp;
        this.business = bz;
        this.accountToEdit = ua;
        this.isEditMode = (ua != null);
        
        initComponents();
        populateFields(); // Populate fields based on mode

    }
    
    private void populateFields() {
        // Possible roles (reference)
        String[] possibleRoles = {"Admin", "Student", "Faculty", "Register"};

        if (isEditMode && accountToEdit != null) {
            // --- Edit Mode ---
            if(lblPerson != null) lblPerson.setText("Editing Person:"); // **** CHANGE LABEL TEXT ****
            if(cmbPerson != null) cmbPerson.setVisible(false);
            if(txtPersonId != null) {
                txtPersonId.setVisible(true);
                txtPersonId.setText(accountToEdit.getAssociatedPerson() != null ?
                                    accountToEdit.getAssociatedPerson().toString() : "N/A");
            }

            if(txtUsername != null) txtUsername.setText(accountToEdit.getUserLoginName());
            // txtUsername.setEditable(false); // Optional: disable username editing

            if(txtPassword != null) txtPassword.setText("");
            if(txtConfirmPassword != null) txtConfirmPassword.setText("");
            if(lblPassword != null) lblPassword.setText("New Password (blank = no change):");
            if(lblConfirmPassword != null) lblConfirmPassword.setVisible(false);
            if(txtConfirmPassword != null) txtConfirmPassword.setVisible(false);

            if(txtRole != null) txtRole.setText(accountToEdit.getRole());
            if(txtRole != null) txtRole.setEditable(false);

        } else {
            // --- Add Mode ---
            if(lblPerson != null) lblPerson.setText("Select Person:"); // **** CHANGE LABEL TEXT ****
            if(cmbPerson != null) cmbPerson.setVisible(true);
            if(txtPersonId != null) txtPersonId.setVisible(false);

            populatePersonComboBox(); // Fill the person dropdown

            if(txtUsername != null) txtUsername.setText("");
            if(txtUsername != null) txtUsername.setEditable(true);

            if(txtPassword != null) txtPassword.setText("");
            if(txtConfirmPassword != null) txtConfirmPassword.setText("");
            if(lblPassword != null) lblPassword.setText("Password:");
            if(lblConfirmPassword != null) lblConfirmPassword.setVisible(true);
            if(txtConfirmPassword != null) txtConfirmPassword.setVisible(true);

            // Automatically determine role based on initial ComboBox selection
            if(cmbPerson != null) determineAndSetRole((Person) cmbPerson.getSelectedItem());
            if(txtRole != null) txtRole.setEditable(false); // Role determined automatically
        }
    }
    
    private void populatePersonComboBox() {
       // ... (populatePersonComboBox logic remains the same) ...
       if (cmbPerson == null) return;
        DefaultComboBoxModel<Person> model = new DefaultComboBoxModel<>();
        cmbPerson.setModel(model);
        model.removeAllElements();
        PersonDirectory pd = business.getDepartment().getPersonDirectory();
        UserAccountDirectory uad = business.getUserAccountDirectory();
        ArrayList<Person> personsWithoutAccounts = new ArrayList<>();
        if (pd != null && pd.getPersonList() != null && uad != null && uad.getUserAccountList() != null) {
            for (Person p : pd.getPersonList()) {
                 if (p == null) continue;
                boolean hasAccount = false;
                 for (UserAccount ua : uad.getUserAccountList()){
                     if (ua != null && ua.getAssociatedPerson() != null && p.getPersonId() != null &&
                         p.getPersonId().equals(ua.getAssociatedPerson().getPersonId())){
                         hasAccount = true;
                         break;
                     }
                 }
                if (!hasAccount) personsWithoutAccounts.add(p);
            }
        } else { System.err.println("Error populating Person ComboBox: Directory is null."); }
        if (personsWithoutAccounts.isEmpty()) {
             cmbPerson.setEnabled(false);
             if(txtRole != null) txtRole.setText("N/A");
             System.out.println("Info: No persons available without user accounts.");
        } else {
            for (Person p : personsWithoutAccounts) model.addElement(p);
            cmbPerson.setEnabled(true);
            if (cmbPerson.getItemCount() > 0) {
                 cmbPerson.setSelectedIndex(0);
                 determineAndSetRole((Person) cmbPerson.getSelectedItem());
            } else { if(txtRole != null) txtRole.setText("N/A"); }
        }
    }
    
    // Helper method to determine role based on Person's profile
    private void determineAndSetRole(Person person) {
       // ... (determineAndSetRole logic remains the same) ...
        String determinedRole = "N/A";
        if (person != null) {
            Department department = business.getDepartment();
            StudentDirectory sd = (department != null) ? department.getStudentDirectory() : null;
            FacultyDirectory fd = (department != null) ? department.getFacultyDirectory() : null;
            if (sd != null && sd.findStudent(person.getPersonId()) != null) determinedRole = "Student";
            else if (fd != null && fd.findFacultyByPersonId(person.getPersonId()) != null) determinedRole = "Faculty";
        }
        if(txtRole != null) txtRole.setText(determinedRole);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        Back1 = new javax.swing.JButton();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblConfirmPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        txtConfirmPassword = new javax.swing.JTextField();
        cmbPerson = new javax.swing.JComboBox<>();
        lblPerson = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        txtRole = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtPersonId = new javax.swing.JTextField();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("Administer User Account");
        add(lblTitle);
        lblTitle.setBounds(21, 20, 550, 28);

        Back1.setText("<< Back");
        Back1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Back1ActionPerformed(evt);
            }
        });
        add(Back1);
        Back1.setBounds(40, 290, 100, 23);

        lblUsername.setText("Username");
        add(lblUsername);
        lblUsername.setBounds(20, 60, 100, 17);

        lblPassword.setText("Password");
        add(lblPassword);
        lblPassword.setBounds(20, 90, 100, 17);

        lblConfirmPassword.setText("ConfirmPassword");
        add(lblConfirmPassword);
        lblConfirmPassword.setBounds(20, 120, 110, 17);
        add(txtUsername);
        txtUsername.setBounds(140, 60, 140, 23);
        add(txtPassword);
        txtPassword.setBounds(140, 90, 140, 23);
        add(txtConfirmPassword);
        txtConfirmPassword.setBounds(140, 120, 140, 23);

        add(cmbPerson);
        cmbPerson.setBounds(430, 90, 150, 23);

        lblPerson.setText("PersonToAssign");
        add(lblPerson);
        lblPerson.setBounds(320, 90, 93, 17);

        lblRole.setText("Role");
        add(lblRole);
        lblRole.setBounds(370, 120, 42, 17);
        add(txtRole);
        txtRole.setBounds(430, 120, 150, 23);

        btnSave.setText("Save");
        add(btnSave);
        btnSave.setBounds(270, 210, 72, 23);

        jLabel1.setText("PersonID");
        add(jLabel1);
        jLabel1.setBounds(350, 60, 60, 17);
        add(txtPersonId);
        txtPersonId.setBounds(430, 60, 150, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void Back1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Back1ActionPerformed

        // Navigate back to the ManagePersonsJPanel
        CardSequencePanel.remove(this);
        ((CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);


    }//GEN-LAST:event_Back1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back1;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<Person> cmbPerson;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblConfirmPassword;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPerson;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextField txtConfirmPassword;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtPersonId;
    private javax.swing.JTextField txtRole;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

}
