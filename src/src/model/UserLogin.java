/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.model;

import com.mysql.cj.callback.UsernameCallback;

/**
 *
 * @author 1
 */
public class UserLogin {

    public String username;
    public String password;

    UserLogin(String a, String b) {
        this.username = a;
        this.password = b;
    }
}
