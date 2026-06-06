/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.dao;

import com.mycompany.smart.inventory.management.database.DBConfig;
import com.mycompany.smart.inventory.management.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author danar
 */
public class AuthDAO {
    public User login(String username, String password) {
        String sql = "SELECT id_user, username, nama_lengkap, role "
                + "FROM users "
                + "WHERE username = ? AND password = ?";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("nama_lengkap"),
                        rs.getString("role")
                );
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return null;
    }
}
