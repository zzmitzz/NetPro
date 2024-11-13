package src.server.database_connection;

import java.io.IOException;
import java.sql.Connection;
import java.net.ServerSocket;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import src.client.data.dto.User;
import src.model.Question;
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
    public boolean doRegisterRequest(String fullname, String username, String password) {
        String sql = "INSERT INTO user (fullname, username, password, score) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, fullname);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setDouble(4, 0.0);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return false;  
    }

    public ArrayList<Question> getQuestions(int packOrder) {
        ArrayList<Question> result =  new ArrayList<Question>();
        String query = "Select * FROM quiz Where packOrder = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, packOrder);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("ID"); // Assuming you have an id column
                String question = rs.getString("Question");
                String answer = rs.getString("Answer");
                int firstIndex = rs.getInt("FirstIndex");
                Question q = new Question(question,answer,firstIndex);
                result.add(q);
            }
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
    public static void main(String[] args) {
        ServerDBConnection a = new ServerDBConnection();
        List<Question> tmp = a.getQuestions(1);
        for(Question it : tmp){
            System.out.println(it.ques);
        }
    }
}
