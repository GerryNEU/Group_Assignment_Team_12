/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Persona;

/**
 *
 * @author kal bugrara
 */
public class Person {
    
    private String personId;
    private String name;
    
    public Person (String id){
        
        this.personId = id;
    }
    public String getPersonId(){
        return personId;
    }

    public boolean isMatch(String id){
        if (getPersonId().equals(id)) return true;
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    
}
