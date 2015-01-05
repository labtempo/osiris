/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.interfaces;

import java.io.Serializable;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Teste implements Serializable {

    private String name;

    public Teste() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeste() {
        return this.name;
    }
}
