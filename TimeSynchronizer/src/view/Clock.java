package view;

import controller.Controller;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Interface para a exibiçao do relógio.
 * @author Santana
 */
public class Clock extends javax.swing.JFrame {

    private final Controller controller = Controller.getInstance();

    private final String id = JOptionPane.showInputDialog(null, "Nome da Máquina");
    private final String horaDefinida = JOptionPane.showInputDialog(null, "Digite a hora");
    private final String drift = JOptionPane.showInputDialog(null, "Digite o drift desejado");
    private int hora;
    private int min;
    private int seg;

    /**
     * Creates new form Clock
     */
    public Clock() {
        initComponents();
        inicializarValores();
        contar();
        sincronizar();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clock = new javax.swing.JLabel();
        idvis = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TimeSynchronizer");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                sairDaLista(evt);
            }
        });

        clock.setFont(new java.awt.Font("Cantarell", 0, 48)); // NOI18N
        clock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        idvis.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        idvis.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        idvis.setText("f");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idvis, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clock, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(clock, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idvis, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        idvis.getAccessibleContext().setAccessibleName("");
        idvis.getAccessibleContext().setAccessibleParent(idvis);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sairDaLista(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_sairDaLista
//        this.controller.sair(id);
        System.exit(0);
    }//GEN-LAST:event_sairDaLista

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Clock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Clock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Clock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Clock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Clock().setVisible(true);
            }
        });
    }

    /**
     * Thread que a realiza a contagem do cronômetro.
     */
    
    public void contar() {
        new Thread() {
            @Override
            public void run() {

                String hr = "";
                String min2 = "";
                String seg2 = "";

                double minhaContagem = Double.parseDouble(drift);

                while (true) {
                    try {
                        long novo = (long) (1000 * minhaContagem);
                        Thread.sleep(novo);
                        seg = seg + 1;
                        if (seg > 59) {
                            seg = 0;
                            min = min + 1;
                            if (min > 59) {
                                min = 0;
                                hora = hora + 1;
                                if (hora > 23) {
                                    hora = 0;
                                }
                            }
                        }

                        hr = hora <= 9 ? "0" + hora : hora + "";
                        min2 = min <= 9 ? "0" + min : min + "";
                        seg2 = seg <= 9 ? "0" + seg : seg + "";

                        setLabel(hr + ":" + min2 + ":" + seg2);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }.start();
    }
    
    /**
     * Thread que realiza a sincronização com os outros relógios.
     */
    
    public void sincronizar() {
        new Thread() {
            @Override
            public void run() {

                String hr = "";
                String min2 = "";
                String seg2 = "";

                while (true) {
                    try {
                        Thread.sleep(5*1000);
                        int horatemp = controller.getHora();
                        int mintemp = controller.getMin();
                        int segtemp = controller.getSeg();
                        int resulthora = horatemp - hora;
                        int resultmin = mintemp - min;
                        int resultseg = segtemp - seg;

                        if(controller.getCoordenador().equals(id)){
                            controller.vencedorEeicao(id);
                            if(controller.getMaxRTT()/1000 > 1){
                                seg = seg + 1;
                                controller.enviarHora(hora, min, seg);
                            }
                            else{
                            controller.enviarHora(hora, min, seg);
                        }
                            
                            
                        } else{
                            if(resulthora < 0 || (resulthora == 0 && resultmin < 0) || (resulthora == 0 && resultmin == 0 && resultseg < 0)){
                            controller.vencedorEeicao(id);
                            controller.enviarHora(hora, min, seg);
                        } else{
                            hora = horatemp;
                            min = mintemp;
                            seg = segtemp; 
                            }
                        }
 
//                        if () {
//                            
//                        } else{
//                            
//                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        }.start();
    }

    private void setLabel(String texto) {
        clock.setText(texto);
    }

    private void inicializarValores() {
        String[] inf = horaDefinida.split(":");
        this.hora = Integer.parseInt(inf[0]);
        this.min = Integer.parseInt(inf[1]);
        this.seg = Integer.parseInt(inf[2]);

        idvis.setText(id);
        clock.setText(horaDefinida);
        controller.setID(id);
        
//        if(controller.getCoordenador().equals("")){
//                controller.realizarEleicao(id, hora, min, seg);
//        }
        
//        int resulthora = controller.getHora() - hora;
//        int resultmin = controller.getMin() - min;
//        int resultseg = controller.getSeg() - seg;
//
//        if(resulthora < 0 || (resulthora == 0 && resultmin < 0) || (resulthora == 0 && resultmin == 0 && resultseg < 0)){
//            controller.realizarEleicao(id, hora, min, seg);
//        } else{
//            hora = controller.getHora();
//            min = controller.getMin();
//            seg = controller.getSeg();
//            
//            
//        }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clock;
    private javax.swing.JLabel idvis;
    // End of variables declaration//GEN-END:variables
}
