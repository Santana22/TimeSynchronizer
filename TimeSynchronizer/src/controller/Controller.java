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
    private static Controller INSTANCE = null;
    private ClienteTimeSynchronizer cliente = new ClienteTimeSynchronizer();  
    
    public static Controller getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Controller();
        }
        return INSTANCE;
    }

    public void enviarHora(int hora, int min, int seg) {
        this.cliente.enviarHora(hora, min, seg);
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
    
    public synchronized String getCoordenador(){
        return this.cliente.getCoordenador();
    }
    
    public synchronized void setCoordenador(String novoCoordenador){
        this.cliente.setCoordenador(novoCoordenador);
    }
    
    public void setID(String id){
        this.cliente.setID(id);
    }
    
    public String getID(){
        return this.cliente.getID();
    }
    
    public boolean executarEleicao(){
        return this.cliente.getExecutarEleicao();
    }
    
    public void verificarCoordenador(String id){
        this.cliente.verificarCoordenador(id);
    }
    
    public void realizarEleicao(String id, int hora, int min, int seg){
        this.cliente.realizarEleicao(id, hora, min, seg);
    }
}
