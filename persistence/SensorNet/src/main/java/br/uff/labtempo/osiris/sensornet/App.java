package br.uff.labtempo.osiris.sensornet;

import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.sensornet.controller.EventController;
import br.uff.labtempo.osiris.sensornet.persistence.memory.SensorDao;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    private static Server server;

    public static void main(String[] args) throws Exception {        
        server = new Server();
        server.start();        
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
}
