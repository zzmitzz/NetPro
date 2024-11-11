package src.client.presentation.home_screen;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Player {
    private final SimpleStringProperty fullname;
    private final SimpleStringProperty username;
    private final SimpleIntegerProperty score;

    public Player(String fullName, String username, int score) {
        this.fullname = new SimpleStringProperty(fullName);
        this.username = new SimpleStringProperty(username);
        this.score = new SimpleIntegerProperty(score);
    }

    // Getters
    public String getFullname() { return fullname.get(); }
    public String getUsername() { return username.get(); }
    public int getScore() { return score.get(); }

    // Property accessors
    public SimpleStringProperty fullNameProperty() { return fullname; }
    public SimpleStringProperty usernameProperty() { return username; }
    public SimpleIntegerProperty scoreProperty() { return score; }

    // Setters
    public void setFullname(String value) { fullname.set(value); }
    public void setUsername(String value) { username.set(value); }
    public void setScore(int value) { score.set(value); }
}