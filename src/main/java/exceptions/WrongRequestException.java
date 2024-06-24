/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author maria
 */
public class WrongRequestException extends Exception {

    public WrongRequestException(String message) {
        super(message);
    }
}
