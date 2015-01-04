/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.virtualsensornet.model.Converter;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;

/**
 *
 * @author Felipe
 */
class ConverterJpa implements ConverterDao {

    private final DataManager data;

    public ConverterJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Converter get(long id) {
        return data.get(Converter.class, id);
    }

    @Override
    public void save(Converter o) {
        data.save(o);
    }

    @Override
    public void update(Converter o) {
        data.update(o);
    }

    @Override
    public void delete(Converter o) {
        data.delete(o);
    }

}
