/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.listmembers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Matin
 */
public class Member {
         final SimpleStringProperty name;
         final SimpleStringProperty username;
         final SimpleStringProperty lastname;
         final SimpleStringProperty password;
        
        public Member(String name,String username,String lastname,String password){
            this.name = new SimpleStringProperty(name);
            this.username = new SimpleStringProperty(username);
            this.lastname = new SimpleStringProperty(lastname);
            this.password = new SimpleStringProperty(password);
            
        }

    public String getName() {
        return name.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getLastname() {
        return lastname.get();
    }

    public String getPassword() {
        return password.get();
    }
        

       
}
