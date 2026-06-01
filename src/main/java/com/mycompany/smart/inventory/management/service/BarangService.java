/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.service;

import com.mycompany.smart.inventory.management.dao.BarangDAO;

/**
 *
 * @author danar
 */
public class BarangService {

    private BarangDAO barangDAO = new BarangDAO();

    public boolean tambahBarang(String kodeBarang, String namaBarang,
            int idKategori, String stokAwalText,
            String stokMinimumText, String satuan) {

        if (kodeBarang == null || kodeBarang.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode barang tidak boleh kosong");
        }

        if (namaBarang == null || namaBarang.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong");
        }

        if (idKategori <= 0) {
            throw new IllegalArgumentException("Kategori belum dipilih");
        }

        if (stokAwalText == null || stokAwalText.trim().isEmpty()) {
            throw new IllegalArgumentException("Stok awal tidak boleh kosong");
        }

        int stokAwal;
        try {
            stokAwal = Integer.parseInt(stokAwalText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Stok awal harus berupa angka");
        }

        if (stokAwal < 0) {
            throw new IllegalArgumentException("Stok awal tidak boleh negatif");
        }

        if (stokMinimumText == null || stokMinimumText.trim().isEmpty()) {
            throw new IllegalArgumentException("Stok minimum tidak boleh kosong");
        }

        int stokMinimum;
        try {
            stokMinimum = Integer.parseInt(stokMinimumText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Stok minimum harus berupa angka");
        }

        if (stokMinimum < 0) {
            throw new IllegalArgumentException("Stok minimum tidak boleh negatif");
        }

        if (satuan == null || satuan.trim().isEmpty()) {
            throw new IllegalArgumentException("Satuan tidak boleh kosong");
        }

        return barangDAO.tambahBarang(kodeBarang, namaBarang, idKategori,
                stokAwal, stokMinimum, satuan);
    }

    public boolean updateBarang(String kodeBarang, String namaBarang,
            int idKategori, String stokAwalText,
            String stokMinimumText, String satuan) {

        if (kodeBarang == null || kodeBarang.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode barang tidak boleh kosong");
        }

        if (namaBarang == null || namaBarang.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong");
        }

        if (idKategori <= 0) {
            throw new IllegalArgumentException("Kategori belum dipilih");
        }

        if (stokAwalText == null || stokAwalText.trim().isEmpty()) {
            throw new IllegalArgumentException("Stok awal tidak boleh kosong");
        }

        int stokAwal;
        try {
            stokAwal = Integer.parseInt(stokAwalText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Stok awal harus berupa angka");
        }

        if (stokAwal < 0) {
            throw new IllegalArgumentException("Stok awal tidak boleh negatif");
        }

        if (stokMinimumText == null || stokMinimumText.trim().isEmpty()) {
            throw new IllegalArgumentException("Stok minimum tidak boleh kosong");
        }

        int stokMinimum;
        try {
            stokMinimum = Integer.parseInt(stokMinimumText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Stok minimum harus berupa angka");
        }

        if (stokMinimum < 0) {
            throw new IllegalArgumentException("Stok minimum tidak boleh negatif");
        }

        if (satuan == null || satuan.trim().isEmpty()) {
            throw new IllegalArgumentException("Satuan tidak boleh kosong");
        }

        return barangDAO.updateBarang(kodeBarang, namaBarang, idKategori,
                stokAwal, stokMinimum, satuan);
    }

    public boolean hapusBarang(String kodeBarang) {
        if (kodeBarang == null || kodeBarang.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode barang tidak boleh kosong");
        }

        return barangDAO.hapusBarang(kodeBarang);
    }
}
