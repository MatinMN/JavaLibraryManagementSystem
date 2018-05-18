/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.head.main;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Matin
 */
public class Activity {
  
    SimpleStringProperty id;
    SimpleStringProperty username;
    SimpleStringProperty activity;
    SimpleStringProperty date;

    public Activity(String id,String username, String activity, String date) {
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.activity = new SimpleStringProperty(activity);
        this.date = new SimpleStringProperty(date);
       
       
    }

    public String getUsername() {
        return username.get();
    }
    
    public String getId() {
        return id.get();
    }
    public String getActivity() {
        return activity.get();
    }

    public String getDate() {
        return date.get();
    }
    
    
}
