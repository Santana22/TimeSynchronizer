/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

/**
 *
 * @author vinicius
 */
public class ClienteTimeSynchronizer {

    private int porta = 22222;
    private InetAddress endereco;
    private MulticastSocket conexao;
    private static int hora;
    private static int min;
    private static int seg;
    private static boolean atualizarHora = false;
    private static ArrayList<String> listaNomes = new ArrayList();

    public ClienteTimeSynchronizer() {
        try {
            endereco = InetAddress.getByName("235.0.0.1");
            conexao = new MulticastSocket(porta);
            conexao.joinGroup(endereco);
            new ThreadCliente(conexao).start();
        } catch (Exception ex) {
        }
    }

    public synchronized void cadastrar(String nome) {
        byte dados[] = ("1001" + ";" + nome).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    public synchronized void elegerCoordenador() {
    }

    public synchronized void enviarHora(int hora, int min, int seg) {

        byte dados[] = ("1002" + ";" + hora + ";" + min + ";" + seg).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    public synchronized boolean getAtualizarHora() {
        return ClienteTimeSynchronizer.atualizarHora;
    }

    public synchronized void setAtualizarHora(boolean valor) {
        ClienteTimeSynchronizer.atualizarHora = valor;
        
    }

    public synchronized int getHora() {
        return ClienteTimeSynchronizer.hora;
    }

    public synchronized int getMin() {
        return ClienteTimeSynchronizer.min;
    }

    public synchronized int getSeg() {
        return ClienteTimeSynchronizer.seg;
    }

    public synchronized void setHora(int valor) {
        ClienteTimeSynchronizer.hora = valor;
    }

    public synchronized void setMin(int valor) {
        ClienteTimeSynchronizer.min = valor;
    }

    public synchronized void setSeg(int valor) {
        ClienteTimeSynchronizer.seg = valor;
    }

    private class ThreadCliente extends Thread {

        private final MulticastSocket minhaConexaoMulticast;

        public ThreadCliente(MulticastSocket conexao) {
            this.minhaConexaoMulticast = conexao;
        }

        public void run() {
            try {

                while (true) {
                    byte dados[] = new byte[1024];
                    DatagramPacket datagrama = new DatagramPacket(dados, dados.length);
                    minhaConexaoMulticast.receive(datagrama);
                    String msg = new String(datagrama.getData());

                    if (msg.startsWith("1001")) {

                        String[] dadosRecebidos = msg.split(";");

                        if (!listaNomes.contains(dadosRecebidos[1].trim())) {
                            listaNomes.add(dadosRecebidos[1].trim());
                        }
                    } else if (msg.startsWith("1002")) {

                        String[] dadosRecebidos = msg.split(";");

                        int hora = Integer.parseInt(dadosRecebidos[1].trim());
                        int min = Integer.parseInt(dadosRecebidos[2].trim());
                        int seg = Integer.parseInt(dadosRecebidos[3].trim());

                        if ((hora > ClienteTimeSynchronizer.hora && min > ClienteTimeSynchronizer.min && seg > ClienteTimeSynchronizer.seg)
                                || (hora == ClienteTimeSynchronizer.hora && min > ClienteTimeSynchronizer.min)
                                || (hora == ClienteTimeSynchronizer.hora && min == ClienteTimeSynchronizer.min && seg > ClienteTimeSynchronizer.seg)
                                || (hora > ClienteTimeSynchronizer.hora && min < ClienteTimeSynchronizer.min && seg < ClienteTimeSynchronizer.seg)
                                || (hora > ClienteTimeSynchronizer.hora && min < ClienteTimeSynchronizer.min && seg > ClienteTimeSynchronizer.seg)
                                || hora > ClienteTimeSynchronizer.hora) {
                            ClienteTimeSynchronizer.hora = hora;
                            ClienteTimeSynchronizer.min = min;
                            ClienteTimeSynchronizer.seg = seg;
                            ClienteTimeSynchronizer.atualizarHora = true;
                        }

                    }

                    Thread.sleep(500);
                }

            } catch (Exception exc) {

            }
        }
    }

}
