/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.server.usecase;

import java.util.List;
import src.client.data.dto.User;
import src.model.Question;
import src.server.database_connection.ServerDBConnection;

public class ServerUsecase implements onActionServer {
    ServerDBConnection serverDBConnection = new ServerDBConnection();
    
    @Override
    public User onLogin(String username, String password) {
        return serverDBConnection.doLoginRequest(username, password);
    }

    @Override
    public boolean onSigningUp(String fullname, String username, String password) {
        return serverDBConnection.doRegisterRequest(fullname, username, password);
    }

    @Override
    public List<User> getListUser() {
        return serverDBConnection.getAllUser();
    }

    @Override
    public List<Question> getQuestionByPack(int packOrder) {
        return serverDBConnection.getQuestions(packOrder);
    }

    @Override
    public void updateUserScore(String username, double scoreBonus) {
        serverDBConnection.updateUserScore(username,(int) scoreBonus);
    }
}
