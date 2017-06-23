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

    public void cadastrar(String nome) {
        this.cliente.cadastrar(nome);
    }

    public void enviarHora(int hora, int min, int seg) {
        this.cliente.enviarHora(hora, min, seg);
    }

    public void elegerCoordenador() {
        this.cliente.elegerCoordenador();
    }

    public boolean getAtualizarHora() {
        return this.cliente.getAtualizarHora();
    }

    public void setAtualizarHora(boolean valor) {
        this.cliente.setAtualizarHora(valor);
    }

    public synchronized int getHora() {
        return cliente.getHora();

    }

    public synchronized int getMin() {
        return cliente.getMin();

    }

    public synchronized int getSeg() {
        return cliente.getSeg();

    }
    
    public synchronized void setMin(int valor){
        this.cliente.setMin(valor);
    }
    
    public synchronized void setHora(int valor){
        this.cliente.setHora(valor);
    }
    
    public synchronized void setSeg(int valor){
        this.cliente.setSeg(valor);
    }
}
