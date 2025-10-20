/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import info5100.university.example.Department.Department;
import Business.UserAccounts.UserAccountDirectory;

/**
 *
 * @author kal bugrara
 */
public class Business {

    String name;
    UserAccountDirectory userAccountDirectory;
    Department department; // add department
    

    public Business(String n) {
        name = n;

        userAccountDirectory = new UserAccountDirectory();
        department = new Department(n); // Initailize Department
        
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return userAccountDirectory;
    }
    
    // add getter
    public Department getDepartment() {
        return department;
    }
}
