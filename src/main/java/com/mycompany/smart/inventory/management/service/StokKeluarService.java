package com.mycompany.smart.inventory.management.service;

import com.mycompany.smart.inventory.management.dao.StokKeluarDAO;

/**
 * Service untuk stok keluar.
 */
public class StokKeluarService {

    private StokKeluarDAO stokKeluarDAO = new StokKeluarDAO();

    public boolean simpanStokKeluar(
            String kodeBarang,
            String departemenTujuan,
            String jumlahKeluarText,
            String tanggalKeluar
    ) {
        if (kodeBarang == null || kodeBarang.isEmpty()) {
            throw new IllegalArgumentException("Barang belum dipilih");
        }

        if (departemenTujuan == null || departemenTujuan.trim().isEmpty()) {
            throw new IllegalArgumentException("Departemen tujuan tidak boleh kosong");
        }

        if (jumlahKeluarText == null || jumlahKeluarText.trim().isEmpty()) {
            throw new IllegalArgumentException("Jumlah keluar tidak boleh kosong");
        }

        int jumlahKeluar;

        try {
            jumlahKeluar = Integer.parseInt(jumlahKeluarText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Jumlah keluar harus berupa angka");
        }

        if (jumlahKeluar <= 0) {
            throw new IllegalArgumentException("Jumlah keluar harus lebih dari 0");
        }

        if (tanggalKeluar == null || tanggalKeluar.trim().isEmpty()) {
            throw new IllegalArgumentException("Tanggal keluar tidak boleh kosong");
        }

        boolean berhasil = stokKeluarDAO.tambahStokKeluar(
                kodeBarang,
                departemenTujuan,
                jumlahKeluar,
                tanggalKeluar
        );

        if (!berhasil) {
            throw new IllegalArgumentException("Stok tidak cukup atau barang tidak ditemukan");
        }

        return true;
    }
}
