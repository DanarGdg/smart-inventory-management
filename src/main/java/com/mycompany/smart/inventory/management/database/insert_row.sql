/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  danar
 * Created: 19 May 2026
 */

TRUNCATE stok_keluar;
TRUNCATE stok_masuk;
TRUNCATE barang;
TRUNCATE kategori;

INSERT INTO kategori (nama_kategori) VALUES
('Elektronik'),
('ATK'),
('Furniture');

INSERT INTO barang (
    kode_barang, nama_barang, id_kategori, stok, stok_minimum, satuan
) VALUES
('B001', 'Keyboard Logitech', 1, 8, 5, 'unit'),
('B002', 'Pulpen Pilot', 2, 50, 20, 'pcs'),
('B003', 'Mouse Wireless', 1, 3, 5, 'unit'),
('B004', 'Kursi Kantor', 3, 10, 3, 'unit');

INSERT INTO stok_masuk (
    kode_barang, supplier, jumlah_masuk, tanggal_masuk
) VALUES
('B001', 'PT Sumber Jaya', 10, '2026-05-19'),
('B002', 'Toko ATK Mandiri', 50, '2026-05-19'),
('B003', 'PT Sumber Jaya', 5, '2026-05-19');

INSERT INTO stok_keluar (
    kode_barang, departemen_tujuan, jumlah_keluar, tanggal_keluar
) VALUES
('B001', 'Lab Komputer', 2, '2026-05-20'),
('B003', 'Ruang Dosen', 2, '2026-05-20');