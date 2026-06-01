/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.dao;

import com.mycompany.smart.inventory.management.database.DBConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danar
 */
public class DashboardDAO {

    public int getTotalBarang() {
        return getCount("SELECT COUNT(*) FROM barang");
    }

    public int getTotalKategori() {
        return getCount("SELECT COUNT(*) FROM kategori");
    }

    public int getTotalStokKritis() {
        return getCount("SELECT COUNT(*) FROM barang WHERE stok <= stok_minimum");
    }

    public int getTotalStokMasuk() {
        return getSum("SELECT COALESCE(SUM(jumlah_masuk), 0) FROM stok_masuk");
    }

    public int getTotalStokKeluar() {
        return getSum("SELECT COALESCE(SUM(jumlah_keluar), 0) FROM stok_keluar");
    }

    private int getCount(String sql) {
        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil data count: " + e.getMessage());
        }

        return 0;
    }

    private int getSum(String sql) {
        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil data sum: " + e.getMessage());
        }

        return 0;
    }

    public List<Object[]> getBarangStokKritis() {
        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT b.kode_barang, b.nama_barang, k.nama_kategori, "
                + "b.stok, b.stok_minimum "
                + "FROM barang b "
                + "JOIN kategori k ON b.id_kategori = k.id_kategori "
                + "WHERE b.stok <= b.stok_minimum";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("nama_kategori"),
                    rs.getInt("stok"),
                    rs.getInt("stok_minimum"),
                    "KRITIS"
                };

                list.add(row);
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil stok kritis: " + e.getMessage());
        }

        return list;
    }

    public List<Object[]> cariBarangStokKritis(String keyword) {
        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT b.kode_barang, b.nama_barang, k.nama_kategori, "
                + "b.stok, b.stok_minimum "
                + "FROM barang b "
                + "JOIN kategori k ON b.id_kategori = k.id_kategori "
                + "WHERE b.stok <= b.stok_minimum "
                + "AND b.nama_barang LIKE ?";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("nama_kategori"),
                    rs.getInt("stok"),
                    rs.getInt("stok_minimum")
                };

                list.add(row);
            }

        } catch (Exception e) {
            System.out.println("Gagal mencari stok kritis: " + e.getMessage());
        }

        return list;
    }
}
