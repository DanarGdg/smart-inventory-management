/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smart.inventory.management.service;

import com.mycompany.smart.inventory.management.dao.DashboardDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author danar
 */
public class DashboardService {

    private DashboardDAO dashboardDAO = new DashboardDAO();

    public int getTotalBarang() {
        return dashboardDAO.getTotalBarang();
    }

    public int getTotalKategori() {
        return dashboardDAO.getTotalKategori();
    }

    public int getTotalStokMasuk() {
        return dashboardDAO.getTotalStokMasuk();
    }

    public int getTotalStokKeluar() {
        return dashboardDAO.getTotalStokKeluar();
    }

    public int getTotalStokKritis() {
        return dashboardDAO.getTotalStokKritis();
    }

    public Map<String, Integer> getJumlahBarangPerKategori() {
        return dashboardDAO.getJumlahBarangPerKategori();
    }

    public Map<String, Integer> getStatusStokBarang() {
        return dashboardDAO.getStatusStokBarang();
    }

    public List<Object[]> cariBarangStokKritis(String keyword) {
        if (keyword == null) {
            keyword = "";
        }

        return dashboardDAO.cariBarangStokKritis(keyword.trim());
    }
}
