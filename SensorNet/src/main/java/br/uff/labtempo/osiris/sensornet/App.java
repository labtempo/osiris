package br.uff.labtempo.osiris.sensornet;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    private static Bootstrap boot;

    public static void main(String[] args) throws Exception {
        shutdownHook();
        boot = new Bootstrap();
        boot.start();
    }

    /**
     * Eventos
     *
     * created * nova rede * novo sensor na rede * rede reativada(retornando do
     * estado inativo) * sensor reativado(retornando do estado de inativo)
     *
     * modified * rede atualizada(metadados) * sensor atualizado(datos +
     * metadados) * sensor bateria baixa(extra - regra de negócio do sensornet)
     *
     * deleted * sensor inativo(estrapolou o intervalo de captura) * rede
     * inativa(sem nenhum sensor ativo)
     *
     */
    /**
     * Estados dos sensores
     *
     * * novo * atualizado * inativo * reativado
     *
     * novo -> atualizado -> inativo -> reativado -> atualizado novo -> inativo
     * -> reativado -> atualizado
     *
     */
    private static void shutdownHook() {
        System.out.println("Control + C to terminate");
        final Thread thread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("ShutdownHook is running...");
                thread.setName("Shutdown hook");
                try {
                    boot.close();
                    thread.join();
                } catch (Exception ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("ShutdownHook end");
            }
        });
    }
}
