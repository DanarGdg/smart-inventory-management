package com.mycompany.smart.inventory.management.model;

import java.sql.Date;

/**
 * Model untuk mencatat stok keluar.
 */
public class StokKeluar {

    private int idKeluar;
    private String kodeBarang;
    private String namaBarang;
    private String departemenTujuan;
    private int jumlahKeluar;
    private Date tanggalKeluar;

    public StokKeluar(int idKeluar, String kodeBarang, String namaBarang,
            String departemenTujuan, int jumlahKeluar, Date tanggalKeluar) {
        this.idKeluar = idKeluar;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.departemenTujuan = departemenTujuan;
        this.jumlahKeluar = jumlahKeluar;
        this.tanggalKeluar = tanggalKeluar;
    }

    public int getIdKeluar() {
        return idKeluar;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public String getDepartemenTujuan() {
        return departemenTujuan;
    }

    public int getJumlahKeluar() {
        return jumlahKeluar;
    }

    public Date getTanggalKeluar() {
        return tanggalKeluar;
    }
}
