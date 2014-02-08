/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.datastorage;

import java.util.List;

/**
 *
 * @author Felipe
 */
public interface Storage {

    public void createRepository(String repository) ;

    public String[] listRepositories();
    
    public boolean hasRepository(String repository);

    public String getRepository(String repository);

    public boolean removeRepository(String repository);

    public String[] listRepositoryKeys(String repository);

    public void addEntry(String repository, String key, String value);

    public String getEntryContent(String repository, String key);

    public boolean removeEntry(String repository, String key);
    
}
