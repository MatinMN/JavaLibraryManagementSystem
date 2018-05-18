/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.listbooks;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Matin
 */
public class Book {
         public SimpleStringProperty title;
         public  SimpleStringProperty id;
         public SimpleStringProperty author;
         public SimpleStringProperty category;
         public SimpleBooleanProperty availability;
        
        public Book(String title,String id,String author,String category, Boolean avail){
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.id = new SimpleStringProperty(id);
            this.category = new SimpleStringProperty(category);
            this.availability = new SimpleBooleanProperty(avail);
            
        }
        
        public void setStatus(boolean status){
         this.availability = new SimpleBooleanProperty(status);
        }
        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getCatrgory() {
            return category.get();
        }

        public String getCategory() {
            return category.get();
        }
        public boolean getAvailability() {
             return availability.get();
        }
    
}
