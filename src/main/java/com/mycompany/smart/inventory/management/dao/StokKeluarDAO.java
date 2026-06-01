package com.mycompany.smart.inventory.management.dao;

import com.mycompany.smart.inventory.management.database.DBConfig;
import com.mycompany.smart.inventory.management.model.StokKeluar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO untuk stok keluar.
 */
public class StokKeluarDAO {

    public boolean tambahStokKeluar(
            String kodeBarang,
            String departemenTujuan,
            int jumlahKeluar,
            String tanggalKeluar
    ) {
        String checkSql = "SELECT stok FROM barang WHERE kode_barang = ?";
        String insertSql = "INSERT INTO stok_keluar "
                + "(kode_barang, departemen_tujuan, jumlah_keluar, tanggal_keluar) "
                + "VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE barang SET stok = stok - ? "
                + "WHERE kode_barang = ? AND stok >= ?";

        try (Connection conn = DBConfig.getConnection()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, kodeBarang);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }

                    int stokSaatIni = rs.getInt("stok");
                    if (stokSaatIni < jumlahKeluar) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setString(1, kodeBarang);
                insertPs.setString(2, departemenTujuan);
                insertPs.setInt(3, jumlahKeluar);
                insertPs.setString(4, tanggalKeluar);
                insertPs.executeUpdate();
            }

            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setInt(1, jumlahKeluar);
                updatePs.setString(2, kodeBarang);
                updatePs.setInt(3, jumlahKeluar);
                int rows = updatePs.executeUpdate();

                if (rows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal tambah stok keluar: " + e.getMessage());
            return false;
        }
    }

    public List<StokKeluar> getRiwayatStokKeluar() {
        List<StokKeluar> list = new ArrayList<>();
        String sql = "SELECT sk.id_keluar, sk.kode_barang, b.nama_barang, "
                + "sk.departemen_tujuan, sk.jumlah_keluar, sk.tanggal_keluar "
                + "FROM stok_keluar sk "
                + "JOIN barang b ON sk.kode_barang = b.kode_barang "
                + "ORDER BY sk.id_keluar DESC";

        try (Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StokKeluar stokKeluar = new StokKeluar(
                        rs.getInt("id_keluar"),
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("departemen_tujuan"),
                        rs.getInt("jumlah_keluar"),
                        rs.getDate("tanggal_keluar")
                );
                list.add(stokKeluar);
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil riwayat stok keluar: " + e.getMessage());
        }

        return list;
    }

    public List<StokKeluar> searchRiwayatStokKeluar(String keyword) {
        List<StokKeluar> list = new ArrayList<>();

        String sql = "SELECT sk.id_keluar, sk.kode_barang, b.nama_barang, "
                + "sk.departemen_tujuan, sk.jumlah_keluar, sk.tanggal_keluar "
                + "FROM stok_keluar sk "
                + "JOIN barang b ON sk.kode_barang = b.kode_barang "
                + "WHERE b.nama_barang LIKE ? "
                + "OR sk.kode_barang LIKE ? "
                + "OR sk.departemen_tujuan LIKE ? "
                + "OR sk.tanggal_keluar LIKE ? "
                + "ORDER BY sk.id_keluar DESC";

        try (Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword + "%";

            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StokKeluar stokKeluar = new StokKeluar(
                            rs.getInt("id_keluar"),
                            rs.getString("kode_barang"),
                            rs.getString("nama_barang"),
                            rs.getString("departemen_tujuan"),
                            rs.getInt("jumlah_keluar"),
                            rs.getDate("tanggal_keluar")
                    );

                    list.add(stokKeluar);
                }
            }

        } catch (Exception e) {
            System.out.println("Gagal mencari riwayat stok keluar: " + e.getMessage());
        }

        return list;
    }
}
