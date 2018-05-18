/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlib.ui.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import matlib.books.issue.Issue;
import matlib.head.main.Activity;
import matlib.ui.listbooks.Book;
import matlib.ui.listcategories.Category;
import matlib.ui.listmembers.Member;
//import javax.swing.JOptionPane;

/**
 *
 * @author Matin
 */
public class Database {
    
    private static Database handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement sql = null;
    
    
    public Database(){
        createConnection();
        // these functions set up the data only need to run it once it is not needed to run if the programm is compiled 
//        setupBookTable();
//        setupUsersTable();
 //       setupCategoriesTable();
//        setupIssuedBooksTable();
//        setupActivityTable();
//        setupMemberShipTable();
        // add the head librarian and librarians here (there is no register so they are added to the system manualy) only need to run once
//        execAction("INSERT INTO users (username,name,lastname,password,type) VALUES ('admin','admin','admin','admin','head')");
//        execAction("INSERT INTO users (username,name,lastname,password,type) VALUES ('matin','matin','Mazloom','matin','librarian')");
//        execAction("INSERT INTO users (username,name,lastname,password,type) VALUES ('ayoob','ayoob','Mohammed Hassan','ayoob','librarian')");
//        execAction("INSERT INTO users (username,name,lastname,password,type) VALUES ('omar','omar','ABDELMOAMEN AMIN HASSAN','omar','librarian')");

//          
    }
    
    public static Database getInstannce(){
    
        if(handler ==null){
            handler = new Database();
        }
        return handler;
    }
    
     private static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
     
    void setupBookTable(){
        String TABLE_NAME = "books";
        
        try {
            sql = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME,null);
            
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else{
                sql.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  id varchar(200) primary key,\n"
                        + "  title varchar(200),\n"
                        + "  category varchar(200),\n"
                        + "  author varchar(200),\n"
                        + "  description varchar(255),\n "
                        + "  intcode varchar(100),\n"
                        + "  isAvail boolean default true"
                        + " )");
            }
            
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }finally{
        }
    
    }
    
    
    void setupActivityTable(){
        String TABLE_NAME = "activity";
        
        try {
            sql = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME,null);
            
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else{
                sql.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  id INT not null primary key \n GENERATED ALWAYS AS IDENTITY \n (START WITH 1, INCREMENT BY 1) ,\n"
                        + "  username VARCHAR(255), \n"
                        + "  action VARCHAR(255), \n"
                        + "  date VARCHAR(255) \n"
                        + " )");
            }
            
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }finally{
        }
    }
    
    void setupMemberShipTable(){
        String TABLE_NAME = "membership";
        
        try {
            sql = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME,null);
            
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else{
                sql.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  id INT not null primary key \n GENERATED ALWAYS AS IDENTITY \n (START WITH 1, INCREMENT BY 1) ,\n"
                        + "  username VARCHAR(255), \n"
                        + "  active VARCHAR(255), \n"
                        + "  date VARCHAR(255) \n"
                        + " )");
            }
            
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }finally{
        }
    }
    void setupUsersTable(){
        String TABLE_NAME = "users";
        
        try {
            sql = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME,null);
            
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else{
                sql.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  username varchar(200) primary key,\n"
                        + "  name varchar(200),\n"
                        + "  lastname varchar(200),\n"
                        + "  type varchar(50),\n" // user level member , librarian or head librarian 
                        + "  password varchar(200) "
                        + " )");
            }
            
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }finally{
        }
    
    }
    
    void setupCategoriesTable(){
        String TABLE_NAME = "categories";
        
        try {
            sql = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME,null);
            
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else{
                sql.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  id INT not null primary key \n GENERATED ALWAYS AS IDENTITY \n (START WITH 1, INCREMENT BY 1) ,\n"
                        + "  name varchar(200) \n"
                        + " )");
            }
            
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }finally{
        }
    }
    
    void setupIssuedBooksTable(){
        String TABLE_NAME = "issued";
        
        try {
            sql = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME,null);
            
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else{
                sql.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  id INT not null primary key \n GENERATED ALWAYS AS IDENTITY \n (START WITH 1, INCREMENT BY 1) ,\n"
                        + "  username VARCHAR(255), \n"
                        + "  bookid VARCHAR(255), \n"
                        + "  status VARCHAR(255), \n"
                        + "  date VARCHAR(255) \n" // lazy I know . but derby has some wired stuff unlike mysql (Matin)
                        + " )");
            }
            
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }finally{
        }
    }
    
    
     public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            sql = conn.createStatement();
            result = sql.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }
     
      public boolean deleteBook(Book book) {
        try {
            String deleteStatement = "DELETE FROM books WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, book.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            
        }
        return false;
    }
    
    public boolean deleteMember(Member member) {
        try {
            String deleteStatement = "DELETE FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, member.getUsername());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            
        }
        return false;
    }
    
    public boolean deleteActivity(Activity activity) {
        try {
            String deleteStatement = "DELETE FROM activity WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, activity.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            
        }
        return false;
    }
    
    public boolean deleteCategory(Category category) {
        try {
            String deleteStatement = "DELETE FROM categories WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, category.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            
        }
        return false;
    }
    public boolean execAction(String qu) {
        try {
            sql = conn.createStatement();
            sql.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }
    
    // edit matin 
    public String bookInfo(String id,String get){
        String query = "SELECT * FROM books WHERE id='"+id+"'";
        ResultSet rs = this.execQuery(query);
        String result = "";
        try{
            while(rs.next()){
                return rs.getString(get);
            }
        }catch(SQLException ex){
            
        }
        return "";
    }
    
    // edit matin 
    public Member userInfo(String id){
        String query = "SELECT * FROM users WHERE username='"+id+"'";
        ResultSet rs = this.execQuery(query);
        String result = "";
        try{
            while(rs.next()){
                return new Member(rs.getString("name"),rs.getString("username"),rs.getString("lastname"),rs.getString("password"));
            }
        }catch(SQLException ex){
            
        }
        return new Member("error","error","error","error"); // this only happens if someone tries skip the login page using some hack this should never happen if it does well you get error 
    }
    
    public boolean updateBook(Book book) {
        try {
            String update = "UPDATE books SET title=?, author=?, category=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getCatrgory());
            stmt.setString(4, book.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            //return false;
        }
        return false;
    }
    
       public boolean updateCategory(Category category) {
        try {
            String update = "UPDATE categories SET name=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            //return false;
        }
        return false;
    }
    
    public boolean updateMember(Member member) {
        try {
            String update = "UPDATE users SET name=?, lastname=?, password=? WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getLastname());
            stmt.setString(3, member.getPassword());
            stmt.setString(4, member.getUsername());
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            //return false;
        }
        return false;
    }
    
    public boolean changeIssueStatus(Issue issue){
        try {
            String update = "UPDATE issued SET status=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(update);
            String status = (issue.getStatusNumber().equals("1")) ? "0" : "1";
            boolean st = (issue.getStatusNumber().equals("1")) ? true : false;
            stmt.setString(1, status);
            stmt.setString(2, issue.getId());
            int res = stmt.executeUpdate();
            changeBookStatus(issue.getBookId(),st); // can be improved in the future
            return (res > 0);
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            //return false;
        }
        
        return false;
    }
    
    public boolean changeBookStatus(String id,boolean status){
        try {
            String update = "UPDATE books SET isAvail=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setBoolean(1, status);
            stmt.setString(2, id); 
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            //return false;
        }
        return false;
    }

}
