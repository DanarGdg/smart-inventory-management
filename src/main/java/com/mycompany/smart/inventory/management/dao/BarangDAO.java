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

    public boolean tambahBarang(String kodeBarang, String namaBarang, 
                                int idKategori, int stokAwal, 
                                int stokMinimum, String satuan) {
        String sql = "INSERT INTO barang (kode_barang, nama_barang, id_kategori, stok, stok_minimum, satuan) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kodeBarang);
            ps.setString(2, namaBarang);
            ps.setInt(3, idKategori);
            ps.setInt(4, stokAwal);
            ps.setInt(5, stokMinimum);
            ps.setString(6, satuan);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Gagal tambah barang: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBarang(String kodeBarang, String namaBarang,
                               int idKategori, int stokAwal,
                               int stokMinimum, String satuan) {
        String sql = "UPDATE barang SET nama_barang = ?, id_kategori = ?, stok = ?, stok_minimum = ?, satuan = ? "
                + "WHERE kode_barang = ?";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaBarang);
            ps.setInt(2, idKategori);
            ps.setInt(3, stokAwal);
            ps.setInt(4, stokMinimum);
            ps.setString(5, satuan);
            ps.setString(6, kodeBarang);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Gagal update barang: " + e.getMessage());
            return false;
        }
    }

    public boolean hapusBarang(String kodeBarang) {
        String sql = "DELETE FROM barang WHERE kode_barang = ?";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kodeBarang);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Gagal hapus barang: " + e.getMessage());
            return false;
        }
    }

    public Barang getBarangByKode(String kodeBarang) {
        String sql = "SELECT b.kode_barang, b.nama_barang, b.id_kategori, "
                + "k.nama_kategori, b.stok, b.stok_minimum, b.satuan "
                + "FROM barang b "
                + "JOIN kategori k ON b.id_kategori = k.id_kategori "
                + "WHERE b.kode_barang = ?";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kodeBarang);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
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

                return barang;
            }

        } catch (Exception e) {
            System.out.println("Gagal ambil barang: " + e.getMessage());
        }

        return null;
    }

    public List<java.util.Map<String, Object>> getAllKategori() {
        List<java.util.Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT id_kategori, nama_kategori FROM kategori ORDER BY nama_kategori";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                java.util.Map<String, Object> kategori = new java.util.HashMap<>();
                kategori.put("id", rs.getInt("id_kategori"));
                kategori.put("nama", rs.getString("nama_kategori"));
                list.add(kategori);
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil kategori: " + e.getMessage());
        }

        return list;
    }

    public boolean tambahKategori(String namaKategori) {
        String sql = "INSERT INTO kategori (nama_kategori) VALUES (?)";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaKategori);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Gagal tambah kategori: " + e.getMessage());
            return false;
        }
    }
}
