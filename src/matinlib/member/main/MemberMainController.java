/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matinlib.member.main;

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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import matlib.books.issue.Issue;
import matlib.ui.database.Database;
import matlib.ui.listbooks.Book;
import matlib.ui.listcategories.Category;
import matlib.ui.login.LoginController;
import matlib.ui.main.MainController;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class MemberMainController implements Initializable {
    
    ObservableList<Book> searchList = FXCollections.observableArrayList();
    
    ObservableList<Book> booksList = FXCollections.observableArrayList();
    
    ObservableList<Category> categoryList = FXCollections.observableArrayList();
    
    ObservableList<Issue> issuesList = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Book> booksTableView;
    
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
    private TableColumn<Category, String> nameCol;
    @FXML
    private TableView<Category> tableView;
    
    @FXML
    private TableView<Book> searchTableView;
    @FXML
    private TableColumn<Book, String> titleCol1;
    @FXML
    private TableColumn<Book, String> idCol1;
    @FXML
    private TableColumn<Book, String> authorCol1;
    @FXML
    private TableColumn<Book, String> categoryCol1;
    @FXML
    private TableColumn<Book, Boolean> availabilityCol1;

    Database database;
    @FXML
    private TextField searchInput;
    @FXML
    private Text welcomeText;
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
    @FXML
    private StackPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        database = Database.getInstannce();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        // serarch table
        titleCol1.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol1.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol1.setCellValueFactory(new PropertyValueFactory<>("category"));
        availabilityCol1.setCellValueFactory(new PropertyValueFactory<>("availability"));
        // borrowed books
        bookidCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        borrowedCol.setCellValueFactory(new PropertyValueFactory<>("borrowedBy"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // fetch the data from the database
        loadTableData();
        loadBooksTableData();
        loadSearchTableData("");
        loadIssuesTableData();
        
        // set the welcome text 
        welcomeText.setText("" + LoginController.getLoggedUser());
    }    

    
    @FXML
    private void refresh(ActionEvent event) {
        loadBooksTableData();
    }

    private void loadBooksTableData(){
        booksList.clear();
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
    
     private void loadSearchTableData(String search){
         
         if(search == null) return; // sometimes the input.gettext returns null this avoid errors 
         
        searchList.clear();
        String query = "SELECT * FROM books WHERE title LIKE '%"+search+"%' OR category LIKE '%"+search+"%'";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String title = rs.getString("title");
                String id = rs.getString("id");
                String author = rs.getString("author");
                String category = rs.getString("category");
                Boolean avail = rs.getBoolean("isAvail");
                
                searchList.add(new Book(title,id,author,category,avail));
            }
        }catch(SQLException ex){
            
        }
        
        searchTableView.setItems(searchList);
        
    } 
    
     
    // this functions gets the books borrowed by the logged in user. it only works if this page is loaded from the log in screen calling it manualy will result in errors
    private void loadIssuesTableData(){
        issuesList.clear();
        database = Database.getInstannce();
        String query = "SELECT * FROM issued WHERE username='"+LoginController.getLoggedUser()+"'"; // I use the variable user in the login controller to keep track of the user logged in
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String id = rs.getString("id");
                String bookid = rs.getString("bookid");
                String username = "You";
                String status = rs.getString("status");
                String date = rs.getString("date");
                issuesList.add(new Issue(id,bookid,username,date,status));
            }
        }catch(SQLException ex){
            
        }
        
        issuedTableView.setItems(issuesList);
        
    } 
     
    private void loadTableData(){
        database = Database.getInstannce();
        String query = "SELECT * FROM categories";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String name = rs.getString("name");
                String id = rs.getString("id");
                categoryList.add(new Category(name,id)); // add the category to the list
            }
        }catch(SQLException ex){
            
        }
        
        tableView.getItems().setAll(categoryList);
        
    }
    private void search(InputMethodEvent event) {
         loadSearchTableData(searchInput.getText());
    }

    private void search(KeyEvent event) {
        loadSearchTableData(searchInput.getText());
    }

    @FXML
    private void changeIssueStatus(ActionEvent event) { // changes the status of a book 
        Issue issue = issuedTableView.getSelectionModel().getSelectedItem();
        if(issue == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a book you want to return");
            alert.showAndWait();
            return;
        }
        if(issue.getStatusNumber().equals("0")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("You have already returned this book");
            alert.showAndWait();
            return;
        }
        //database.changeBookStatus(issue.getBookId(), true);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return book");
        alert.setContentText("Are you sure you want to return the selected book ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK){
            boolean result = Database.getInstannce().changeIssueStatus(issue);
            if(result){
               loadIssuesTableData();
               loadBooksTableData();
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Book returened successfuly");
                alert.showAndWait();
                    
            return;
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("operation failed");
                    alert.showAndWait();
            }
        
        }else{
        
        
        }
    }

    @FXML
    private void reloadIssuedTable(ActionEvent event) { // this functions just reload the table 
        loadIssuesTableData();
    }

    @FXML
    private void logout(ActionEvent event) {
       loadWindow("/matlib/ui/login/Login.fxml","Login"); // in some pages I have used rootpane and in others rootPane will fix later(Matin)
       Stage stage = (Stage) rootPane.getScene().getWindow();
       stage.close();
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
    
     private boolean checkIfIssued(String id){
    
        String query = "SELECT * FROM issued WHERE bookid='"+id+"' AND status='1'"; // sql injection don't tell anyone(matin)
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
    private void borrowBook(ActionEvent event){
        Book selectedBook = booksTableView.getSelectionModel().getSelectedItem();
        String book = selectedBook.getId();
        String username = LoginController.getLoggedUser();
        
        
        if(checkUserId(username)){// this line is not neccesery
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
            alert.setContentText("This book is not available");
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
            loadIssuesTableData();
            loadBooksTableData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Book borrowed successfuly");
            alert.showAndWait();
        } else { // there was a problem
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("There was an error while borrowing the book please try");
            alert.showAndWait();
        }


    }
    
    private boolean checkBookId(String id){
    
        String query = "SELECT id FROM books WHERE id='"+id+"'";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                return false;
            }
        }catch(SQLException ex){
        }
        
        
        
        return true;
    }
    
    private boolean checkUserId(String id){
    
        String query = "SELECT username FROM users WHERE username='"+id+"'";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                return false;
            }
        }catch(SQLException ex){
        }
        
        return true;
    }

    @FXML
    private void borrowBookSearch(ActionEvent event) {
        Book selectedBook = searchTableView.getSelectionModel().getSelectedItem();
        String book = selectedBook.getId();
        String username = LoginController.getLoggedUser();
        
        
        if(checkUserId(username)){// this line is not neccesery
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
            alert.setContentText("This book is not available");
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
            loadIssuesTableData();
            loadBooksTableData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Book borrowed successfuly");
            alert.showAndWait();
        } else { // there was a problem
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("There was an error while borrowing the book please try");
            alert.showAndWait();
        }
        
    }

    @FXML
    private void openProfile(ActionEvent event) {
        loadWindow("/matinlib/member/profile/Profile.fxml","Your profile");
    }

    @FXML
    private void search(ActionEvent event) {
        loadSearchTableData(searchInput.getText());
    }
    @FXML
    private void searchBook(KeyEvent event) {
          loadSearchTableData(searchInput.getText());
    }

    @FXML
    private void renewMembership(ActionEvent event) {
        Boolean result = checkMembership(LoginController.getLoggedUser());
        
        if (result) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("You have already exteded your membership");
            alert.showAndWait();
            return;
        }
        
        // 
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        String date = dtf.format(now);
        String query = "INSERT INTO membership (username,active,date) VALUES ("
                + "'" + LoginController.getLoggedUser() + "',"
                + "'" + "Membership Renewed" + "',"
                + "'" + date + "'"
                + ")";
        if (database.execAction(query)) {
            loadIssuesTableData();
            loadBooksTableData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Membership renewed successfuly");
            alert.showAndWait();
        } else { // there was a problem
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("There was an error while renewing your membership. please try again");
            alert.showAndWait();
        }
    }
    
    
    private boolean checkMembership(String id){
    
        String query = "SELECT * FROM membership WHERE username='"+id+"'"; 
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
