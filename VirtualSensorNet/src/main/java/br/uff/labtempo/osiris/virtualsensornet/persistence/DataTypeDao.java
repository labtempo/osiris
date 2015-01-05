/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence;

import br.uff.labtempo.osiris.virtualsensornet.model.DataType;

/**
 *
 * @author Felipe
 */
public interface DataTypeDao extends Dao<DataType>{
    public DataType get(long id);
}
