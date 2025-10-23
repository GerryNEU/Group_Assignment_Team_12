/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Persona;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class PersonDirectory {
    
      ArrayList<Person> personlist ;
    
      public PersonDirectory (){
          
       personlist = new ArrayList();

    }

    public Person newPerson(String id) {

        Person p = new Person(id);
        personlist.add(p);
        return p;
    }

    public Person findPerson(String id) {

        for (Person p : personlist) {

            if (p.isMatch(id)) {
                return p;
            }
        }
        
        return null; //not found after going through the whole list
    }
    
    public Person findPersonByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        
        for (Person person : personlist) {
            // Use equal() for safe string comparison
            if (email.equals(person.getEmail())) {
                return person;
            }
        }
        
        return null; // Not found
    }
    
    public void deletePerson(Person person) {
        if (person != null) {
            personlist.remove(person);
        }
    }

    public ArrayList<Person> getPersonlist() {
        return personlist;
    }

}
