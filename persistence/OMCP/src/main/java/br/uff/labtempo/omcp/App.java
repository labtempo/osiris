package br.uff.labtempo.omcp;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws URISyntaxException {
        //TODO: encontrar meio de definir id e queries(paramentros)
        URI uri = new URI("omcp://sensornet/resource/50?param=valor&param2=valor2");

        System.out.println(uri.getHost());// sensornet

        System.out.println(uri.getPath());// /resource/id
        System.out.println(uri.getRawPath());// /resource/id

        System.out.println(uri.getFragment());// null
        System.out.println(uri.getRawFragment());// null

        System.out.println(uri.getAuthority());// sensornet
        System.out.println(uri.getRawAuthority());// sensornet

        System.out.println(uri.getQuery());// param=valor;param2=valor2
        System.out.println(uri.getRawQuery());// null

        System.out.println(uri.getScheme());// omcp
        System.out.println(uri.getSchemeSpecificPart());// omcp
        System.out.println(uri.getRawSchemeSpecificPart());// //sensornet/resource/id?param=valor;param2=valor2

        System.out.println(uri.getPort());// -1
        
        System.out.println(uri.getUserInfo());// null
        System.out.println(uri.getRawUserInfo());// null

    }

}

/**
 * Apenas o protocolo Pacotes - request e response Métodos - get, put, post,
 * delete e notify
 *
 */
