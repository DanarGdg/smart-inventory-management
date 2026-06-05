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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author danar
 */
public class DashboardDAO {

    private int getSingleInt(String sql) {
        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil data dashboard: " + e.getMessage());
        }

        return 0;
    }

    public int getTotalBarang() {
        String sql = "SELECT COUNT(*) FROM barang";
        return getSingleInt(sql);
    }

    public int getTotalKategori() {
        String sql = "SELECT COUNT(*) FROM kategori";
        return getSingleInt(sql);
    }

    public int getTotalStokKritis() {
        String sql = "SELECT COUNT(*) FROM barang WHERE stok <= stok_minimum";
        return getSingleInt(sql);
    }

    public int getTotalStokMasuk() {
        String sql = "SELECT COALESCE(SUM(jumlah_masuk), 0) FROM stok_masuk";
        return getSingleInt(sql);
    }

    public int getTotalStokKeluar() {
        String sql = "SELECT COALESCE(SUM(jumlah_keluar), 0) FROM stok_keluar";
        return getSingleInt(sql);
    }

    public Map<String, Integer> getJumlahBarangPerKategori() {
        Map<String, Integer> data = new LinkedHashMap<>();

        String sql = "SELECT k.nama_kategori, COUNT(b.kode_barang) AS total "
                + "FROM kategori k "
                + "LEFT JOIN barang b ON k.id_kategori = b.id_kategori "
                + "GROUP BY k.id_kategori, k.nama_kategori "
                + "ORDER BY k.nama_kategori";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.put(
                        rs.getString("nama_kategori"),
                        rs.getInt("total")
                );
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil chart kategori: " + e.getMessage());
        }

        return data;
    }

    public Map<String, Integer> getStatusStokBarang() {
        Map<String, Integer> data = new LinkedHashMap<>();

        String sql = "SELECT "
                + "SUM(CASE WHEN stok > stok_minimum THEN 1 ELSE 0 END) AS stok_aman, "
                + "SUM(CASE WHEN stok <= stok_minimum THEN 1 ELSE 0 END) AS stok_kritis "
                + "FROM barang";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                data.put("Stok Aman", rs.getInt("stok_aman"));
                data.put("Stok Kritis", rs.getInt("stok_kritis"));
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil status stok: " + e.getMessage());
        }

        return data;
    }

    public List<Object[]> cariBarangStokKritis(String keyword) {
        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT b.kode_barang, b.nama_barang, k.nama_kategori, "
                + "b.stok, b.stok_minimum "
                + "FROM barang b "
                + "JOIN kategori k ON b.id_kategori = k.id_kategori "
                + "WHERE b.stok <= b.stok_minimum "
                + "AND (b.kode_barang LIKE ? "
                + "OR b.nama_barang LIKE ? "
                + "OR k.nama_kategori LIKE ?) "
                + "ORDER BY b.stok ASC";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("nama_kategori"),
                        rs.getInt("stok"),
                        rs.getInt("stok_minimum")
                    });
                }
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil barang stok kritis: " + e.getMessage());
        }

        return list;
    }
}
