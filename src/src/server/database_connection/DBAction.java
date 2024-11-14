
package src.server.database_connection;

import java.util.ArrayList;
import src.client.data.dto.User;

public interface DBAction {
    ArrayList<User> getAllUser();
    User doLoginRequest(String username, String password); // Could be null in case no user satisfy
    boolean doRegisterRequest(String fullname, String username, String password);
    void updateUserScore(String username, int score);
}
