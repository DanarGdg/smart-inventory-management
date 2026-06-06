/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.smart.inventory.management;

import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.smart.inventory.management.database.DBConfig;
import javax.swing.UIManager;

/**
 *
 * @author danar
 */
public class SmartInventoryManagement {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        DBConfig.getConnection();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            UIManager.put("Component.arc", 16);
            UIManager.put("Button.arc", 18);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 10);

            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.rowHeight", 36);

        } catch (Exception e) {
            System.out.println("Gagal load FlatLaf: " + e.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
