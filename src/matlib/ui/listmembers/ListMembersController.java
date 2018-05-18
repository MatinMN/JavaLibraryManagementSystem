/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.listmembers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import matlib.ui.database.Database;
import matlib.ui.listbooks.Book;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class ListMembersController implements Initializable {
    
     ObservableList<Member> list = FXCollections.observableArrayList();
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> usernameCol;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> LastName;
    @FXML
    private TableColumn<Member, String> passwordCol;

    Database database;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        loadTableData();
    }    
    
    
    private void init(){
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        LastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        
    } 
    
    private void loadTableData(){
        database = Database.getInstannce();
        String query = "SELECT * FROM users";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String username = rs.getString("username");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                String password = rs.getString("password");
                
                list.add(new Member(name,username,lastname,password));
            }
        }catch(SQLException ex){
            
        }
        
        tableView.getItems().setAll(list);
        
    }
    
     private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

}
