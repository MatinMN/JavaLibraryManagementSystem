/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matinlib.member.profile;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import matlib.ui.database.Database;
import matlib.ui.listbooks.Book;
import matlib.ui.listmembers.Member;
import matlib.ui.login.LoginController;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class ProfileController implements Initializable {

    @FXML
    private TextField userName;
    @FXML
    private TextField name;
    @FXML
    private TextField lastname;
    @FXML
    private TextField password;

    private Member member;
    
    private Database database;
    @FXML
    private AnchorPane rootPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = Database.getInstannce();
        member = database.userInfo(LoginController.getLoggedUser()); // a user most be logged for this to work
        userName.setText(member.getUsername());
        name.setText(member.getName());
        lastname.setText(member.getLastname());
        password.setText(member.getPassword());
        userName.setEditable(false); // dont' allow the user to change his username
    }    

    @FXML
    private void updateProfile(ActionEvent event) {
        String editedName = name.getText();
        String editedLastname = lastname.getText();
        String editedPassword = password.getText(); 
        
        // check if anything has change
        if(editedName.equals(member.getName()) && editedLastname.equals(member.getLastname()) && editedPassword.equals(member.getPassword())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("No changes made");
            alert.showAndWait();
            return;
        }
        // update profile
        Member edits = new Member(editedName,member.getUsername(),editedLastname,editedPassword);
        boolean result = database.updateMember(edits);
        if (result) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Profile updated successfuly");
            alert.showAndWait();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("There was an error while updating your profile. please try");
            alert.showAndWait();

        }
        
    }

    @FXML
    private void cancel(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
}
