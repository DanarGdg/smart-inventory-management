/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.model;

/**
 *
 * @author danar
 */
public class Barang {

    private String kodeBarang;
    private String namaBarang;
    private int idKategori;
    private String namaKategori;
    private int stok;
    private int stokMinimum;
    private String satuan;

    public Barang(String kodeBarang, String namaBarang, int idKategori,
            String namaKategori, int stok, int stokMinimum, String satuan) {
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
        this.stok = stok;
        this.stokMinimum = stokMinimum;
        this.satuan = satuan;
    }
    
    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getStokMinimum() {
        return stokMinimum;
    }

    public void setStokMinimum(int stokMinimum) {
        this.stokMinimum = stokMinimum;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public boolean isStokKritis() {
        return stok <= stokMinimum;
    }

    public String getJenisBarang() {
        return "Barang Umum";
    }

    @Override
    public String toString() {
        return namaBarang;
    }
}
