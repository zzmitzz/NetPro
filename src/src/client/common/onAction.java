/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client.common;

import java.io.IOException;

/**
 *
 * @author 1
 */

/**
 * The onAction interface defines the contract for handling different outcomes of an action.
 */
public interface onAction {

    /**
     * Called when the action is successful.
     *
     * @param data the result data of the successful action
     * @throws IOException if an I/O error occurs
     */
    void onSuccess(String data) throws IOException;

    /**
     * Called when the action fails.
     */
    void onFail();

    /**
     * Called when an error occurs during the action.
     *
     * @param error the error message
     */
    void onError(String error);
}
