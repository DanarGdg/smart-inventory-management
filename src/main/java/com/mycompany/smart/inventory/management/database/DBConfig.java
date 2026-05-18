/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author danar
 */
public class DBConfig {

    public static Connection getConnection() {
        try {
            String url
                    = "jdbc:mysql://localhost:3306/inventory_db";

            String user = "root";

            String password = "";

            Connection conn
                    = DriverManager.getConnection(
                            url,
                            user,
                            password);

            System.out.println("Koneksi berhasil");

            return conn;
        } catch (Exception e) {
            System.out.println(
                    "Koneksi gagal : "
                    + e.getMessage()
            );

            return null;
        }
    }
}
