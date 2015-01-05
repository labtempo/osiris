/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.interfaces;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface Storage {

    public boolean createRepository(String repository) ;

    public List<String> listRepositories();
    
    public boolean hasRepository(String repository);

    public String getRepository(String repository);

    public boolean removeRepository(String repository);

    public List<String> listRepositoryKeys(String repository);
    
    public Map<String,String> getRepositoryKeysAndEntry(String repository);

    public void addEntry(String repository, String key, String value);

    public String getEntryContent(String repository, String key);

    public boolean removeEntry(String repository, String key);
    
}
