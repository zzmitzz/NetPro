/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client.common;

/**
 *
 * @author 1
 */
public interface onAction {
    void onSuccess(String data);
    void onFail();
    void onError(String error);
}
