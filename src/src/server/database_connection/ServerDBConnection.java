
package src.server.database_connection;

import java.io.IOException;
import java.sql.Connection;
import java.net.ServerSocket;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import src.server.Constant;

public class ServerDBConnection {
    private Connection con;
    
    
    public ServerDBConnection(){
        getDBConnection(Constant.dbName, Constant.userDBName, Constant.passDBName);

    }
    private void getDBConnection(String dbName, String username, String password){
        String dbUrl = "jdbc:mysql://127.0.0.1/" + dbName;
        String dbClass = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection (dbUrl,username, password);
            System.out.println("DB Connected");
        }catch(Exception e) {
            e.printStackTrace();
        }   
    }
    
    private boolean getAllUser() throws Exception {
        String query = "Select * FROM user";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                // Retrieve the data for each column
                int id = rs.getInt("id"); // Assuming you have an id column
                String fullname = rs.getString("fullname");
                String username = rs.getString("username");
                String password = rs.getString("password");
                double score = rs.getDouble("score");

                // Print the record to the terminal
                System.out.println("ID: " + id + ", Full Name: " + fullname + 
                                   ", Username: " + username + 
                                   ", Password: " + password + 
                                   ", Score: " + score);
                return true;
            }
        }catch(Exception e) {
            throw e;
        } 
        return true;
 }

    public static void main(String[] args) {
        ServerDBConnection a = new ServerDBConnection();
        try{
            a.getAllUser();
        }catch(Exception e){
            
        }
    }
}
