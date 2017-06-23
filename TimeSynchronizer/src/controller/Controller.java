/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import client.ClienteTimeSynchronizer;

/**
 *
 * @author vinicius
 */

public class Controller {
    
    private ClienteTimeSynchronizer cliente = new ClienteTimeSynchronizer();
    
    private Controller() {
    }
    
    public static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }
    
    private static class ControllerHolder {
        private static final Controller INSTANCE = new Controller();
    }
    
    
}
