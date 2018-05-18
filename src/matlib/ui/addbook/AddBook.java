/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.addbook;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import matlib.ui.database.Database;
import matlib.ui.listbooks.Book;
import matlib.ui.listbooks.ListBooks;

/**
 *
 * @author Matin
 */
public class AddBook implements Initializable {
    
    private Label label;
    @FXML
    private TextField title;
    @FXML
    private TextField id;
    @FXML
    private TextField author;
    
       
    Database database;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField category;
    
    private boolean isEditOn = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = Database.getInstannce();
        checkData();
    }    
    
    @FXML
    private void addBook(ActionEvent event){
        
        String bookId = id.getText();
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookCategory = category.getText();
        
        if (bookId.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookCategory.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("All the feild are required");
            alert.showAndWait();
            return;
        }
        
        if(isEditOn){
            Book book = new Book(bookName,bookId,bookAuthor,bookCategory,true);
            boolean result = database.updateBook(book);
            if(result){
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Book edited successfuly");
                alert.showAndWait();
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            }else{
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("There was an error while editing the book please try");
                alert.showAndWait();
                
            }
        }else{
            

            String query = "INSERT INTO books (id,title,author,category,isAvail) VALUES ("
                    + "'" + bookId+"',"
                    + "'" + bookName+"',"
                    + "'" + bookAuthor+"',"
                    + "'" + bookCategory+"',"
                    + "" + true+ ""
                    + ")";
            //System.out.println(query);

            if(database.execAction(query)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Book added successfuly");
                alert.showAndWait();
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            }else{ // there was a problem
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("There was an error while adding the book please try");
                alert.showAndWait();
            }
             
        }
    }
    
    @FXML
    private void cancel(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    private void checkData(){
    
        String query = "SELECT title FROM books";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                //String title = rs.getString("title");
                //System.out.println(title);
            }
        }catch(SQLException ex){
        }
    }
    
    
    public void getData(Book book){
        
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        category.setText(book.getCatrgory());
        id.setEditable(false);
        isEditOn = true;
    }
}
