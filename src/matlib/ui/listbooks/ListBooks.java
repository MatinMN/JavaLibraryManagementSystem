/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.listbooks;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import matlib.ui.addbook.AddBook;
import matlib.ui.database.Database;
import matlib.ui.main.MainController;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class ListBooks implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();
    
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> categoryCol;
    @FXML
    private TableColumn<Book, Boolean> availabilityCol;
    @FXML
    private TableView<Book> tableView;

    Database database;
    @FXML
    private AnchorPane rootPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        
        loadTableData();
    }    
    
    
    private void init(){
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }
    
    
    private void loadTableData(){
        list.clear();
        database = Database.getInstannce();
        String query = "SELECT * FROM books";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String title = rs.getString("title");
                String id = rs.getString("id");
                String author = rs.getString("author");
                String category = rs.getString("category");
                Boolean avail = rs.getBoolean("isAvail");
                
                list.add(new Book(title,id,author,category,avail));
            }
        }catch(SQLException ex){
            
        }
        
        tableView.setItems(list);
        
    }
    
    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    @FXML
    private void deleteBook(ActionEvent event) {
        
        Book book = tableView.getSelectionModel().getSelectedItem();
        if(book == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a book");
            alert.showAndWait();
            return;
        }
        
        if(checkIfIssued(book.getId())){
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setHeaderText(null);
                alert1.setContentText("This book is already issued to someone please cancel it befor you delete this book");
                alert1.showAndWait();
                return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting book");
        alert.setContentText("Are you sure you want to delete the book "+ book.title.get() +" ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK){
            
            boolean result = Database.getInstannce().deleteBook(book);
            
            if(result){
                list.remove(book);
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Book deleted successfuly");
                alert.showAndWait();
                    
            return;
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Book deletetion failed");
                    alert.showAndWait();
            
            }
        
        }else{
        
        
        }
        
    }

    @FXML
    private void editBook(ActionEvent event) {
        
        Book book = tableView.getSelectionModel().getSelectedItem();
        if(book == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a book");
            alert.showAndWait();
            return;
        }
        // pass data to the add book page not the cleanest code but it works 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/matlib/ui/addbook/AddBook.fxml"));
            Parent parent = loader.load();
            
            AddBook controller = (AddBook) loader.getController();
            
            controller.getData(book);
            
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit book");
            stage.setScene(new Scene(parent));
            stage.show();
            
            stage.setOnCloseRequest((e)->{
                loadTableData();
            });
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void refresh(ActionEvent event){
        loadTableData();
    }

    @FXML
    private void changeStatus(ActionEvent event) {
        Book book = tableView.getSelectionModel().getSelectedItem();
        if(book == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a book");
            alert.showAndWait();
            return;
        }
        
        book.setStatus(!book.getAvailability()); // change the status (still need to work on the borrowed books table )
        loadTableData();
    }
    
      private boolean checkIfIssued(String id){
    
        String query = "SELECT * FROM issued WHERE bookid='"+id+"' AND status='1'"; // sql injection yea yea I know
        System.out.println(query);
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                return true;
            }
        }catch(SQLException ex){
        }
        
        
        return false;
    }
}
