/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import thread.ThreadCliente;

/**
 *
 * @author vinicius
 */
public class ClienteTimeSynchronizer {

    private int porta = 22222;
    private InetAddress endereco;
    private MulticastSocket conexao;

    public ClienteTimeSynchronizer() {
        try {
            endereco = InetAddress.getByName("235.0.0.1");
            conexao = new MulticastSocket(porta);
            conexao.joinGroup(endereco);
            new ThreadCliente(conexao).start();
        } catch (Exception ex) {
        }
    }

}
