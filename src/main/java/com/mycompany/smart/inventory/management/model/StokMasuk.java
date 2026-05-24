/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.model;

import java.sql.Date;

/**
 *
 * @author danar
 */
public class StokMasuk {

    private int idMasuk;
    private String kodeBarang;
    private String namaBarang;
    private String supplier;
    private int jumlahMasuk;
    private Date tanggalMasuk;

    public StokMasuk(int idMasuk, String kodeBarang, String namaBarang,
            String supplier, int jumlahMasuk, Date tanggalMasuk) {
        this.idMasuk = idMasuk;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.supplier = supplier;
        this.jumlahMasuk = jumlahMasuk;
        this.tanggalMasuk = tanggalMasuk;
    }

    public int getIdMasuk() {
        return idMasuk;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getJumlahMasuk() {
        return jumlahMasuk;
    }

    public Date getTanggalMasuk() {
        return tanggalMasuk;
    }
}
