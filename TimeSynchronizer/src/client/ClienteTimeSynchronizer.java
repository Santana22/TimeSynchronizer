package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;

/**
 * Esta classe é abstração para a comunicação entre os dispositivos para
 * sincronização.
 *
 * @author Santana
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
    private static long maxRTT = 0;
    private static boolean atualizarHora = false;
    private static boolean executarEleicao = true;
    private static boolean responderamEleicao = false;

    /**
     * Esta contrutor da Classe.
     *
     * @author Santana
     */
    public ClienteTimeSynchronizer() {
        try {
            endereco = InetAddress.getByName("235.0.0.1");
            conexao = new MulticastSocket(porta);
            conexao.joinGroup(endereco);
            new ThreadCliente(conexao).start();
        } catch (Exception ex) {
        }
    }

    /**
     * Método que é responsável por enviar a hora em uma eleição.
     *
     * @param id identificação de quem enviou
     * @param hora horas enviadas
     * @param min minutos enviados
     * @param seg segundos enviados
     */
    public void enviarHoraEleicao(String id, int hora, int min, int seg) {
        byte dados[] = ("1001" + ";" + id + ";" + hora + ";" + min + ";" + seg).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    /**
     * Método que é responsável por enviar a hora.
     *
     * @param hora horas enviadas
     * @param min minutos enviados
     * @param seg segundos enviados
     */
    public void enviarHora(int hora, int min, int seg) {
        byte dados[] = ("1002" + ";" + hora + ";" + min + ";" + seg).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    /**
     * Método que é responsável por convoar uma eleição.
     *
     * @param id identificação de quem convocou
     * @param hora horas enviadas
     * @param min minutos enviados
     * @param seg segundos enviados
     */
    public void realizarEleicao(String id, int hora, int min, int seg) {
        ClienteTimeSynchronizer.responderamEleicao = false;
        System.out.println("Pedido de Eleição de " + id);
        byte dados[] = ("1003" + ";" + id + ";" + hora + ";" + min + ";" + seg).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    /**
     * Método que é responsável por responder a uma convocação de eleição.
     *
     * @param id1 identificação de quem convocou uma eleição.
     * @param id2 identificaçao de quem respondeu.
     */
    public void responderEleicao(String id1, String id2) {
        System.out.println(id2 + " respondeu " + id1);
        byte dados[] = ("1004" + ";" + id1 + ";" + id2).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);
        } catch (IOException ex) {
        }
    }

    /**
     * Método que é responsável por enviar o vencedor de uma eleição.
     *
     * @param id identificação de quem venceu
     */
    public void vencedorEleicao(String id) {
        
        System.out.println(id + " é o Coordenador");
        byte dados[] = ("1005" + ";" + id).getBytes();
        DatagramPacket msgPacket = new DatagramPacket(dados, dados.length, endereco, porta);
        try {
            conexao.send(msgPacket);     
            
        } catch (IOException ex) {
        }
    }

    /**
     * Método que é modifica o id.
     *
     * @param id identificação do dispositivo.
     */
    public void setID(String id) {
        ClienteTimeSynchronizer.id = id;
    }

    /**
     * Método que retorna o id.
     */
    public String getID() {
        return ClienteTimeSynchronizer.id;
    }

    /**
     * Método que retorna as horas.
     */
    public int getHora() {
        return ClienteTimeSynchronizer.hora;
    }

    /**
     * Método que retorna os minutos.
     */
    public int getMin() {
        return ClienteTimeSynchronizer.min;
    }

    /**
     * Método que retorna os segundos.
     */
    public int getSeg() {
        return ClienteTimeSynchronizer.seg;
    }

    /**
     * Método que é responsável por modificar as horas.
     *
     * @param valor nova hora
     */
    public void setHora(int valor) {
        ClienteTimeSynchronizer.hora = valor;
    }

    /**
     * Método que é responsável por modificar os minutos.
     *
     * @param valor novos minutos
     */
    public void setMin(int valor) {
        ClienteTimeSynchronizer.min = valor;
    }

    /**
     * Método que é responsável por modificar os segundos.
     *
     * @param valor novos segundos
     */
    public void setSeg(int valor) {
        ClienteTimeSynchronizer.seg = valor;
    }

    /**
     * Método que é responsável por retorna o coordenador.
     */
    public String getCoordenador() {
        return ClienteTimeSynchronizer.coordenador;
    }

    /**
     * Método que é responsável por modificar o coordenador.
     *
     * @param novoCoordenador novo coordenador
     */
    public void setCoordenador(String novoCoordenador) {
        ClienteTimeSynchronizer.coordenador = novoCoordenador;
    }
    
    public long getMaxRTT(){
        System.out.println("RTT máximo: "+maxRTT + " ms");
        return ClienteTimeSynchronizer.maxRTT;
    }

    /**
     * Classe interna responsavel por identificar os pacotes na rede.
     */
    private class ThreadCliente extends Thread {

        private final MulticastSocket minhaConexaoMulticast;

        public ThreadCliente(MulticastSocket conexao) {
            this.minhaConexaoMulticast = conexao;
        }

        /**
         * Método de execução para obter os pacotes.
         */
        @Override
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

                        System.out.println("Atualização pelo Coordenador " + ClienteTimeSynchronizer.coordenador);
                        
                        ClienteTimeSynchronizer.hora = Integer.parseInt(dadosRecebidos[1].trim());
                        ClienteTimeSynchronizer.min = Integer.parseInt(dadosRecebidos[2].trim());
                        ClienteTimeSynchronizer.seg = Integer.parseInt(dadosRecebidos[3].trim());
                        
                        
                    } else if (msg.startsWith("1003")) {
                        String[] dadosRecebidos = msg.split(";");
                        int resulthora = Integer.parseInt(dadosRecebidos[2].trim()) - ClienteTimeSynchronizer.hora;
                        int resultmin = Integer.parseInt(dadosRecebidos[3].trim()) - ClienteTimeSynchronizer.min;
                        int resultseg = Integer.parseInt(dadosRecebidos[4].trim()) - ClienteTimeSynchronizer.seg;

                        if (!dadosRecebidos[1].trim().equals(id)) {
                            if (resulthora < 0 && (resulthora == 0 || resultmin < 0) && (resulthora == 0 || resultmin == 0 || resultseg < 0)) {
                                responderEleicao(dadosRecebidos[1].trim(), id);
                                realizarEleicao(id, hora, min, seg);
                            }
                        } else {
                            if (ClienteTimeSynchronizer.responderamEleicao == false) {
                                vencedorEleicao(id);
                            }
                        }

                    } else if (msg.startsWith("1004")) {
                        String[] dadosRecebidos = msg.split(";");

                        if (dadosRecebidos[1].trim().equals(id)) {
                            ClienteTimeSynchronizer.responderamEleicao = true;
                        }

                    } else if (msg.startsWith("1005")) {
                        String[] dadosRecebidos = msg.split(";");
                        ClienteTimeSynchronizer.coordenador = dadosRecebidos[1].trim();
                        Random rd = new Random();
                        
                        maxRTT = 100 + rd.nextInt(1000);
                    }

                    Thread.sleep(100);
                }
            } catch (Exception exc) {

            }
        }
    }
}
