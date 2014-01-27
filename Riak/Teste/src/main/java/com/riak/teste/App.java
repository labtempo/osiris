package com.riak.teste;

import com.basho.riak.client.RiakException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws RiakException
    {
        Riak riak = new Riak();
        riak.fetchBucket("t");
        riak.insertValue("node","node 5");
        
    }
    
}
