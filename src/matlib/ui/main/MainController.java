/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.main;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import matlib.books.issue.Issue;
import matlib.ui.addbook.AddBook;
import matlib.ui.database.Database;
import matlib.ui.listbooks.Book;
import matlib.ui.listmembers.Member;
import matlib.ui.login.LoginController;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class MainController implements Initializable {

    
    Database database;
    
    ObservableList<Member> usersList = FXCollections.observableArrayList();
    
    ObservableList<Book> booksList = FXCollections.observableArrayList();
    
    ObservableList<Issue> issuesList = FXCollections.observableArrayList();
    
    @FXML
    private StackPane rootPane;
    
    // books table view 
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
    
    // users table 
    @FXML
    private TableColumn<Member, String> usernameCol;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> LastName;
    @FXML
    private TableColumn<Member, String> passwordCol;
    @FXML
    private TableView<Book> booksTableView;
    @FXML
    private TableView<Member> usersTableView;
    @FXML
    private TableView<Issue> issuedTableView;
    @FXML
    private TableColumn<Issue, String> bookidCol;
    @FXML
    private TableColumn<Issue, String> borrowedCol;
    @FXML
    private TableColumn<Issue, String> dateCol;
    @FXML
    private TableColumn<Issue, String> statusCol;

    /**
     * THe main class with most of the code need some cleaning up(Matin).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        database = Database.getInstannce();
        init();
        loadUsersTableData();
        loadBooksTableData();
        loadIssuesTableData();
    }    
    
     private void init(){
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        LastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        bookidCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        borrowedCol.setCellValueFactory(new PropertyValueFactory<>("borrowedBy"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
     
    private void loadBooksTableData(){
        booksList.clear();
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
                
                booksList.add(new Book(title,id,author,category,avail));
            }
        }catch(SQLException ex){
            
        }
        
        booksTableView.setItems(booksList);
        
    } 
    
    private void loadIssuesTableData(){
        issuesList.clear();
        database = Database.getInstannce();
        String query = "SELECT * FROM issued";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String id = rs.getString("id");
                String bookid = rs.getString("bookid");
                String username = rs.getString("username");
                String status = rs.getString("status");
                String date = rs.getString("date");
                issuesList.add(new Issue(id,bookid,username,date,status));
            }
        }catch(SQLException ex){
            
        }
        
        issuedTableView.setItems(issuesList);
        
    } 
    
     private void loadUsersTableData(){
        usersList.clear();
        database = Database.getInstannce();
        String query = "SELECT * FROM users WHERE type='member'";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String username = rs.getString("username");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                String password = rs.getString("password");
                
                usersList.add(new Member(name,username,lastname,password));
            }
        }catch(SQLException ex){
            
        }
        
        usersTableView.getItems().setAll(usersList);
        
    }
    
    void loadWindow(String location,String title){
    
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    @FXML
    private void deleteBook(ActionEvent event) {
        
        Book book = booksTableView.getSelectionModel().getSelectedItem();
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
                booksList.remove(book);
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
        
        Book book = booksTableView.getSelectionModel().getSelectedItem();
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
                loadBooksTableData();
            });
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void refresh(ActionEvent event){
        loadBooksTableData();
    }

    private void changeStatus(ActionEvent event) {
        Book book = booksTableView.getSelectionModel().getSelectedItem();
        if(book == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a book");
            alert.showAndWait();
            return;
        }
        
        //book.setStatus(!book.getAvailability()); // change the status (still need to work on the borrowed books table )
        loadBooksTableData();
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/matlib/ui/addbook/AddBook.fxml","Add Book");
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/matlib/ui/addmember/AddMember.fxml","Add Member");
    }

    @FXML
    private void loadAddCategory(ActionEvent event) {
        loadWindow("/matlib/ui/addcategory/AddCategory.fxml","Add Category");
    }

    @FXML
    private void loadIssuebook(ActionEvent event) {
         loadWindow("/matlib/books/issue/IssueBook.fxml","Issue a book");
    }

   
    @FXML
    private void logout(ActionEvent event) {
       // add the clock out log to the database
       DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        String q = "INSERT INTO activity (username,action,date) VALUES ('"
                + LoginController.getLoggedUser() + "',"
                + "'Clock Out'" + ",'"
                + date + "'"
                + ")";

        System.out.println(q);
        if (database.execAction(q)) {
            System.out.println("clock out");
        } else {
            System.out.println("error clocking in");
        }
        
       loadWindow("/matlib/ui/login/Login.fxml","Login");
       Stage stage = (Stage) rootPane.getScene().getWindow();
       stage.close();
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

    @FXML
    private void changeIssueStatus(ActionEvent event) {
        Issue issue = issuedTableView.getSelectionModel().getSelectedItem();
        if(issue == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select an issue");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("changing status");
        alert.setContentText("Are you sure you want change the status of issue "+ issue.getId() +" ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK){
            boolean result = Database.getInstannce().changeIssueStatus(issue);
            if(result){
               loadIssuesTableData();
               loadBooksTableData();
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Issue status changed successfuly");
                alert.showAndWait();
            return;
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("status change failed");
                    alert.showAndWait();
            }
        
        }else{
        
        
        }
       
    }

    @FXML
    private void reloadUsersTable(ActionEvent event) {
        loadUsersTableData();
    }

    @FXML
    private void reloadIssuedTable(ActionEvent event) {
        loadIssuesTableData();
    }

    @FXML
    private void loadCategories(ActionEvent event) {
         loadWindow("/matlib/ui/listcategories/ListCategories.fxml","List of categories");
    }

    @FXML
    private void removeMember(ActionEvent event) {
         Member member = usersTableView.getSelectionModel().getSelectedItem();
        if(member == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a member");
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting member");
        alert.setContentText("Are you sure you want to delete the member  "+ member.getUsername() +" ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK){
            boolean result = Database.getInstannce().deleteMember(member);
            
            if(result){
                usersList.remove(member);
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Member deleted successfuly");
                alert.showAndWait();
                reloadUsersTable(event);
            return;
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to delete the member");
                    alert.showAndWait();
            
            }
        
        }else{
        
        
        }        
    }
   
}
