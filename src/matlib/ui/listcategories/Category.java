/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.listcategories;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Matin
 */
public class Category {
       final SimpleStringProperty name;
       final SimpleStringProperty id;
       
        public Category(String name,String id){
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
        }

     public String getName() {
        return name.get();
    }
     
    public String getId() {
        return id.get();
    }
}
