/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence;

import java.util.List;

/**
 *
 * @author Felipe
 */
public interface NetworkDao<T> extends Dao<T> {

    public T get(T o);

    public T get(String networkId);

    public List<T> getAll();
}
