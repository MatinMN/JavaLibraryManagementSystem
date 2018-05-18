/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.head.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import matlib.books.issue.Issue;
import matlib.ui.database.Database;
import matlib.ui.listbooks.Book;
import matlib.ui.listmembers.Member;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    Database database;

    ObservableList<Member> usersList = FXCollections.observableArrayList();

    ObservableList<Book> booksList = FXCollections.observableArrayList();

    ObservableList<Issue> issuesList = FXCollections.observableArrayList();

    ObservableList<Activity> activityList = FXCollections.observableArrayList();

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
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private TableColumn<Activity, String> activity;
    @FXML
    private TableColumn<Activity, String> acname; // ac stands for activity
    @FXML
    private TableColumn<Activity, String> acdate;// not a good column name sorry
    @FXML
    private TextArea reportInput;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        database = Database.getInstannce();
        init();
        loadActivityTableData();
        loadUsersTableData();
        loadBooksTableData();
        loadIssuesTableData();
    }

    private void init() {
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
        borrowedCol.setCellValueFactory(new PropertyValueFactory<>("borrowedBy"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        activity.setCellValueFactory(new PropertyValueFactory<>("activity"));
        acname.setCellValueFactory(new PropertyValueFactory<>("username"));
        acdate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadBooksTableData() {
        booksList.clear();
        database = Database.getInstannce();
        String query = "SELECT * FROM books";
        ResultSet rs = database.execQuery(query);
        try {
            while (rs.next()) {
                String title = rs.getString("title");
                String id = rs.getString("id");
                String author = rs.getString("author");
                String category = rs.getString("category");
                Boolean avail = rs.getBoolean("isAvail");

                booksList.add(new Book(title, id, author, category, avail));
            }
        } catch (SQLException ex) {

        }

        booksTableView.setItems(booksList);

    }

    private void loadIssuesTableData() {
        issuesList.clear();
        database = Database.getInstannce();
        String query = "SELECT * FROM issued";
        ResultSet rs = database.execQuery(query);
        try {
            while (rs.next()) {
                String id = rs.getString("id");
                String bookid = rs.getString("bookid");
                String username = rs.getString("username");
                String status = rs.getString("status");
                String date = rs.getString("date");
                issuesList.add(new Issue(id, bookid, username, date, status));
            }
        } catch (SQLException ex) {

        }

        issuedTableView.setItems(issuesList);

    }

    private void loadActivityTableData() {
        //activityList.clear();
        String query = "SELECT * FROM activity";
        ResultSet rs = database.execQuery(query);
        try {
            while (rs.next()) {
                String Xuser = rs.getString("username");
                String Xactivity = rs.getString("action");
                String Xdate = rs.getString("date");
                String Xid = rs.getString("id");
                activityList.add(new Activity(Xid,Xuser, Xactivity, Xdate));
            }
        } catch (SQLException ex) {

        }

        activityTableView.setItems(activityList);
    }

    private void loadUsersTableData() {
        usersList.clear();
        database = Database.getInstannce();
        String query = "SELECT * FROM users";
        ResultSet rs = database.execQuery(query);
        try {
            while (rs.next()) {
                String username = rs.getString("username");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                String password = rs.getString("password");

                usersList.add(new Member(name, username, lastname, password));
            }
        } catch (SQLException ex) {

        }

        usersTableView.getItems().setAll(usersList);

    }

    @FXML
    private void logout(ActionEvent event) {
        loadWindow("/matlib/ui/login/Login.fxml", "Login");
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    // this function opens a window
    void loadWindow(String location, String title) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(matlib.ui.main.MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void refresh(ActionEvent event) {
        loadBooksTableData();
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
    private void loadCategories(ActionEvent event) { // opens the window list categories 
        loadWindow("/matlib/ui/listcategories/ListCategories.fxml", "List of categories");
    }

    @FXML
    private void generateReport(ActionEvent event) throws IOException {
        if (reportInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please type something first");
            alert.showAndWait();
            return;
        }
        Report();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Report was generated successfuly at folder reports");
        alert.showAndWait();
    }

    // this function is called when the button genereate report is pressed
    public void Report() throws IOException, FileNotFoundException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd;HH-mm");
        Calendar cal = Calendar.getInstance();
        String s = dateFormat.format(cal.getTime());

        File report = new File(".\\reports\\" + s + ".txt");

        PrintWriter Report = new PrintWriter(report);

        FileWriter ToReport = new FileWriter(report);

        ToReport.write(reportInput.getText());

        ToReport.close();

    }

    @FXML
    private void deleteMember(ActionEvent event) {
        Member member = usersTableView.getSelectionModel().getSelectedItem();
        if (member == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a member");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Member");
        alert.setContentText("Are you sure you want to delete the member  " + member.getUsername() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            boolean result = Database.getInstannce().deleteMember(member);

            if (result) {
                usersList.remove(member);
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Member deleted successfuly");
                alert.showAndWait();
                reloadUsersTable(event);
                return;
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete the member");
                alert.showAndWait();

            }

        } else {

        }
    }

    @FXML
    private void deleteActivity(ActionEvent event) {
        Activity activity = activityTableView.getSelectionModel().getSelectedItem();
        if (activity == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a record");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting record");
        alert.setContentText("Are you sure you want to delete the record selected on date" + activity.getDate() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            boolean result = Database.getInstannce().deleteActivity(activity);

            if (result) {
                activityList.remove(activity);
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("record deleted successfuly");
                alert.showAndWait();
                reloadUsersTable(event);
                return;
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete the record");
                alert.showAndWait();

            }

        } else {

        }
    }
}
