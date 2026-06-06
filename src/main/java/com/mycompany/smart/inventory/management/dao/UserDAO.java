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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danar
 */
public class UserDAO {

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();

        String sql = "SELECT id_user, username, nama_lengkap, role "
                + "FROM users "
                + "ORDER BY id_user DESC";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("nama_lengkap"),
                        rs.getString("role")
                ));
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil data user: " + e.getMessage());
        }

        return list;
    }

    public List<User> searchUsers(String keyword) {
        List<User> list = new ArrayList<>();

        String sql = "SELECT id_user, username, nama_lengkap, role "
                + "FROM users "
                + "WHERE username LIKE ? "
                + "OR nama_lengkap LIKE ? "
                + "OR role LIKE ? "
                + "ORDER BY id_user DESC";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new User(
                            rs.getInt("id_user"),
                            rs.getString("username"),
                            rs.getString("nama_lengkap"),
                            rs.getString("role")
                    ));
                }
            }

        } catch (Exception e) {
            System.out.println("Gagal mencari user: " + e.getMessage());
        }

        return list;
    }

    public boolean insertUser(String username, String password, String namaLengkap, String role) {
        String sql = "INSERT INTO users (username, password, nama_lengkap, role) "
                + "VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, namaLengkap);
            ps.setString(4, role);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Gagal tambah user: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(int idUser, String username, String password, String namaLengkap, String role) {
        String sql;

        boolean updatePassword = password != null && !password.trim().isEmpty();

        if (updatePassword) {
            sql = "UPDATE users SET username = ?, password = ?, nama_lengkap = ?, role = ? "
                    + "WHERE id_user = ?";
        } else {
            sql = "UPDATE users SET username = ?, nama_lengkap = ?, role = ? "
                    + "WHERE id_user = ?";
        }

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (updatePassword) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, namaLengkap);
                ps.setString(4, role);
                ps.setInt(5, idUser);
            } else {
                ps.setString(1, username);
                ps.setString(2, namaLengkap);
                ps.setString(3, role);
                ps.setInt(4, idUser);
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Gagal update user: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int idUser) {
        String sql = "DELETE FROM users WHERE id_user = ?";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Gagal hapus user: " + e.getMessage());
            return false;
        }
    }

    public boolean isUsernameExists(String username, Integer exceptIdUser) {
        String sql;

        if (exceptIdUser == null) {
            sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        } else {
            sql = "SELECT COUNT(*) FROM users WHERE username = ? AND id_user <> ?";
        }

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            if (exceptIdUser != null) {
                ps.setInt(2, exceptIdUser);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            System.out.println("Gagal cek username: " + e.getMessage());
        }

        return false;
    }
}
