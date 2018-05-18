/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.listcategories;

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
import matlib.ui.addcategory.AddCategoryController;
import matlib.ui.database.Database;
import matlib.ui.main.MainController;

/**
 * FXML Controller class
 *
 * @author Matin
 */
public class ListCategoriesController implements Initializable {

    ObservableList<Category> list = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableColumn<Category, String> nameCol;
    @FXML
    private TableView<Category> tableView;

    Database database;
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadTableData();
    }    
    
    private void loadTableData(){
        list.clear();
        database = Database.getInstannce();
        String query = "SELECT * FROM categories";
        ResultSet rs = database.execQuery(query);
        try{
            while(rs.next()){
                String name = rs.getString("name");
                String id = rs.getString("id");
                list.add(new Category(name,id)); // add the category to the list
            }
        }catch(SQLException ex){
            
        }
        
        tableView.getItems().setAll(list);
        
    }
    
     private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    @FXML
    private void deleteCategory(ActionEvent event) {
        
        Category category = tableView.getSelectionModel().getSelectedItem();
        if(category == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a category");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting category");
        alert.setContentText("Are you sure you want to delete the category "+ category.getName() +" ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK){
            boolean result = Database.getInstannce().deleteCategory(category);
            
            if(result){
                list.remove(category);
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("category deleted successfuly");
                alert.showAndWait();
                loadTableData();
            return;
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to delete the category");
                    alert.showAndWait();
            
            }
        
        }else{
        
        
        }
        
    }

    @FXML
    private void reloadTable(ActionEvent event) {
        loadTableData();
    }

    @FXML
    private void editCategory(ActionEvent event) {
        Category category = tableView.getSelectionModel().getSelectedItem();
        if(category == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a category");
            alert.showAndWait();
            return;
        }
        // pass data to the add book page not the cleanest code but it works 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/matlib/ui/addcategory/AddCategory.fxml"));
            Parent parent = loader.load();
            
            AddCategoryController controller = (AddCategoryController) loader.getController();
            
            controller.getData(category);
            
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Category");
            stage.setScene(new Scene(parent));
            stage.show();
            
            stage.setOnCloseRequest((e)->{
                loadTableData();
            });
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
