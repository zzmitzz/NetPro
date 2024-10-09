package src.client.data.dto;

public class User {
    
    public User(String fullName, String username, String password, double score){
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.score = score;
    }
    
    public String fullName;
    private String username;
    private String password;
    public double score = 0.0;
    
    
}
