/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.dao;

import com.mycompany.smart.inventory.management.model.StokMasuk;

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
public class StokMasukDAO {

    public boolean tambahStokMasuk(
            String kodeBarang,
            String supplier,
            int jumlahMasuk,
            String tanggalMasuk
    ) {
        String insertSql = "INSERT INTO stok_masuk "
                + "(kode_barang, supplier, jumlah_masuk, tanggal_masuk) "
                + "VALUES (?, ?, ?, ?)";

        String updateSql = "UPDATE barang SET stok = stok + ? "
                + "WHERE kode_barang = ?";

        try {
            Connection conn = DBConfig.getConnection();

            PreparedStatement insertPs = conn.prepareStatement(insertSql);
            insertPs.setString(1, kodeBarang);
            insertPs.setString(2, supplier);
            insertPs.setInt(3, jumlahMasuk);
            insertPs.setString(4, tanggalMasuk);
            insertPs.executeUpdate();

            PreparedStatement updatePs = conn.prepareStatement(updateSql);
            updatePs.setInt(1, jumlahMasuk);
            updatePs.setString(2, kodeBarang);
            updatePs.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Gagal tambah stok masuk: " + e.getMessage());
            return false;
        }
    }

    public List<StokMasuk> getRiwayatStokMasuk() {
        List<StokMasuk> list = new ArrayList<>();
        String sql = "SELECT sm.id_masuk, sm.kode_barang, b.nama_barang, "
                + "sm.supplier, sm.jumlah_masuk, sm.tanggal_masuk "
                + "FROM stok_masuk sm "
                + "JOIN barang b ON sm.kode_barang = b.kode_barang "
                + "ORDER BY sm.id_masuk DESC";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StokMasuk stokMasuk = new StokMasuk(
                        rs.getInt("id_masuk"),
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("supplier"),
                        rs.getInt("jumlah_masuk"),
                        rs.getDate("tanggal_masuk")
                );

                list.add(stokMasuk);
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil riwayat stok masuk: " + e.getMessage());
        }

        return list;
    }

    public List<StokMasuk> searchRiwayatStokMasuk(String keyword) {
        List<StokMasuk> list = new ArrayList<>();

        String sql = "SELECT sm.id_masuk, sm.kode_barang, b.nama_barang, "
                + "sm.supplier, sm.jumlah_masuk, sm.tanggal_masuk "
                + "FROM stok_masuk sm "
                + "JOIN barang b ON sm.kode_barang = b.kode_barang "
                + "WHERE b.nama_barang LIKE ? "
                + "OR sm.kode_barang LIKE ? "
                + "OR sm.supplier LIKE ? "
                + "OR sm.tanggal_masuk LIKE ? "
                + "ORDER BY sm.id_masuk DESC";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            String searchKeyword = "%" + keyword + "%";

            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, searchKeyword);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StokMasuk stokMasuk = new StokMasuk(
                        rs.getInt("id_masuk"),
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("supplier"),
                        rs.getInt("jumlah_masuk"),
                        rs.getDate("tanggal_masuk")
                );

                list.add(stokMasuk);
            }

        } catch (Exception e) {
            System.out.println("Gagal mencari riwayat stok masuk: " + e.getMessage());
        }

        return list;
    }

    public List<StokMasuk> searchRiwayatStokMasuk(
            String keyword,
            String tanggalDari,
            String tanggalSampai
    ) {
        List<StokMasuk> list = new ArrayList<>();

        String sql = "SELECT sm.id_masuk, sm.kode_barang, b.nama_barang, "
                + "sm.supplier, sm.jumlah_masuk, sm.tanggal_masuk "
                + "FROM stok_masuk sm "
                + "JOIN barang b ON sm.kode_barang = b.kode_barang "
                + "WHERE (b.nama_barang LIKE ? "
                + "OR sm.kode_barang LIKE ? "
                + "OR sm.supplier LIKE ?) "
                + "AND sm.tanggal_masuk BETWEEN ? AND ? "
                + "ORDER BY sm.id_masuk DESC";

        try {
            Connection conn = DBConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            String searchKeyword = "%" + keyword + "%";

            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, tanggalDari);
            ps.setString(5, tanggalSampai);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StokMasuk stokMasuk = new StokMasuk(
                        rs.getInt("id_masuk"),
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("supplier"),
                        rs.getInt("jumlah_masuk"),
                        rs.getDate("tanggal_masuk")
                );

                list.add(stokMasuk);
            }

        } catch (Exception e) {
            System.out.println("Gagal mencari riwayat stok masuk: " + e.getMessage());
        }

        return list;
    }
}
