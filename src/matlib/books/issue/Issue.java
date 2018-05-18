/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.books.issue;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Matin
 */
public class Issue {
    public SimpleStringProperty id;
    public SimpleStringProperty bookId;
    public SimpleStringProperty borrowedBy;
    public SimpleStringProperty date;
    public SimpleStringProperty status;

    public Issue(String id,String bookId, String borrowedBy, String date, String status) {
        this.id = new SimpleStringProperty(id);
        this.bookId = new SimpleStringProperty(bookId);
        this.borrowedBy = new SimpleStringProperty(borrowedBy);
        this.date = new SimpleStringProperty(date);
        this.status = new SimpleStringProperty(status);
    }

    public String getId() {
        return id.get();
    }
    
    public String getBookId() {
        return bookId.get();
    }

    public String getBorrowedBy() {
        return borrowedBy.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getStatus() {
        return (status.get().equals("1")?"not returened" : "returned");
    }
    
    public String getStatusNumber() {
        return status.get();
    }
    
}
