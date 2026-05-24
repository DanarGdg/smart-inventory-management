/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.model;

/**
 *
 * @author danar
 */
public class Furniture extends Barang{

    public Furniture(
            String kodeBarang,
            String namaBarang,
            int idKategori,
            String namaKategori,
            int stok,
            int stokMinimum,
            String satuan
    ) {
        super(kodeBarang, namaBarang, idKategori, namaKategori, stok, stokMinimum, satuan);
    }

    @Override
    public String getJenisBarang() {
        return "Furniture";
    }
}
