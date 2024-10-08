/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

/**
 *
 * @author 1
 */
public class Application {
    public static void main(String[] args) {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.initScreen( new WelcomeScreen.ScreenCallBack(){
            @Override
            public void onServerClick() {
                
            }

            @Override
            public void onClientClick() {
            }
           
        });
    }
}
