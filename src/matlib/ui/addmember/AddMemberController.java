/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.addmember;

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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import matlib.ui.database.Database;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class AddMemberController implements Initializable {

        
    @FXML
    private TextField username;
    @FXML
    private TextField name;
    @FXML
    private TextField lastName;
    @FXML
    private TextField password;   
    
    Database database;
    @FXML
    private AnchorPane rootPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
          database = Database.getInstannce();
         checkData();
    }    

    @FXML
    private void cancel(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    
    @FXML
    private void addMember(ActionEvent event){
        
        String Xusername = username.getText();
        String Xname = name.getText();
        String Xlastname = lastName.getText();
        String Xpassword = password.getText();
       
        // check if the inputs are empty if so show a alert
        if(Xusername.isEmpty() || Xname.isEmpty() || Xlastname.isEmpty()|| Xpassword.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("All the feild are required");
            alert.showAndWait();
            return;
        }
        
        // check if the username is already take
        if(checkUserName(Xusername)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("This username already exists");
            alert.showAndWait();
            return;
        }
        
        // add the new user to the data base
        String query = "INSERT INTO users (username,name,lastname,password,type) VALUES ("
                + "'" + Xusername +"',"
                + "'" + Xname + "',"
                + "'" + Xlastname + "',"
                + "'" + Xpassword + "',"
                + "'" + "member" + "'"
                + ")";
        
        //System.out.println(query); // print it for debuging
        
        if(database.execAction(query)){ // check if the process was successfull
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("member added successfuly");
            alert.showAndWait();
        }else{ // there was a problem
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("There was an error while adding the member please try");
            alert.showAndWait();
        }
        
    }
    
    
    // this funtion is not used it's only for debuging
    private void checkData(){
    
        String query = "SELECT username FROM users";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String title = rs.getString("username");
                System.out.println(title);
            }
        }catch(SQLException ex){
        }
    }
    
    // check if a user with this username exists
    private boolean checkUserName(String name){
        
        String query = "SELECT username FROM users WHERE username='"+name+"'";
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
