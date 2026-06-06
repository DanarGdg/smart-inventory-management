/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.service;

import com.mycompany.smart.inventory.management.dao.UserDAO;
import com.mycompany.smart.inventory.management.model.User;
import java.util.List;

/**
 *
 * @author danar
 */
public class UserService {

    private UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<User> searchUsers(String keyword) {
        if (keyword == null) {
            keyword = "";
        }

        return userDAO.searchUsers(keyword.trim());
    }

    public boolean tambahUser(String username, String password, String namaLengkap, String role) {
        validateUserInput(username, password, namaLengkap, role, true);

        if (userDAO.isUsernameExists(username.trim(), null)) {
            throw new IllegalArgumentException("Username sudah digunakan");
        }

        return userDAO.insertUser(
                username.trim(),
                password.trim(),
                namaLengkap.trim(),
                role
        );
    }

    public boolean updateUser(int idUser, String username, String password, String namaLengkap, String role) {
        if (idUser <= 0) {
            throw new IllegalArgumentException("Pilih user yang ingin diupdate");
        }

        validateUserInput(username, password, namaLengkap, role, false);

        if (userDAO.isUsernameExists(username.trim(), idUser)) {
            throw new IllegalArgumentException("Username sudah digunakan oleh user lain");
        }

        return userDAO.updateUser(
                idUser,
                username.trim(),
                password == null ? "" : password.trim(),
                namaLengkap.trim(),
                role
        );
    }

    public boolean hapusUser(int idUser, int currentUserId) {
        if (idUser <= 0) {
            throw new IllegalArgumentException("Pilih user yang ingin dihapus");
        }

        if (idUser == currentUserId) {
            throw new IllegalArgumentException("User yang sedang login tidak boleh menghapus akun sendiri");
        }

        return userDAO.deleteUser(idUser);
    }

    private void validateUserInput(
            String username,
            String password,
            String namaLengkap,
            String role,
            boolean passwordRequired
    ) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }

        if (passwordRequired && (password == null || password.trim().isEmpty())) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }

        if (namaLengkap == null || namaLengkap.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama lengkap tidak boleh kosong");
        }

        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role harus dipilih");
        }

        if (!role.equals("ADMIN") && !role.equals("PETUGAS")) {
            throw new IllegalArgumentException("Role tidak valid");
        }
    }
}
