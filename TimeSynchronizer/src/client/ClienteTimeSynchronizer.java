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
    private static String coordenador = "";
    private static int horacoordenador;
    private static int mincoordenador;
    private static int segcoordenador;
    private static int horapublica;
    private static int minpublica;
    private static int segpublica;
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

    public synchronized void enviarHora(int hora, int min, int seg) {

        byte dados[] = ("1002" + ";" + hora + ";" + min + ";" + seg).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    public synchronized void elegerCoordenador() {
    }

    public synchronized boolean getAtualizarHora() {
        return ClienteTimeSynchronizer.atualizarHora;
    }

    public synchronized void setAtualizarHora(boolean valor) {
        ClienteTimeSynchronizer.atualizarHora = valor;
    }

    public synchronized int getHora() {
        return ClienteTimeSynchronizer.horapublica;
    }

    public synchronized int getMin() {
        return ClienteTimeSynchronizer.minpublica;
    }

    public synchronized int getSeg() {
        return ClienteTimeSynchronizer.segpublica;
    }

    public synchronized void setHora(int valor) {
        ClienteTimeSynchronizer.horapublica = valor;
    }

    public synchronized void setMin(int valor) {
        ClienteTimeSynchronizer.minpublica = valor;
    }

    public synchronized void setSeg(int valor) {
        ClienteTimeSynchronizer.segpublica = valor;
    }

    public synchronized String getCoordenador() {
        return ClienteTimeSynchronizer.coordenador;
    }

    public synchronized void setCoordenador(String novoCoordenador) {
        ClienteTimeSynchronizer.coordenador = novoCoordenador;
    }
    
    public synchronized ArrayList<String> getLista(){
        return ClienteTimeSynchronizer.listaNomes;
    }

    public synchronized int getHoraCoordenador(){
        return ClienteTimeSynchronizer.horacoordenador;
    }
    
    public synchronized int getMinCoordenador(){
        return ClienteTimeSynchronizer.mincoordenador;
    }
    
    public synchronized int getSegCoordenador(){
        return ClienteTimeSynchronizer.segcoordenador;
    }
    
    public synchronized void setHoraCoordenador(int novaHora){
        ClienteTimeSynchronizer.horacoordenador = novaHora;
    }
    
    public synchronized void setMinCoordenador(int novoMin){
        ClienteTimeSynchronizer.horacoordenador = novoMin;
    }
    
    public synchronized void setSegCoordenador(int novoSeg){
        ClienteTimeSynchronizer.horacoordenador = novoSeg;
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

                        if ((hora > ClienteTimeSynchronizer.horapublica && min > ClienteTimeSynchronizer.minpublica && seg > ClienteTimeSynchronizer.segpublica)
                                || (hora == ClienteTimeSynchronizer.horapublica && min > ClienteTimeSynchronizer.minpublica)
                                || (hora == ClienteTimeSynchronizer.horapublica && min == ClienteTimeSynchronizer.minpublica && seg > ClienteTimeSynchronizer.segpublica)
                                || (hora > ClienteTimeSynchronizer.horapublica && min < ClienteTimeSynchronizer.minpublica && seg < ClienteTimeSynchronizer.segpublica)
                                || (hora > ClienteTimeSynchronizer.horapublica && min < ClienteTimeSynchronizer.minpublica && seg > ClienteTimeSynchronizer.segpublica)
                                || hora > ClienteTimeSynchronizer.horapublica) {
                            ClienteTimeSynchronizer.horapublica = hora;
                            ClienteTimeSynchronizer.minpublica = min;
                            ClienteTimeSynchronizer.segpublica = seg;
                            ClienteTimeSynchronizer.atualizarHora = true;
                        }
                    }

                    Thread.sleep(100);
                }

            } catch (Exception exc) {

            }
        }
    }
}
