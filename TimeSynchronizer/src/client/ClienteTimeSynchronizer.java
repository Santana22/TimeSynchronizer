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

/**
 *
 * @author vinicius
 */
public class ClienteTimeSynchronizer {

    private int porta = 22222;
    private InetAddress endereco;
    private MulticastSocket conexao;
    private static String coordenador = "";
    private static String id = "";
    private static int hora;
    private static int min;
    private static int seg;
    private static boolean atualizarHora = false;
    private static boolean executarEleicao = true;
    private static boolean responderamEleicao = false;

    public ClienteTimeSynchronizer() {
        try {
            endereco = InetAddress.getByName("235.0.0.1");
            conexao = new MulticastSocket(porta);
            conexao.joinGroup(endereco);
            new ThreadCliente(conexao).start();
        } catch (Exception ex) {
        }
    }

    public void enviarHoraEleicao(String id, int hora, int min, int seg) {
        byte dados[] = ("1001" + ";" + id + ";" + hora + ";" + min + ";" + seg).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    public void enviarHora(int hora, int min, int seg) {
        byte dados[] = ("1002" + ";" + hora + ";" + min + ";" + seg).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    public void realizarEleicao(String id, int hora, int min, int seg) {
        ClienteTimeSynchronizer.responderamEleicao = false;
        byte dados[] = ("1003" + ";" + id + ";" + hora + ";" + min + ";" + seg).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    public void responderEleicao(String id1, String id2) {
        byte dados[] = ("1004;"+id1+";"+id2).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    public void vencedorEleicao(String id) {
        byte dados[] = ("1005" + ";" + id).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    public synchronized void verificarCoordenador(String coordenador) {
        byte dados[] = ("1006;" + coordenador).getBytes();
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

    public void setID(String id) {
        ClienteTimeSynchronizer.id = id;
    }

    public String getID() {
        return ClienteTimeSynchronizer.id;
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

    public synchronized String getCoordenador() {
        System.out.println(ClienteTimeSynchronizer.coordenador);
        return ClienteTimeSynchronizer.coordenador;
        
    }

    public synchronized void setCoordenador(String novoCoordenador) {
        ClienteTimeSynchronizer.coordenador = novoCoordenador;
//        System.out.println(ClienteTimeSynchronizer.coordenador);
    }

    public synchronized boolean getExecutarEleicao() {
        return ClienteTimeSynchronizer.executarEleicao;
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

                    } else if (msg.startsWith("1002")) {
                        String[] dadosRecebidos = msg.split(";");

                        ClienteTimeSynchronizer.hora = Integer.parseInt(dadosRecebidos[1].trim());
                        ClienteTimeSynchronizer.min = Integer.parseInt(dadosRecebidos[2].trim());
                        ClienteTimeSynchronizer.seg = Integer.parseInt(dadosRecebidos[3].trim());

                    } else if (msg.startsWith("1003")) {
                        String[] dadosRecebidos = msg.split(";");
                        int resulthora = Integer.parseInt(dadosRecebidos[2].trim()) - ClienteTimeSynchronizer.hora;
                        int resultmin = Integer.parseInt(dadosRecebidos[3].trim()) - ClienteTimeSynchronizer.min;
                        int resultseg = Integer.parseInt(dadosRecebidos[4].trim()) - ClienteTimeSynchronizer.seg;

                        if (dadosRecebidos[1].trim().equals(id)) {
                            if(resulthora < 0 || (resulthora == 0 && resultmin < 0) || (resulthora == 0 && resultmin == 0 && resultseg < 0)){
                                responderEleicao(dadosRecebidos[1].trim(), id);
                                realizarEleicao(id, hora, min, seg);
                            }
                            responderEleicao(dadosRecebidos[1].trim(), id);
                        } 

                    } else if (msg.startsWith("1004")) {
                        String[] dadosRecebidos = msg.split(";");
                        
                        if(dadosRecebidos[1].trim().equals(dadosRecebidos[2].trim()) && ClienteTimeSynchronizer.responderamEleicao == false){
                            vencedorEleicao(id);
                        }else if(dadosRecebidos[1].trim().equals(id)){
                            ClienteTimeSynchronizer.responderamEleicao = true;
                        }
                        
   
                    } else if (msg.startsWith("1005")) {
                        String[] dadosRecebidos = msg.split(";");
                        ClienteTimeSynchronizer.coordenador = dadosRecebidos[1].trim();

                    } else if (msg.startsWith("1006")) {
                        String[] dadosRecebidos = msg.split(";");

                        if (dadosRecebidos[1].trim().equals(id)) {
                            byte dados2[] = ("1007").getBytes();
                            DatagramPacket msgPacket = new DatagramPacket(dados2, dados2.length, endereco, porta);
                            try {
                                conexao.send(msgPacket);
                            } catch (IOException ex) {
                            }
                        }
                    } else if (msg.startsWith("1007")) {
                        ClienteTimeSynchronizer.executarEleicao = false;
                    }

                    Thread.sleep(100);
                }
            } catch (Exception exc) {

            }
        }
    }
}
