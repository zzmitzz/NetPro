package src.client.data.dto;


import src.client.data.dto.*;
import java.io.Serializable;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    public User(String fullName, String username, String password, double score){
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.score = score;
    }
    
    private String fullName;
    private String username;
    private String password;
    public double score = 0.0;

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public double getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }
    
    
    
}
