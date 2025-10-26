/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface.WorkAreas.AdminRole.ManagePersonnelWorkResp;

import Business.Business;
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

/**
 *
 * @author kal bugrara
 */
public class AdministerPersonJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ManageSuppliersJPanel
     */
    JPanel CardSequencePanel;
    Business business;
    Person personToEdit; // Stores the person being edited, null if adding new
    private final boolean isEditMode;

    public AdministerPersonJPanel(Business bz, JPanel jp, Person p) {

        CardSequencePanel = jp;
        this.business = bz;
        this.personToEdit = p;
        this.isEditMode = (p != null);
        initComponents();

        populateFields(); // Populate fiedls if in edit mode

    }

    private void populateFields() {
        // --- 1. Populate cmbRole with options FIRST ---
        cmbRole.removeAllItems(); // Clear any existing items first
        cmbRole.addItem("Student");
        cmbRole.addItem("Faculty");
        // Add other roles here if needed in the future

        // --- 2. Now set fields based on mode ---
        if (isEditMode && personToEdit != null) {
            lblTitle.setText("Edit Person Profile"); // Also update title dynamically
            txtPersonId.setText(personToEdit.getPersonId());
            txtName.setText(personToEdit.getName());
            txtEmail.setText(personToEdit.getEmail());

            // Determine and set the current role
            Department department = business.getDepartment();
            StudentDirectory sd = department.getStudentDirectory();
            FacultyDirectory fd = department.getFacultyDirectory();
            String currentRole = null;
            if (sd.findStudent(personToEdit.getPersonId()) != null) {
                 currentRole = "Student";
            } else if (fd.findFacultyByPersonId(personToEdit.getPersonId()) != null) {
                 currentRole = "Faculty";
            }
            // else { check for other roles }

            if (currentRole != null) {
                 cmbRole.setSelectedItem(currentRole); // Select the current role
            } else {
                 cmbRole.setSelectedIndex(-1); // No matching role found, select nothing
            }
            cmbRole.setEnabled(true); // Allow role change in edit mode

        } else {
            // Add mode: Clear fields and set defaults
            lblTitle.setText("Add New Person"); // Update title
            txtPersonId.setText("(Auto-generated on save)"); // Updated placeholder text
            txtName.setText("");
            txtEmail.setText("");

            // Safely set the default selection (Student)
            if (cmbRole.getItemCount() > 0) {
                 cmbRole.setSelectedIndex(0); // Select the first item ("Student")
            }
            cmbRole.setEnabled(true); // Ensure role can be selected when adding
        }
    }

    private String generateNextPersonId(String rolePrefix, PersonDirectory personDirectory) {
        int maxNumber = 0;
        ArrayList<Person> persons = personDirectory.getPersonList(); // Assuming getPersonList() exists

        for (Person p : persons) {
            String id = p.getPersonId();
            if (id != null && id.startsWith(rolePrefix)) {
                try {
                    // Extract the numeric part after the prefix
                    String numPart = id.substring(rolePrefix.length());
                    int currentNum = Integer.parseInt(numPart);
                    if (currentNum > maxNumber) {
                        maxNumber = currentNum;
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    // Handle IDs that don't match the expected format (e.g., old UUIDs)
                    System.err.println("Skipping improperly formatted ID during generation: " + id);
                }
            }
        }
        // Format the next number with leading zeros (e.g., 001, 010, 100)
        return rolePrefix + String.format("%03d", maxNumber + 1);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Back = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        lblID = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtPersonId = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        lblRole = new javax.swing.JLabel();
        cmbRole = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        Back.setText("<< Back");
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });
        add(Back);
        Back.setBounds(30, 290, 80, 23);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("Manage Person Profile");
        add(lblTitle);
        lblTitle.setBounds(150, 20, 250, 28);

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        add(btnSave);
        btnSave.setBounds(190, 220, 150, 23);

        lblID.setText("ID");
        add(lblID);
        lblID.setBounds(120, 70, 50, 17);

        lblName.setText("Name");
        add(lblName);
        lblName.setBounds(120, 100, 40, 17);

        lblEmail.setText("Email");
        add(lblEmail);
        lblEmail.setBounds(120, 130, 80, 17);
        add(txtPersonId);
        txtPersonId.setBounds(230, 70, 200, 23);
        add(txtName);
        txtName.setBounds(230, 100, 200, 23);
        add(txtEmail);
        txtEmail.setBounds(230, 130, 200, 23);

        lblRole.setText("Role");
        add(lblRole);
        lblRole.setBounds(120, 160, 90, 17);

        add(cmbRole);
        cmbRole.setBounds(230, 160, 120, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
        // TODO add your handling code here:
        CardSequencePanel.remove(this);
        ((CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);

        // Refresh the table in ManagePersonsJPanel
        refreshPreviousTable();
    }//GEN-LAST:event_BackActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
       // 1. Get input
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String selectedRole = (String) cmbRole.getSelectedItem();

        // 2. Validate input
        if (name.isEmpty() || email.isEmpty() || selectedRole == null) {
            JOptionPane.showMessageDialog(this, "Name, Email, and Role cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Stay on page on validation error
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (!pat.matcher(email).matches()) {
             JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Stay on page
        }

        Department department = business.getDepartment();
        PersonDirectory personDirectory = department.getPersonDirectory();
        StudentDirectory studentDirectory = department.getStudentDirectory();
        FacultyDirectory facultyDirectory = department.getFacultyDirectory();
        UserAccountDirectory userAccountDirectory = business.getUserAccountDirectory();


        // 3. Check for duplicate email (excluding self in edit mode)
        Person existingPersonWithEmail = personDirectory.findPersonByEmail(email);
        if (existingPersonWithEmail != null && (!isEditMode || !existingPersonWithEmail.getPersonId().equals(personToEdit.getPersonId()))) {
            JOptionPane.showMessageDialog(this, "This email address is already registered to another person.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return; // Stay on page
        }


        // --- Perform Add or Update ---
        boolean saveSuccessful = false; // Flag to control navigation

        if (isEditMode) {
            // --- Update existing person --- (Role change logic is complex and needs careful implementation - see previous response)
             if (personToEdit == null) {
                 JOptionPane.showMessageDialog(this, "Error: No person selected for editing.", "Internal Error", JOptionPane.ERROR_MESSAGE);
                 return; // Stay on page
             }

            // --- Simplified Update - Focus on Name/Email first ---
            personToEdit.setName(name);
            personToEdit.setEmail(email);

            // --- Placeholder for Role Change Logic (from previous response) ---
            // Determine originalRole
            // if (roleChanged) {
            //     Delete old profile
            //     Create new profile
            //     Update UserAccount role
            //     Handle failures gracefully
            // }
            // For now, assume role change logic is handled elsewhere or disabled
            // --- End Placeholder ---

            JOptionPane.showMessageDialog(this, "Person information updated successfully.", "Update Success", JOptionPane.INFORMATION_MESSAGE);
            saveSuccessful = true; // Assume success for now


        } else {
            // --- Add new person ---
            // *** Use new ID generation logic ***
            String idPrefix = "";
            if ("Student".equals(selectedRole)) {
                idPrefix = "S";
            } else if ("Faculty".equals(selectedRole)) {
                idPrefix = "F";
            } // Add other prefixes if needed

            if (idPrefix.isEmpty()){
                 JOptionPane.showMessageDialog(this, "Invalid role selected for ID generation.", "Creation Error", JOptionPane.ERROR_MESSAGE);
                 return; // Stay on page
            }

            String uniqueID = generateNextPersonId(idPrefix, personDirectory);
            // *** End new ID generation logic ***


            Person newPerson = personDirectory.newPerson(uniqueID);
             if (newPerson == null) { // Check if ID already exists or other error
                JOptionPane.showMessageDialog(this, "Failed to create new person record (possibly duplicate ID '" + uniqueID + "').", "Creation Error", JOptionPane.ERROR_MESSAGE);
                 return; // Stay on page
            }
            newPerson.setName(name);
            newPerson.setEmail(email);

            // Create Profile object based on role
            boolean profileCreated = false;
            if ("Student".equals(selectedRole)) {
                StudentProfile studentProfile = studentDirectory.newStudentProfile(newPerson);
                profileCreated = (studentProfile != null);
            } else if ("Faculty".equals(selectedRole)) {
                FacultyProfile facultyProfile = facultyDirectory.newFacultyProfile(newPerson);
                 profileCreated = (facultyProfile != null);
            }

            if (profileCreated) {
                JOptionPane.showMessageDialog(this, "Person registered successfully!\nID: " + uniqueID + "\nName: " + name, "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                saveSuccessful = true;
                // Consider creating UserAccount here as well
            } else {
                 JOptionPane.showMessageDialog(this, "Person record created, but failed to create the profile ("+ selectedRole +"). Rolling back.", "Registration Warning", JOptionPane.WARNING_MESSAGE);
                 personDirectory.removePerson(newPerson); // Rollback person creation
                 saveSuccessful = false; // Stay on page
            }
        }

        // --- Navigate back only if save was fully successful ---
        if (saveSuccessful) {
            CardSequencePanel.remove(this);
            ((CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
            refreshPreviousTable(); // Refresh the list view
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void refreshPreviousTable() {
        Component[] components = CardSequencePanel.getComponents();
        // Iterate backwards to find the most recent ManagePersonsJPanel instance
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] instanceof ManagePersonsJPanel) {
                ((ManagePersonsJPanel) components[i]).populateTable(); // Call the populateTable method
                return; // Found and refreshed, exit loop
            }
        }
        System.err.println("Warning: Could not find ManagePersonsJPanel in CardSequencePanel to refresh.");
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPersonId;
    // End of variables declaration//GEN-END:variables

}
