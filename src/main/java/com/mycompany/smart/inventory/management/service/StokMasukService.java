/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.service;

import com.mycompany.smart.inventory.management.dao.StokMasukDAO;
import com.mycompany.smart.inventory.management.model.StokMasuk;
import java.util.List;

/**
 *
 * @author danar
 */
public class StokMasukService {

    private StokMasukDAO stokMasukDAO = new StokMasukDAO();

    public boolean simpanStokMasuk(
            String kodeBarang,
            String supplier,
            String jumlahMasukText,
            String tanggalMasuk
    ) {

        if (kodeBarang == null || kodeBarang.isEmpty()) {
            throw new IllegalArgumentException("Barang belum dipilih");
        }

        if (supplier == null || supplier.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier tidak boleh kosong");
        }

        if (jumlahMasukText == null || jumlahMasukText.trim().isEmpty()) {
            throw new IllegalArgumentException("Jumlah masuk tidak boleh kosong");
        }

        int jumlahMasuk;

        try {
            jumlahMasuk = Integer.parseInt(jumlahMasukText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Jumlah masuk harus berupa angka");
        }

        if (jumlahMasuk <= 0) {
            throw new IllegalArgumentException("Jumlah masuk harus lebih dari 0");
        }

        if (tanggalMasuk == null || tanggalMasuk.trim().isEmpty()) {
            throw new IllegalArgumentException("Tanggal masuk tidak boleh kosong");
        }

        return stokMasukDAO.tambahStokMasuk(
                kodeBarang,
                supplier,
                jumlahMasuk,
                tanggalMasuk
        );
    }

    public List<StokMasuk> getRiwayatStokMasuk() {
        return stokMasukDAO.getRiwayatStokMasuk();
    }

    public List<StokMasuk> searchRiwayatStokMasuk(
            String keyword,
            String tanggalDari,
            String tanggalSampai
    ) {
        if (keyword == null) {
            keyword = "";
        }

        return stokMasukDAO.searchRiwayatStokMasuk(
                keyword.trim(),
                tanggalDari,
                tanggalSampai
        );
    }
}
