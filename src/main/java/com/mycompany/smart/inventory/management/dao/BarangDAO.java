/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.dao;

import com.mycompany.smart.inventory.management.database.DBConfig;
import com.mycompany.smart.inventory.management.model.ATK;
import com.mycompany.smart.inventory.management.model.Barang;
import com.mycompany.smart.inventory.management.model.Elektronik;
import com.mycompany.smart.inventory.management.model.Furniture;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danar
 */
public class BarangDAO {

    public List<Barang> getAllBarang() {
        List<Barang> list = new ArrayList<>();

        String sql = "SELECT b.kode_barang, b.nama_barang, b.id_kategori, "
                + "k.nama_kategori, b.stok, b.stok_minimum, b.satuan "
                + "FROM barang b "
                + "JOIN kategori k ON b.id_kategori = k.id_kategori";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String kodeBarang = rs.getString("kode_barang");
                String namaBarang = rs.getString("nama_barang");
                int idKategori = rs.getInt("id_kategori");
                String namaKategori = rs.getString("nama_kategori");
                int stok = rs.getInt("stok");
                int stokMinimum = rs.getInt("stok_minimum");
                String satuan = rs.getString("satuan");

                Barang barang;

                if (namaKategori.equalsIgnoreCase("Elektronik")) {
                    barang = new Elektronik(
                            kodeBarang, namaBarang, idKategori,
                            namaKategori, stok, stokMinimum, satuan
                    );
                } else if (namaKategori.equalsIgnoreCase("ATK")) {
                    barang = new ATK(
                            kodeBarang, namaBarang, idKategori,
                            namaKategori, stok, stokMinimum, satuan
                    );
                } else if (namaKategori.equalsIgnoreCase("Furniture")) {
                    barang = new Furniture(
                            kodeBarang, namaBarang, idKategori,
                            namaKategori, stok, stokMinimum, satuan
                    );
                } else {
                    barang = new Barang(
                            kodeBarang, namaBarang, idKategori,
                            namaKategori, stok, stokMinimum, satuan
                    );
                }
                
                list.add(barang);
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil data barang: " + e.getMessage());
        }

        return list;
    }
}
