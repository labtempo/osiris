package br.uff.labtempo.storagetest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class Populate {

    static int TOTAL = 1000000;

    public static void main(String[] args) throws Exception {
        add();
    }

    static void string() throws Exception {
        Connection conn = new ConnectionFactory().getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("truncate `string`");
        long startTime = System.nanoTime();

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `string`(`data`) VALUES");

        for (int i = 0; i < TOTAL; i++) {
            if (i > 0) {
                sb.append(",");
            }
            int value = (int) (1000 * Math.random());
            sb.append(" (+" + String.valueOf(value) + ")");
        }

        stmt.execute(sb.toString());

        long stopTime = System.nanoTime();
        System.out.print("time: ");
        System.out.println(stopTime - startTime);
    }
    
    static void add() throws SQLException {
        long start = System.currentTimeMillis();
        Connection c = new ConnectionFactory().getConnection();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();
        stmt.execute("truncate `string`");
        stmt.execute("truncate `integer`");
        stmt.execute("truncate `double`");
        stmt.close();
        PreparedStatement psString = c.prepareStatement("INSERT INTO `string`(`data`) VALUES (?);");
        PreparedStatement psInt = c.prepareStatement("INSERT INTO `integer`(`data`) VALUES (?);");
        PreparedStatement psDouble = c.prepareStatement("INSERT INTO `double`(`data`) VALUES (?);");
        for (int i = 0; i < TOTAL; i++) {
            int value = (int) (1000 * Math.random());
            psString.setString(1, String.valueOf(value));
            psString.addBatch();
            psInt.setInt(1, value);
            psInt.addBatch();
            psDouble.setDouble(1, value);
            psDouble.addBatch();         
            
        }
        psString.executeBatch();
        psInt.executeBatch();
        psDouble.executeBatch();
        c.commit();

        long end = System.currentTimeMillis();
        System.out.println("Batch time was " + (end - start));

        psString.close();
        c.close();
    }   

}
