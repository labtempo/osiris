/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.riak.teste;

import java.util.*;
import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.UnresolvedConflictException;

/**
 *
 * @author proto
 */
public class Riak {

    IRiakClient client;
    Bucket bucket;

    public Riak() throws RiakException {
        client = RiakFactory.pbcClient();

    }

    public void createBucket(String name) throws RiakRetryFailedException {
        if (client != null) {
            client.createBucket(name).execute();
        }
    }

    public boolean fetchBucket(String name) throws RiakRetryFailedException {
        bucket = client.fetchBucket(name).execute();
        if(bucket != null)
            return true;
        return  false;
    }

    public void insertValue(String key, String value) throws RiakRetryFailedException {
        bucket.store(key, value).execute();
    }

    public String fetchValue(String key) throws UnresolvedConflictException, RiakRetryFailedException {
        return bucket.fetch(key, String.class).execute();
    }

}
