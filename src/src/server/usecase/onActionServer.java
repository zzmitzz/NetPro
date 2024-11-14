/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.server.usecase;

import java.util.List;
import src.client.data.dto.User;
import src.model.Question;

/**
 *
 * @author 1
 */
public interface onActionServer {
    public User onLogin(String username, String password) ;
    public boolean onSigningUp(String fullname, String username, String password);
    public List<User> getListUser();
    List<Question> getQuestionByPack(int packOrder);
    void updateUserScore(String username, double scoreBonus);
}
