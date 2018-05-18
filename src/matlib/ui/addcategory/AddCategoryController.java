/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.addcategory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import matlib.ui.database.Database;
import matlib.ui.listbooks.Book;
import matlib.ui.listcategories.Category;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class AddCategoryController implements Initializable {

    @FXML
    private Button cancel;
    
    Database database;
    @FXML
    private TextField category;
    
    private String id;
    
    @FXML
    private AnchorPane rootPane;

    private boolean isEditOn = false;
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
    private void addCategory(ActionEvent event){
        String name = category.getText();
        
        if(name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("All the feild are required");
            alert.showAndWait();
            return;
        }
        
        if(checkData(name)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Category already added");
            alert.showAndWait();
            return;
        }
        
        if (isEditOn) {
            Category category = new Category(name,id);
            boolean result = database.updateCategory(category);
            if (result) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("category edited successfuly");
                alert.showAndWait();
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("There was an error while editing the category please try");
                alert.showAndWait();
            }
        } else {
            String query = "INSERT INTO categories (name) VALUES ("
                    + "'" + name + "'"
                    + ")";
            System.out.println(query);

            if (database.execAction(query)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("category added successfuly");
                alert.showAndWait();
            } else { // there was a problem
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("There was an error while adding the category please try");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void cancel(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    private boolean checkData(String id){
    
        String query = "SELECT name FROM categories WHERE name='"+ id+"'";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String title = rs.getString("name");
                return true;
            }
        }catch(SQLException ex){
        }
        return false;
    }

    public void getData(Category cat){
        category.setText(cat.getName());
        this.id = cat.getId();
        isEditOn = true;
    }
}
