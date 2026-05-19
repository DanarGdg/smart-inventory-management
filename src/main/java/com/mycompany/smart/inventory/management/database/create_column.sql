/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  danar
 * Created: 19 May 2026
 */

CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

DROP TABLE IF EXISTS stok_keluar;
DROP TABLE IF EXISTS stok_masuk;
DROP TABLE IF EXISTS barang;
DROP TABLE IF EXISTS kategori;

CREATE TABLE kategori (
    id_kategori INT AUTO_INCREMENT PRIMARY KEY,
    nama_kategori VARCHAR(50) NOT NULL
);

CREATE TABLE barang (
    kode_barang VARCHAR(10) PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    id_kategori INT NOT NULL,
    stok INT NOT NULL,
    stok_minimum INT NOT NULL,
    satuan VARCHAR(20),

    FOREIGN KEY (id_kategori) REFERENCES kategori(id_kategori)
);

CREATE TABLE stok_masuk (
    id_masuk INT AUTO_INCREMENT PRIMARY KEY,
    kode_barang VARCHAR(10) NOT NULL,
    supplier VARCHAR(100) NOT NULL,
    jumlah_masuk INT NOT NULL,
    tanggal_masuk DATE NOT NULL,

    FOREIGN KEY (kode_barang) REFERENCES barang(kode_barang)
);

CREATE TABLE stok_keluar (
    id_keluar INT AUTO_INCREMENT PRIMARY KEY,
    kode_barang VARCHAR(10) NOT NULL,
    departemen_tujuan VARCHAR(100) NOT NULL,
    jumlah_keluar INT NOT NULL,
    tanggal_keluar DATE NOT NULL,

    FOREIGN KEY (kode_barang) REFERENCES barang(kode_barang)
);