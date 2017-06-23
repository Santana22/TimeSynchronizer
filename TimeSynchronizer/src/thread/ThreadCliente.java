/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import java.net.DatagramPacket;
import java.net.MulticastSocket;


public class ThreadCliente extends Thread{

    private final MulticastSocket minhaConexaoMulticast;
    
    public ThreadCliente(MulticastSocket conexao) {
     this.minhaConexaoMulticast = conexao;
    }
    
    
    public void run(){
        try{
            
            while (true){
                byte dados[] = new byte[1024];
                DatagramPacket datagrama = new DatagramPacket(dados, dados.length);
                minhaConexaoMulticast.receive(datagrama);
                String msg = new String(datagrama.getData());
                
                if(msg.startsWith(msg)){
                    
                }
                
            }
            
        } catch (Exception exc){
            
        }
    }
}
