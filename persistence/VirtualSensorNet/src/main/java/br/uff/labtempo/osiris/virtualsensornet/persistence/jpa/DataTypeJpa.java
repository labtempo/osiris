/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;

/**
 *
 * @author Felipe
 */
public class DataTypeJpa implements DataTypeDao {

    private final DataManager data;

    public DataTypeJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public DataType get(long dataTypeId) {
        return data.get(DataType.class, dataTypeId);
    }

    @Override
    public void save(DataType o) {
        data.save(o);
    }

    @Override
    public void update(DataType o) {
        data.update(o);
    }

    @Override
    public void delete(DataType o) {
        data.delete(o);
    }

}
