
package src.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import src.client.data.dto.User;
import src.server.database_connection.ServerDBConnection;

public class Server {
    public static ConcurrentHashMap<String, ClientHandler> listClientConnection = new ConcurrentHashMap<>();
}
