/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence;

/**
 *
 * @author Felipe
 */
interface Dao<T> {
    public void save(T o);

    public void update(T o);

    public void delete(T o);
}
