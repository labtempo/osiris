/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.storage;

import java.util.*;

/**
 *
 * @author Felipe
 */
public class Storage {

    Map<String, Map<String, String>> repositories;

    public Storage() {
        repositories = new HashMap<String, Map<String, String>>();
    }

    public void createRepository(String repository) {
        if (!hasRepository(repository)) {
            repositories.put(repository, new HashMap<String, String>());
        }
    }

    public String[] listRepositories() {
        List<String> repos = new ArrayList<String>();

        for (String key : repositories.keySet()) {
            repos.add(key);
        }

        return repos.toArray(new String[repos.size()]);

    }

    public boolean hasRepository(String repository) {
        return hasKey(repositories, repository);
    }

    public String getRepository(String repository) {
        if (hasKey(repositories, repository)) {
            return repository;
        }
        return null;
    }

    public boolean removeRepository(String repository) {
        if (!hasKey(repositories, repository)) {
            return false;
        }
        repositories.remove(repository);
        return true;
    }

    public String[] listRepositoryKeys(String repository) {
        if (!hasRepository(repository)) {
            return null;
        }

        Map<String, String> repo = repositories.get(repository);

        List<String> repos = new ArrayList<String>();

        for (String key : repo.keySet()) {
            repos.add(key);
        }

        return repos.toArray(new String[repos.size()]);
    }

    public void addEntry(String repository, String key, String value) {
        if (hasRepository(repository)) {
            Map<String, String> repositoryContent = repositories.get(repository);
            repositoryContent.put(key, value);
        }
    }

    public String getEntryContent(String repository, String key) {
        if (hasRepository(repository)) {
            Map<String, String> repo = repositories.get(repository);
            if (hasKey(repo, key)) {
                return repo.get(key);
            }
        }
        return null;
    }

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

}
