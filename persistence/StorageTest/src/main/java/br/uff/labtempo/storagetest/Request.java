/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.storagetest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Felipe
 */
public class Request {

    private static String query = "SELECT avg(data) FROM `string` ";

    public static void main(String[] args) throws Exception {
        Connection conn = new ConnectionFactory().getConnection();
        Statement stmt = conn.createStatement();
        long startTime = System.nanoTime();
       ResultSet rs = stmt.executeQuery(query);
       while( rs.next() )
         System.out.println( rs.getString(1) ) ;
        
        long stopTime = System.nanoTime();
        System.out.print("time: ");
        System.out.println(stopTime - startTime);
    }
}
