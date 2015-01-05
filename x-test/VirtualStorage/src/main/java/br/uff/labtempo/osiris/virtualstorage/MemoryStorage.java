/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualstorage;


import br.uff.labtempo.osiris.util.interfaces.Storage;
import java.util.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class MemoryStorage implements Storage {

    Map<String, Map<String, String>> repositories;

    public MemoryStorage() {
        repositories = new HashMap<String, Map<String, String>>();
    }

    @Override
    public boolean createRepository(String repository) {
        System.out.println("createRepository: "+repository);
        if (!hasRepository(repository)) {
            repositories.put(repository, new HashMap<String, String>());
            return true;
        }
        return false;
    }

    @Override
    public List<String> listRepositories() {
        List<String> repos = new ArrayList<String>();

        for (String key : repositories.keySet()) {
            repos.add(key);
        }

        return repos;

    }

    @Override
    public boolean hasRepository(String repository) {
        return hasKey(repositories, repository);
    }

    @Override
    public String getRepository(String repository) {
        if (hasKey(repositories, repository)) {
            return repository;
        }
        return null;
    }

    @Override
    public boolean removeRepository(String repository) {
        if (!hasKey(repositories, repository)) {
            return false;
        }
        repositories.remove(repository);
        return true;
    }

    @Override
    public List<String> listRepositoryKeys(String repository) {
        if (!hasRepository(repository)) {
            return null;
        }

        Map<String, String> repo = repositories.get(repository);

        List<String> repos = new ArrayList<String>();

        for (String key : repo.keySet()) {
            repos.add(key);
        }

        return repos;
    }

    @Override
    public void addEntry(String repository, String key, String value) {
        if (hasRepository(repository)) {
            Map<String, String> repositoryContent = repositories.get(repository);
            repositoryContent.put(key, value);
        }
    }

    @Override
    public String getEntryContent(String repository, String key) {
        if (hasRepository(repository)) {
            Map<String, String> repo = repositories.get(repository);
            if (hasKey(repo, key)) {
                return repo.get(key);
            }
        }
        return null;
    }

    @Override
    public boolean removeEntry(String repository, String key) {
        if (hasRepository(repository)) {
            Map<String, String> repo = repositories.get(repository);
            if (hasKey(repo, key)) {
                repo.remove(key);
                return true;
            }
        }
        return false;
    }

    private boolean hasKey(Map map, String key) {
        return map.containsKey(key);
    }

    @Override
    public Map<String, String> getRepositoryKeysAndEntry(String repository) {
        System.out.println("getRepositoryKeysAndEnrty: "+repository); 
        Map<String, String> data = repositories.get(repository);
        return data;
    }

}
