/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.service;

import com.mycompany.smart.inventory.management.dao.AuthDAO;
import com.mycompany.smart.inventory.management.model.User;

/**
 *
 * @author danar
 */
public class AuthService {

    private AuthDAO authDAO = new AuthDAO();

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }

        User user = authDAO.login(username.trim(), password.trim());

        if (user == null) {
            throw new IllegalArgumentException("Username atau password salah");
        }

        return user;
    }
}
