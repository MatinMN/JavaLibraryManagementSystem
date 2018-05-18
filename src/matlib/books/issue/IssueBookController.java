/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.books.issue;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import matlib.ui.database.Database;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class IssueBookController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField bookId;
    @FXML
    private TextField userId;
    
    private Database database;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         database = Database.getInstannce();
         //checkData();
    }    
    
    @FXML
    private void issueBook(ActionEvent event){
        
        String book = bookId.getText();
        String username = userId.getText();

        if (username.isEmpty() || book.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("All the feild are required");
            alert.showAndWait();
            return;
        }
        if(checkUserId(username)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The username entered is invalid");
            alert.showAndWait();
            return;
        
        }
        if(checkBookId(book)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The book entered is invalid");
            alert.showAndWait();
            return;
        }
       if(checkIfIssued(book)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("This book is already issued");
            alert.showAndWait();
            return;
        }
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        String date = dtf.format(now);
        String query = "INSERT INTO issued (username,bookid,date,status) VALUES ("
                + "'" + username + "',"
                + "'" + book + "',"
                + "'" + date + "',"
                + "'" + "1" + "'"
                + ")";
        
        // change the book availability
        database.changeBookStatus(book, false);
        System.out.println(query);

        if (database.execAction(query)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Book issued successfuly");
            alert.showAndWait();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } else { // there was a problem
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("There was an error while issueing the book please try");
            alert.showAndWait();
        }


    }
    
    @FXML
    private void cancel(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    private boolean checkBookId(String id){
    
        String query = "SELECT id FROM books WHERE id='"+id+"'"; // sql injection yea yea I know
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                return false;
            }
        }catch(SQLException ex){
        }
        
        
        
        return true;
    }
    
    
      private boolean checkIfIssued(String id){
    
        String query = "SELECT * FROM issued WHERE bookid='"+id+"' AND status='1'"; // sql injection yea yea I know
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                return true;
            }
        }catch(SQLException ex){
        }
        
        
        return false;
    }
    
     private boolean checkUserId(String id){
    
        String query = "SELECT username FROM users WHERE username='"+id+"'"; // sql injection yea yea I know
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                return false;
            }
        }catch(SQLException ex){
        }
        
        return true;
    }
     
    private boolean check(String id){
    
        String query = "SELECT username FROM users WHERE username='"+id+"'"; // sql injection yea yea I know
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                return false;
            }
        }catch(SQLException ex){
        }
        
        return true;
    }
    
}
