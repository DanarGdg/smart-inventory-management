/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.model;

/**
 *
 * @author danar
 */
public class User {

    private int idUser;
    private String username;
    private String namaLengkap;
    private String role;

    public User(int idUser, String username, String namaLengkap, String role) {
        this.idUser = idUser;
        this.username = username;
        this.namaLengkap = namaLengkap;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isPetugas() {
        return "PETUGAS".equalsIgnoreCase(role);
    }
}
