/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.login;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import matlib.ui.database.Database;
import matlib.ui.main.MainController;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class LoginController implements Initializable {

    @FXML
    private TextField username;
    
    
    Database database;
    
    @FXML
    private PasswordField password;
    @FXML
    private AnchorPane rootpane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        database = Database.getInstannce();
    }    
    
    private static String user = null ; // user name of the member logged in it is used latter on in the other screens
    @FXML
    private void login(ActionEvent event) {
        boolean login = false;
        String Xusername = username.getText();
        String Xpassword = password.getText();
        if(Xusername.isEmpty() || Xpassword.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Plase enter your username and password");
            alert.showAndWait();
            return;
        }
        
        String query = "SELECT username,type FROM users WHERE username='"+ username.getText() +"' AND password='"+ password.getText() +"' ";
        String memberType = "";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                login = true;
                memberType = rs.getString("type") == null ? "" : rs.getString("type") ;
                user = rs.getString("username");
            }
        }catch(SQLException ex){
            
        }
        
        if(login == true){
            if(memberType.equals("member")){
                loadWindow("/matinlib/member/main/MemberMain.fxml", "MatinLib Library managment system (Loged in as a member)");
                Stage stage = (Stage) rootpane.getScene().getWindow();
                stage.close();
            }else if(memberType.equals("librarian")){
                // add the clock in 
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                LocalDateTime now = LocalDateTime.now();  
                String date = dtf.format(now);
                    String q = "INSERT INTO activity (username,action,date) VALUES ('"
                            + user +  "',"
                            + "'Clock In'" +", '"
                            + date+"'"
                + ")";
        
               System.out.println(q); // for testing 
               if(database.execAction(q)){
                System.out.println("clock in");   
               }else{
                   System.out.println("error clocking in");
               }
                
                
                // 
                loadWindow("/matlib/ui/main/Main.fxml","MatLib Library managment system (Loged in as a librarian)");
                Stage stage = (Stage) rootpane.getScene().getWindow();
                stage.close();
            }else if(memberType.equals("head")){
                loadWindow("/matlib/head/main/Main.fxml","MatLib Library managment system (Loged in as a head librarian)");
                Stage stage = (Stage) rootpane.getScene().getWindow();
                stage.close();
            }
        }else{
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username and password.\n plase try again");
            alert.showAndWait();
            return;
        }
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
    
    
    public static String getLoggedUser(){ 
        return user;
    }
}
