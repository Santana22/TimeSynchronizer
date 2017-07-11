package controller;

import client.ClienteTimeSynchronizer;

/**
 * Classe que intermedia a interface e a comunicação de rede.
 * @author Santana
 */
public class Controller {
    private static Controller INSTANCE = null;
    private ClienteTimeSynchronizer cliente = new ClienteTimeSynchronizer();  
    
   /**
    * Método para obter o objeto. 
    */
    
    public static Controller getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Controller();
        }
        return INSTANCE;
    }
    
    /**
     * Método que é responsável por enviar a hora.
     *
     * @param hora horas enviadas
     * @param min minutos enviados
     * @param seg segundos enviados
     */

    public void enviarHora(int hora, int min, int seg) {
        this.cliente.enviarHora(hora, min, seg);
    }

    /**
     * Método que retorna as horas.
     */
    public int getHora() {
        return cliente.getHora();
    }
    
    /**
     * Método que retorna os minutos.
     */

    public int getMin() {
        return cliente.getMin();
    }

    /**
     * Método que retorna os segundos.
     */
    
    public int getSeg() {
        return cliente.getSeg();
    }
    
    /**
     * Método que é responsável por modificar os minutos.
     *
     * @param valor novos minutos
     */
    
    public void setMin(int valor){
        this.cliente.setMin(valor);
    }
    /**
     * Método que é responsável por modificar as horas.
     *
     * @param valor nova hora
     */
    
    public void setHora(int valor){
        this.cliente.setHora(valor);
    }
    
    /**
     * Método que é responsável por modificar os segundos.
     *
     * @param valor novos segundos
     */
    
    public void setSeg(int valor){
        this.cliente.setSeg(valor);
    }
    
    /**
     * Método que é responsável por retorna o coordenador.
     */
    
    public String getCoordenador(){
        return this.cliente.getCoordenador();
    }
    
    /**
     * Método que é responsável por modificar o coordenador.
     *
     * @param novoCoordenador novo coordenador
     */
    
    public void setCoordenador(String novoCoordenador){
        this.cliente.setCoordenador(novoCoordenador);
    }
    
    /**
     * Método que é modifica o id.
     *
     * @param id identificação do dispositivo.
     */
    
    public void setID(String id){
        this.cliente.setID(id);
    }
    
    /**
     * Método que retorna o id.
     */
    
    public String getID(){
        return this.cliente.getID();
    }
    
     /**
     * Método que é responsável por convoar uma eleição.
     *
     * @param id identificação de quem convocou
     * @param hora horas enviadas
     * @param min minutos enviados
     * @param seg segundos enviados
     */
    
    public void realizarEleicao(String id, int hora, int min, int seg){
        this.cliente.realizarEleicao(id, hora, min, seg);
    }
    
    /**
     * Método que é responsável por enviar o vencedor de uma eleição.
     *
     * @param id identificação de quem venceu
     */
    
    public void vencedorEeicao(String id){
        this.cliente.vencedorEleicao(id);
    }
    
    public long getMaxRTT(){
        return this.cliente.getMaxRTT();
    }
}
