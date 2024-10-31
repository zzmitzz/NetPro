package src.server.database_connection;

import java.io.IOException;
import java.sql.Connection;
import java.net.ServerSocket;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import src.client.data.dto.User;
import src.server.Constant;

public class ServerDBConnection implements DBAction{

    private Connection con;

    public ServerDBConnection() {
        getDBConnection(Constant.dbName, Constant.userDBName, Constant.passDBName);

    }

    private void getDBConnection(String dbName, String username, String password) {
        String dbUrl = "jdbc:mysql://127.0.0.1/" + dbName;
        String dbClass = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(dbUrl, username, password);
            System.out.println("DB Connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    @Override
    public ArrayList<User> getAllUser() {
        ArrayList<User> result =  new ArrayList<User>();
        String query = "Select * FROM user";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                // Retrieve the data for each column
                int id = rs.getInt("id"); // Assuming you have an id column
                String fullname = rs.getString("fullname");
                String username = rs.getString("username");
                String password = rs.getString("password");
                double score = rs.getDouble("score");
                result.add(new User(fullname,username,password,score));
                System.out.println(fullname);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public User doLoginRequest(String username, String password) {
        User result = null;
        String query = "SELECT * FROM user WHERE username = ? AND password = ? ";
        try(PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1,username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id"); // Assuming you have an id column
                String fullnameRes = rs.getString("fullname");
                String usernameRes = rs.getString("username");
                String passwordRes = rs.getString("password");
                double score = rs.getDouble("score");
                result = new User(fullnameRes,usernameRes,passwordRes, score);
            }
            return result;
        } catch (Exception e) {
        }
        return null;  
    }

    @Override
    public boolean doRegisterRequest(String username, String password) {
        String query = "INSERT INTO user (username,password) VALUES (?,?) ";
        try(PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1,username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            // Execute the update
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
        }
        return false;  
    }
    
    
}
