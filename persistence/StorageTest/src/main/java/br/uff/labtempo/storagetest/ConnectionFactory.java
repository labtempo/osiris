/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.storagetest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Felipe
 */
public class ConnectionFactory {

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/osiris", "root", ""); /*fj21 = nome do banco de dados, root = usuário do bando e "" a senha do bando de dados  */

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
