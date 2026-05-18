/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.smart.inventory.management;

import com.mycompany.smart.inventory.management.database.DBConfig;

/**
 *
 * @author danar
 */
public class SmartInventoryManagement {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        DBConfig.getConnection();
    }
}
