/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccounts;

import info5100.university.example.Persona.Person;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class UserAccountDirectory {
    
   ArrayList<UserAccount> userAccountList;

    public UserAccountDirectory() {
        userAccountList = new ArrayList<>();
    }

    public ArrayList<UserAccount> getUserAccountList() {
        return userAccountList;
    }

    public UserAccount AuthenticateUser(String un, String pw) {
        for (UserAccount ua : userAccountList) {
            if (ua.IsValidUser(un, pw)) {
                return ua;
            }
        }
        return null;
    }

    public UserAccount newUserAccount(Person person, String un, String pw, String role) {
        UserAccount ua = new UserAccount(person, un, pw, role);
        userAccountList.add(ua);
        return ua;
    }

    public UserAccount findUserAccount(String id) {
        for (UserAccount ua : userAccountList) {
            if (ua.isMatch(id)) {
                return ua;
            }
        }
        return null;
    }
}
