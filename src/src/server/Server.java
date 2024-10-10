
package src.server;

import src.client.data.dto.User;
import src.server.database_connection.ServerDBConnection;

public class Server {
    
    ServerDBConnection serverDBConnection = new ServerDBConnection();
    
    public User doLogin(String username, String password){
        return serverDBConnection.doLoginRequest(username, password);
    }
}
