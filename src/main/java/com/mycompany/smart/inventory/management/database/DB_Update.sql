/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Yogi
 * Created: June 1, 2026
 */
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

DROP TABLE IF EXISTS stok_keluar;
DROP TABLE IF EXISTS stok_masuk;
DROP TABLE IF EXISTS barang;
DROP TABLE IF EXISTS kategori;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'PETUGAS') NOT NULL
);

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

INSERT INTO users (username, password, nama_lengkap, role) VALUES
('admin', 'admin123', 'Administrator', 'ADMIN'),
('petugas', 'petugas123', 'Petugas Gudang', 'PETUGAS');

INSERT INTO kategori (nama_kategori) VALUES
('Elektronik'),
('ATK'),
('Furniture');

INSERT INTO barang (kode_barang, nama_barang, id_kategori, stok, stok_minimum, satuan) VALUES
('B001', 'Keyboard Logitech K120', 1, 8, 5, 'unit'),                     
('B002', 'Pulpen Pilot Ballliner Black', 2, 40, 20, 'pcs'),                
('B003', 'Mouse Wireless Logitech M170', 1, 3, 5, 'unit'),                 
('B004', 'Kursi Kerja Ergonomis Chitose', 3, 10, 3, 'unit'),               
('B005', 'Monitor Dell P2422H 24 Inch', 1, 3, 2, 'unit'),                  
('B006', 'Kertas HVS PaperOne A4 80gr', 2, 15, 5, 'rim'),                  
('B007', 'Meja Kantor Indachi Dritto', 3, 3, 2, 'unit'),                   
('B008', 'Printer Epson L3210 EcoTank', 1, 2, 2, 'unit'),                
('B009', 'Buku Kwitansi Sinar Dunia M', 2, 25, 10, 'buku'),                
('B010', 'Lemari Arsip Besi Lion Brands', 3, 3, 1, 'unit');                 

INSERT INTO stok_masuk (kode_barang, supplier, jumlah_masuk, tanggal_masuk) VALUES
('B001', 'PT Bhinneka Mentari Dimensi', 10, '2026-05-19'),
('B002', 'PT ASABA (Asahi Brands)', 50, '2026-05-19'),
('B003', 'PT Bhinneka Mentari Dimensi', 5, '2026-05-19'),
('B004', 'PT Chitose Internasional Tbk', 10, '2026-05-20'),
('B005', 'PT Synnex Metrodata Indonesia', 5, '2026-05-21'),
('B006', 'PT Parindo Pratama', 20, '2026-05-21'),
('B007', 'PT Chitose Internasional Tbk', 4, '2026-05-22'),
('B008', 'PT Epson Indonesia', 3, '2026-05-22'),
('B009', 'PT Tjiwi Kimia Tbk', 30, '2026-05-23'),
('B010', 'PT Lion Metal Works Tbk', 3, '2026-05-23');

INSERT INTO stok_keluar (kode_barang, departemen_tujuan, jumlah_keluar, tanggal_keluar) VALUES
('B001', 'UPT Teknologi Informasi dan Komunikasi (UPATIK) PNJ', 2, '2026-05-20'),
('B003', 'Jurusan Teknik Informatika dan Komputer', 2, '2026-05-20'),
('B006', 'Divisi BAAK & Umum PNJ', 5, '2026-05-22'),
('B008', 'Rektorat PNJ', 1, '2026-05-24'),
('B002', 'Pusat Penelitian dan Pengabdian kepada Masyarakat (P3M)PNJ', 10, '2026-05-25'),
('B005', 'Jurusan Teknik Elektro', 2, '2026-05-26'),
('B007', 'Jurusan Akuntansi', 1, '2026-05-26'),
('B009', 'Divisi Keuangan BAAK & Umum PNJ', 5, '2026-05-27');

