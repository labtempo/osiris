package br.uff.labtempo.osiris.sensornet;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Map<String, String> map1 = new HashMap<>();
        Map<String, String> map2 = new HashMap<>();

        map1.put("chave1", "valor1");
        map1.put("chave2", "valor2");
        map1.put("chave3", "valor3");
        map2.put("chave2", "valor2");
        map2.put("chave3", "valor3");
        map2.put("chave1", "valor1");

        System.out.println(map1.equals(map2));
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
