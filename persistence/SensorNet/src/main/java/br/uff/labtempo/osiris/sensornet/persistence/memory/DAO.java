/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.sensornet.persistence.memory;

import java.util.List;

/**
 *
 * @author Felipe
 */
public interface DAO<T> {
    List<T> getAll(Object...args);
    T getById(String id, Object...args);
    boolean insert(T object, Object...args);
    boolean update(T objectA, T objectB);
}
