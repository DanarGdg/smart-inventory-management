/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.smart.inventory.management;

import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.smart.inventory.management.UI.AppColors;
import com.mycompany.smart.inventory.management.service.DashboardService;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.Map;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import java.util.List;
import com.mycompany.smart.inventory.management.dao.BarangDAO;
import com.mycompany.smart.inventory.management.model.Barang;
import com.mycompany.smart.inventory.management.model.StokMasuk;
import com.mycompany.smart.inventory.management.service.StokMasukService;
import java.awt.Dimension;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import org.openpdf.text.Document;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import com.mycompany.smart.inventory.management.model.StokKeluar;
import com.mycompany.smart.inventory.management.service.StokKeluarService;

/**
 *
 * @author danar
 */
public class NewJFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(NewJFrame.class.getName());
    private CardLayout cardLayout;

    private DashboardService dashboardService = new DashboardService();

    private BarangDAO barangDAO = new BarangDAO();
    private StokMasukService stokMasukService = new StokMasukService();
    private StokKeluarService stokKeluarService = new StokKeluarService();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private void loadDashboardData() {
        try {
            lblValueTotalBarang.setText(String.valueOf(
                    dashboardService.getTotalBarang()
            ));

            lblValueKategori.setText(String.valueOf(
                    dashboardService.getTotalKategori()
            ));

            lblValueStokMasuk.setText(String.valueOf(
                    dashboardService.getTotalStokMasuk()
            ));

            lblValueStokKeluar.setText(String.valueOf(
                    dashboardService.getTotalStokKeluar()
            ));

            lblValueStokKritis.setText(String.valueOf(
                    dashboardService.getTotalStokKritis()
            ));

            loadChartKategori();
            loadChartStatusStok();
            loadTableStokKritis("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal memuat dashboard: " + e.getMessage()
            );
        }
    }

    private void loadChartKategori() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> dataKategori
                = dashboardService.getJumlahBarangPerKategori();

        if (dataKategori.isEmpty()) {
            dataset.addValue(0, "Barang", "Belum Ada Data");
        } else {
            for (Map.Entry<String, Integer> entry : dataKategori.entrySet()) {
                dataset.addValue(
                        entry.getValue(),
                        "Barang",
                        entry.getKey()
                );
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Jumlah Barang per Kategori",
                "Kategori",
                "Jumlah",
                dataset
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setPaint(AppColors.TEXT_PRIMARY);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(AppColors.BORDER);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, AppColors.PRIMARY);
        renderer.setMaximumBarWidth(0.08);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder());
        chartPanel.setBackground(Color.WHITE);

        panelChartKategori.removeAll();
        panelChartKategori.setLayout(new BorderLayout());
        panelChartKategori.setBackground(Color.WHITE);
        panelChartKategori.setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
        panelChartKategori.add(chartPanel, BorderLayout.CENTER);
        panelChartKategori.revalidate();
        panelChartKategori.repaint();
    }

    private void loadChartStatusStok() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        Map<String, Integer> dataStatus
                = dashboardService.getStatusStokBarang();

        int stokAman = dataStatus.getOrDefault("Stok Aman", 0);
        int stokKritis = dataStatus.getOrDefault("Stok Kritis", 0);

        dataset.setValue("Stok Aman", stokAman);
        dataset.setValue("Stok Kritis", stokKritis);

        JFreeChart chart = ChartFactory.createPieChart(
                "Status Stok Barang",
                dataset,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setPaint(AppColors.TEXT_PRIMARY);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelBackgroundPaint(Color.WHITE);
        plot.setSectionPaint("Stok Aman", AppColors.PRIMARY);
        plot.setSectionPaint("Stok Kritis", AppColors.RED);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder());
        chartPanel.setBackground(Color.WHITE);

        panelChartStatusStok.removeAll();
        panelChartStatusStok.setLayout(new BorderLayout());
        panelChartStatusStok.setBackground(Color.WHITE);
        panelChartStatusStok.setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
        panelChartStatusStok.add(chartPanel, BorderLayout.CENTER);
        panelChartStatusStok.revalidate();
        panelChartStatusStok.repaint();
    }

    private void loadTableStokKritis(String keyword) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("No");
        model.addColumn("Kode");
        model.addColumn("Nama Barang");
        model.addColumn("Kategori");
        model.addColumn("Stok");
        model.addColumn("Minimum");
        model.addColumn("Status");

        List<Object[]> list
                = dashboardService.cariBarangStokKritis(keyword);

        int no = 1;

        for (Object[] data : list) {
            model.addRow(new Object[]{
                no++,
                data[0], // kode_barang
                data[1], // nama_barang
                data[2], // nama_kategori
                data[3], // stok
                data[4], // stok_minimum
                "KRITIS"
            });
        }

        jTable1.setModel(model);
        setTableStokKritisColor();
    }

    private void setTableStokKritisColor() {
        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                Component component = super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );

                if (isSelected) {
                    component.setBackground(new Color(220, 252, 231));
                    component.setForeground(AppColors.TEXT_PRIMARY);
                    return component;
                }

                component.setBackground(new Color(254, 226, 226));
                component.setForeground(new Color(127, 29, 29));

                return component;
            }
        });
    }

    private void setupDashboardSearch() {
        jTextField1.addActionListener(e -> {
            loadTableStokKritis(jTextField1.getText());
        });
    }

    private void btnCariStokKritisActionPerformed(java.awt.event.ActionEvent evt) {
        loadTableStokKritis(jTextField1.getText());
    }

    private void setupDateSpinner(JSpinner spinner) {
        spinner.setModel(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
        spinner.setValue(new Date());

        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinner.setPreferredSize(new Dimension(150, 36));
        spinner.setMinimumSize(new Dimension(150, 36));

        spinner.putClientProperty("JComponent.roundRect", true);

        JComponent editor = spinner.getEditor();

        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField textField
                    = ((JSpinner.DefaultEditor) editor).getTextField();

            textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            textField.setBackground(Color.WHITE);
            textField.setForeground(AppColors.TEXT_PRIMARY);

            textField.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        }
    }

    private void setupStokMasukComponents() {
        setupDateSpinner(spnTanggalMasuk);
        setupDateSpinner(spnDariTanggalMasuk);
        setupDateSpinner(spnSampaiTanggalMasuk);

        cmbBarangMasuk.putClientProperty("JComponent.roundRect", true);
        txtSupplierMasuk.putClientProperty("JComponent.roundRect", true);
        txtJumlahMasuk.putClientProperty("JComponent.roundRect", true);
        txtSearchStokMasuk.putClientProperty("JComponent.roundRect", true);

        stylePrimaryButton(btnSimpanStokMasuk);
        styleSecondaryButton(btnClearStokMasuk);
        styleSecondaryButton(btnCariStokMasuk);
        styleSecondaryButton(btnExportPdfStokMasuk);

        btnSimpanStokMasuk.addActionListener(e -> simpanStokMasuk());
        btnClearStokMasuk.addActionListener(e -> clearFormStokMasuk());
        btnCariStokMasuk.addActionListener(e -> cariRiwayatStokMasuk());
        btnExportPdfStokMasuk.addActionListener(e -> exportStokMasukPdf());

        txtSearchStokMasuk.addActionListener(e -> cariRiwayatStokMasuk());
    }

    private void showPage(String pageName) {
        cardLayout.show(contentPanel, pageName);

        resetSidebarButtonColor();

        switch (pageName) {
            case "dashboard":
                setActiveButton(btnDashboard);
                break;
            case "barang":
                setActiveButton(btnBarang);
                break;
            case "stokMasuk":
                setActiveButton(btnStokMasuk);
                break;
            case "stokKeluar":
                setActiveButton(btnStokKeluar);
                break;
        }
    }

    private void resetSidebarButtonColor() {
        JButton[] buttons = {
            btnDashboard,
            btnBarang,
            btnStokMasuk,
            btnStokKeluar
        };

        for (JButton btn : buttons) {
            btn.setBackground(AppColors.SIDEBAR_BG);
            btn.setForeground(AppColors.SIDEBAR_TEXT);
        }
    }

    private void setActiveButton(JButton button) {
        button.setBackground(AppColors.SIDEBAR_ACTIVE);
        button.setForeground(AppColors.SIDEBAR_ACTIVE_TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private void setupModernSidebar() {
        sidebarPanel.setBackground(AppColors.SIDEBAR_BG);
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 1, AppColors.BORDER
        ));

        styleSidebarButton(btnDashboard);
        styleSidebarButton(btnBarang);
        styleSidebarButton(btnStokMasuk);
        styleSidebarButton(btnStokKeluar);
    }

    private void styleSidebarButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        button.setBackground(AppColors.SIDEBAR_BG);
        button.setForeground(AppColors.SIDEBAR_TEXT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
    }

    private void setupSidebarButtonText() {
        btnDashboard.setHorizontalAlignment(SwingConstants.LEFT);
        btnBarang.setHorizontalAlignment(SwingConstants.LEFT);
        btnStokMasuk.setHorizontalAlignment(SwingConstants.LEFT);
        btnStokKeluar.setHorizontalAlignment(SwingConstants.LEFT);
    }

    private void setupModernFrame() {
        setTitle("Smart Inventory Management");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        getContentPane().setBackground(AppColors.BG_MAIN);

        contentPanel.setBackground(AppColors.BG_MAIN);
        contentPanel.setBorder(BorderFactory.createEmptyBorder());

        dashboardPanel.setBackground(AppColors.BG_MAIN);
        barangPanel.setBackground(AppColors.BG_MAIN);
        stokMasukPanel.setBackground(AppColors.BG_MAIN);
        stokKeluarPanel.setBackground(AppColors.BG_MAIN);

        jPanel1.setBackground(AppColors.SURFACE);
        jPanel1.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, AppColors.BORDER
        ));

        jLabel1.setForeground(AppColors.TEXT_PRIMARY);
    }

    private void setupDashboardCards() {
        styleCard(cardTotalBarang, true);
        styleCard(cardKategori, false);
        styleCard(cardStokMasuk, false);
        styleCard(cardStokKeluar, false);
        styleCard(cardStokKritis, false);

        styleCardLabel(lblTitleTotalBarang, true, 13, Font.PLAIN);
        styleCardLabel(lblValueTotalBarang, true, 32, Font.BOLD);
        styleCardLabel(lblInfoTotalBarang, true, 12, Font.PLAIN);

        styleCardLabel(lblTitleKategori, false, 13, Font.PLAIN);
        styleCardLabel(lblValueKategori, false, 32, Font.BOLD);
        styleCardLabel(lblInfoKategori, false, 12, Font.PLAIN);

        styleCardLabel(lblTitleStokMasuk, false, 13, Font.PLAIN);
        styleCardLabel(lblValueStokMasuk, false, 32, Font.BOLD);
        styleCardLabel(lblInfoStokMasuk, false, 12, Font.PLAIN);

        styleCardLabel(lblTitleStokKeluar, false, 13, Font.PLAIN);
        styleCardLabel(lblValueStokKeluar, false, 32, Font.BOLD);
        styleCardLabel(lblInfoStokKeluar, false, 12, Font.PLAIN);

        styleCardLabel(lblTitleStokKritis, false, 13, Font.PLAIN);
        styleCardLabel(lblValueStokKritis, false, 32, Font.BOLD);
        styleCardLabel(lblInfoStokKritis, false, 12, Font.PLAIN);
    }

    private void styleCard(JPanel panel, boolean primary) {
        panel.setOpaque(true);

        if (primary) {
            panel.setBackground(new Color(6, 78, 59));
        } else {
            panel.setBackground(Color.WHITE);
        }

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));
    }

    private void styleCardLabel(JLabel label, boolean primary, int size, int style) {
        label.setFont(new Font("Segoe UI", style, size));

        if (primary) {
            label.setForeground(Color.WHITE);
        } else {
            label.setForeground(new Color(17, 24, 39));
        }
    }

    private void setupModernTables() {
        styleTable(jTable1);
        styleTable(jTable2);
        styleTable(tblRiwayatStokMasuk);
        styleTable(tblRiwayatStokKeluar);
    }

    private void styleTable(javax.swing.JTable table) {
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(AppColors.BORDER);
        table.setSelectionBackground(new Color(220, 252, 231));
        table.setSelectionForeground(AppColors.TEXT_PRIMARY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(249, 250, 251));
        table.getTableHeader().setForeground(AppColors.TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, AppColors.BORDER
        ));
    }

    private void setupModernPanels() {
        styleContentCard(jPanel2); // FORM BARANG
        styleContentCard(jPanel4); // DAFTAR BARANG
        styleContentCard(jPanel6); // FORM STOK MASUK
        styleContentCard(jPanel7); // RIWAYAT STOK MASUK
        styleContentCard(jPanel8); // FORM STOK KELUAR
        styleContentCard(jPanel9); // RIWAYAT STOK KELUAR
    }

    private void styleContentCard(JPanel panel) {
        panel.setBackground(AppColors.SURFACE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER),
                BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
    }

    private void setupPageBackgrounds() {
        contentPanel.setBackground(new Color(243, 244, 246));
        dashboardPanel.setBackground(new Color(243, 244, 246));
        barangPanel.setBackground(new Color(243, 244, 246));
        stokMasukPanel.setBackground(new Color(243, 244, 246));
        stokKeluarPanel.setBackground(new Color(243, 244, 246));
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(AppColors.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setPreferredSize(new Dimension(90, 36));
        button.setMinimumSize(new Dimension(90, 36));

        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(AppColors.TEXT_PRIMARY);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setPreferredSize(new Dimension(90, 36));
        button.setMinimumSize(new Dimension(90, 36));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        button.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void setupModernButtons() {
        stylePrimaryButton(jButton1);
        styleSecondaryButton(jButton2);
        styleSecondaryButton(jButton3);
        styleSecondaryButton(jButton4);

        stylePrimaryButton(btnSimpanStokMasuk);
        styleSecondaryButton(btnClearStokMasuk);
        styleSecondaryButton(btnCariStokMasuk);
        styleSecondaryButton(btnExportPdfStokMasuk);

        stylePrimaryButton(btnSimpanStokKeluar);
        styleSecondaryButton(btnClearStokKeluar);
        styleSecondaryButton(btnCariStokKeluar);
        styleSecondaryButton(btnExportPdfStokKeluar);
    }

    private void loadComboBarangMasuk() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        try {
            List<Barang> listBarang = barangDAO.getAllBarang();

            for (Barang barang : listBarang) {
                model.addElement(
                        barang.getKodeBarang() + " - " + barang.getNamaBarang()
                );
            }

            cmbBarangMasuk.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal load barang: " + e.getMessage()
            );
        }
    }

    private String getKodeBarangMasukDipilih() {
        Object selected = cmbBarangMasuk.getSelectedItem();

        if (selected == null) {
            return null;
        }

        String item = selected.toString();

        if (!item.contains(" - ")) {
            return null;
        }

        return item.split(" - ")[0];
    }

    private void loadRiwayatStokMasuk() {
        List<StokMasuk> list = stokMasukService.getRiwayatStokMasuk();
        tampilkanRiwayatStokMasuk(list);
    }

    private void tampilkanRiwayatStokMasuk(List<StokMasuk> list) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("No");
        model.addColumn("Tanggal");
        model.addColumn("Kode");
        model.addColumn("Nama Barang");
        model.addColumn("Jumlah");
        model.addColumn("Supplier");

        int no = 1;

        for (StokMasuk sm : list) {
            model.addRow(new Object[]{
                no++,
                sm.getTanggalMasuk(),
                sm.getKodeBarang(),
                sm.getNamaBarang(),
                sm.getJumlahMasuk(),
                sm.getSupplier()
            });
        }

        tblRiwayatStokMasuk.setModel(model);
        styleTable(tblRiwayatStokMasuk);
    }

    private void simpanStokMasuk() {
        try {
            String kodeBarang = getKodeBarangMasukDipilih();
            String supplier = txtSupplierMasuk.getText();
            String jumlahMasuk = txtJumlahMasuk.getText();

            Date tanggal = (Date) spnTanggalMasuk.getValue();
            String tanggalMasuk = dateFormat.format(tanggal);

            boolean berhasil = stokMasukService.simpanStokMasuk(
                    kodeBarang,
                    supplier,
                    jumlahMasuk,
                    tanggalMasuk
            );

            if (berhasil) {
                JOptionPane.showMessageDialog(
                        this,
                        "Stok masuk berhasil disimpan"
                );

                clearFormStokMasuk();
                loadRiwayatStokMasuk();
                loadDashboardData();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }

    private void clearFormStokMasuk() {
        if (cmbBarangMasuk.getItemCount() > 0) {
            cmbBarangMasuk.setSelectedIndex(0);
        }

        txtSupplierMasuk.setText("");
        txtJumlahMasuk.setText("");
        spnTanggalMasuk.setValue(new Date());

        txtSupplierMasuk.requestFocus();
    }

    private void cariRiwayatStokMasuk() {
        try {
            String keyword = txtSearchStokMasuk.getText();

            Date dari = (Date) spnDariTanggalMasuk.getValue();
            Date sampai = (Date) spnSampaiTanggalMasuk.getValue();

            if (dari.after(sampai)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Tanggal dari tidak boleh lebih besar dari tanggal sampai"
                );
                return;
            }

            String tanggalDari = dateFormat.format(dari);
            String tanggalSampai = dateFormat.format(sampai);

            List<StokMasuk> list = stokMasukService.searchRiwayatStokMasuk(
                    keyword,
                    tanggalDari,
                    tanggalSampai
            );

            tampilkanRiwayatStokMasuk(list);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal mencari riwayat stok masuk: " + e.getMessage()
            );
        }
    }

    private void exportStokMasukPdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan Stok Masuk");
        chooser.setSelectedFile(new File("laporan_stok_masuk.pdf"));

        int result = chooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getAbsolutePath() + ".pdf");
        }

        try {
            Document document = new Document(PageSize.A4.rotate());

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(file)
            );

            document.open();

            org.openpdf.text.Font titleFont
                    = org.openpdf.text.FontFactory.getFont(
                            org.openpdf.text.FontFactory.HELVETICA_BOLD,
                            16
                    );

            document.add(new Paragraph("Laporan Stok Masuk", titleFont));
            document.add(new Paragraph("Tanggal Cetak: " + dateFormat.format(new Date())));
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(tblRiwayatStokMasuk.getColumnCount());
            pdfTable.setWidthPercentage(100);

            for (int i = 0; i < tblRiwayatStokMasuk.getColumnCount(); i++) {
                pdfTable.addCell(new Phrase(
                        tblRiwayatStokMasuk.getColumnName(i)
                ));
            }

            for (int row = 0; row < tblRiwayatStokMasuk.getRowCount(); row++) {
                for (int col = 0; col < tblRiwayatStokMasuk.getColumnCount(); col++) {
                    Object value = tblRiwayatStokMasuk.getValueAt(row, col);

                    pdfTable.addCell(
                            value == null ? "" : value.toString()
                    );
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(
                    this,
                    "PDF berhasil dibuat:\n" + file.getAbsolutePath()
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal export PDF: " + e.getMessage()
            );
        }
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBackground(Color.WHITE);
        textField.setForeground(AppColors.TEXT_PRIMARY);

        textField.setPreferredSize(new Dimension(150, 36));
        textField.setMinimumSize(new Dimension(150, 36));

        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        textField.putClientProperty("JComponent.roundRect", true);
    }

    private void setupModernInputs() {
        // Stok Masuk
        styleComboBox(cmbBarangMasuk);

        styleTextField(txtSupplierMasuk);
        styleTextField(txtJumlahMasuk);
        styleTextField(txtSearchStokMasuk);

        setupDateSpinner(spnTanggalMasuk);
        setupDateSpinner(spnDariTanggalMasuk);
        setupDateSpinner(spnSampaiTanggalMasuk);

        styleSecondaryButton(btnCariStokMasuk);

        // Stok Keluar
        styleComboBox(cmbBarangKeluar);
        styleTextField(txtTujuanKeluar);
        styleTextField(txtJumlahKeluar);
        styleTextField(txtSearchStokKeluar);

        setupDateSpinner(spnTanggalKeluar);
        setupDateSpinner(spnDariTanggalKeluar);
        setupDateSpinner(spnSampaiTanggalKeluar);
    }

    private void styleComboBox(JComboBox comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(AppColors.TEXT_PRIMARY);

        comboBox.setPreferredSize(new Dimension(150, 36));
        comboBox.setMinimumSize(new Dimension(150, 36));

        comboBox.putClientProperty("JComponent.roundRect", true);
        comboBox.putClientProperty("JComponent.minimumWidth", 150);
    }

    private void setupStokKeluarComponents() {
        setupDateSpinner(spnTanggalKeluar);
        setupDateSpinner(spnDariTanggalKeluar);
        setupDateSpinner(spnSampaiTanggalKeluar);

        styleComboBox(cmbBarangKeluar);

        styleTextField(txtTujuanKeluar);
        styleTextField(txtJumlahKeluar);
        styleTextField(txtSearchStokKeluar);

        stylePrimaryButton(btnSimpanStokKeluar);
        styleSecondaryButton(btnClearStokKeluar);
        styleSecondaryButton(btnCariStokKeluar);
        styleSecondaryButton(btnExportPdfStokKeluar);

        btnSimpanStokKeluar.addActionListener(e -> simpanStokKeluar());
        btnClearStokKeluar.addActionListener(e -> clearFormStokKeluar());
        btnCariStokKeluar.addActionListener(e -> cariRiwayatStokKeluar());
        btnExportPdfStokKeluar.addActionListener(e -> exportStokKeluarPdf());

        txtSearchStokKeluar.addActionListener(e -> cariRiwayatStokKeluar());
    }

    private void loadComboBarangKeluar() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        try {
            List<Barang> listBarang = barangDAO.getAllBarang();

            for (Barang barang : listBarang) {
                model.addElement(
                        barang.getKodeBarang() + " - " + barang.getNamaBarang()
                );
            }

            cmbBarangKeluar.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal load barang keluar: " + e.getMessage()
            );
        }
    }

    private String getKodeBarangKeluarDipilih() {
        Object selected = cmbBarangKeluar.getSelectedItem();

        if (selected == null) {
            return null;
        }

        String item = selected.toString();

        if (!item.contains(" - ")) {
            return null;
        }

        return item.split(" - ")[0];
    }

    private void loadRiwayatStokKeluar() {
        List<StokKeluar> list = stokKeluarService.getRiwayatStokKeluar();
        tampilkanRiwayatStokKeluar(list);
    }

    private void tampilkanRiwayatStokKeluar(List<StokKeluar> list) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("No");
        model.addColumn("Tanggal");
        model.addColumn("Kode");
        model.addColumn("Nama Barang");
        model.addColumn("Jumlah");
        model.addColumn("Tujuan");

        int no = 1;

        for (StokKeluar sk : list) {
            model.addRow(new Object[]{
                no++,
                sk.getTanggalKeluar(),
                sk.getKodeBarang(),
                sk.getNamaBarang(),
                sk.getJumlahKeluar(),
                sk.getDepartemenTujuan()
            });
        }

        tblRiwayatStokKeluar.setModel(model);
        styleTable(tblRiwayatStokKeluar);
    }

    private void simpanStokKeluar() {
        try {
            String kodeBarang = getKodeBarangKeluarDipilih();
            String tujuan = txtTujuanKeluar.getText();
            String jumlahKeluar = txtJumlahKeluar.getText();

            Date tanggal = (Date) spnTanggalKeluar.getValue();
            String tanggalKeluar = dateFormat.format(tanggal);

            boolean berhasil = stokKeluarService.simpanStokKeluar(
                    kodeBarang,
                    tujuan,
                    jumlahKeluar,
                    tanggalKeluar
            );

            if (berhasil) {
                JOptionPane.showMessageDialog(
                        this,
                        "Stok keluar berhasil disimpan"
                );

                clearFormStokKeluar();
                loadRiwayatStokKeluar();

                // Refresh dashboard karena stok dan total stok keluar berubah
                loadDashboardData();

                // Refresh combo barang masuk/keluar supaya stok terbaru ikut terbaca
                loadComboBarangMasuk();
                loadComboBarangKeluar();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }

    private void clearFormStokKeluar() {
        if (cmbBarangKeluar.getItemCount() > 0) {
            cmbBarangKeluar.setSelectedIndex(0);
        }

        txtTujuanKeluar.setText("");
        txtJumlahKeluar.setText("");
        spnTanggalKeluar.setValue(new Date());

        txtTujuanKeluar.requestFocus();
    }

    private void cariRiwayatStokKeluar() {
        try {
            String keyword = txtSearchStokKeluar.getText();

            List<StokKeluar> list
                    = stokKeluarService.searchRiwayatStokKeluar(keyword);

            tampilkanRiwayatStokKeluar(list);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal mencari riwayat stok keluar: " + e.getMessage()
            );
        }
    }

    private void exportStokKeluarPdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan Stok Keluar");
        chooser.setSelectedFile(new File("laporan_stok_keluar.pdf"));

        int result = chooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getAbsolutePath() + ".pdf");
        }

        try {
            Document document = new Document(PageSize.A4.rotate());

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(file)
            );

            document.open();

            org.openpdf.text.Font titleFont
                    = org.openpdf.text.FontFactory.getFont(
                            org.openpdf.text.FontFactory.HELVETICA_BOLD,
                            16
                    );

            document.add(new Paragraph("Laporan Stok Keluar", titleFont));
            document.add(new Paragraph("Tanggal Cetak: " + dateFormat.format(new Date())));
            document.add(new Paragraph(" "));

            PdfPTable pdfTable
                    = new PdfPTable(tblRiwayatStokKeluar.getColumnCount());

            pdfTable.setWidthPercentage(100);

            for (int i = 0; i < tblRiwayatStokKeluar.getColumnCount(); i++) {
                pdfTable.addCell(new Phrase(
                        tblRiwayatStokKeluar.getColumnName(i)
                ));
            }

            for (int row = 0; row < tblRiwayatStokKeluar.getRowCount(); row++) {
                for (int col = 0; col < tblRiwayatStokKeluar.getColumnCount(); col++) {
                    Object value = tblRiwayatStokKeluar.getValueAt(row, col);

                    pdfTable.addCell(
                            value == null ? "" : value.toString()
                    );
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(
                    this,
                    "PDF berhasil dibuat:\n" + file.getAbsolutePath()
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal export PDF stok keluar: " + e.getMessage()
            );
        }
    }

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();

        cardLayout = (CardLayout) contentPanel.getLayout();
        showPage("dashboard");

        setupSidebarButtonText();
        setupModernFrame();
        setupModernSidebar();
        setupModernTables();
        setupDashboardCards();
        setupModernPanels();
        setupPageBackgrounds();
        setupModernButtons();

        setupModernInputs();
        setupDashboardSearch();
        loadDashboardData();

        setupStokMasukComponents();
        loadComboBarangMasuk();
        loadRiwayatStokMasuk();

        setupStokKeluarComponents();
        loadComboBarangKeluar();
        loadRiwayatStokKeluar();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        stokMasukPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtSupplierMasuk = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtJumlahMasuk = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        btnSimpanStokMasuk = new javax.swing.JButton();
        btnClearStokMasuk = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        cmbBarangMasuk = new javax.swing.JComboBox<>();
        spnTanggalMasuk = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtSearchStokMasuk = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        btnExportPdfStokMasuk = new javax.swing.JButton();
        btnCariStokMasuk = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRiwayatStokMasuk = new javax.swing.JTable();
        spnSampaiTanggalMasuk = new javax.swing.JSpinner();
        spnDariTanggalMasuk = new javax.swing.JSpinner();
        stokKeluarPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtTujuanKeluar = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtJumlahKeluar = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        btnClearStokKeluar = new javax.swing.JButton();
        btnSimpanStokKeluar = new javax.swing.JButton();
        cmbBarangKeluar = new javax.swing.JComboBox<>();
        spnTanggalKeluar = new javax.swing.JSpinner();
        jPanel9 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtSearchStokKeluar = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        btnExportPdfStokKeluar = new javax.swing.JButton();
        btnCariStokKeluar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblRiwayatStokKeluar = new javax.swing.JTable();
        spnDariTanggalKeluar = new javax.swing.JSpinner();
        spnSampaiTanggalKeluar = new javax.swing.JSpinner();
        dashboardPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelChartKategori = new javax.swing.JPanel();
        panelChartStatusStok = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();
        cardTotalBarang = new javax.swing.JPanel();
        lblTitleTotalBarang = new javax.swing.JLabel();
        lblValueTotalBarang = new javax.swing.JLabel();
        lblInfoTotalBarang = new javax.swing.JLabel();
        cardKategori = new javax.swing.JPanel();
        lblTitleKategori = new javax.swing.JLabel();
        lblValueKategori = new javax.swing.JLabel();
        lblInfoKategori = new javax.swing.JLabel();
        cardStokMasuk = new javax.swing.JPanel();
        lblTitleStokMasuk = new javax.swing.JLabel();
        lblValueStokMasuk = new javax.swing.JLabel();
        lblInfoStokMasuk = new javax.swing.JLabel();
        cardStokKeluar = new javax.swing.JPanel();
        lblTitleStokKeluar = new javax.swing.JLabel();
        lblValueStokKeluar = new javax.swing.JLabel();
        lblInfoStokKeluar = new javax.swing.JLabel();
        cardStokKritis = new javax.swing.JPanel();
        lblTitleStokKritis = new javax.swing.JLabel();
        lblValueStokKritis = new javax.swing.JLabel();
        lblInfoStokKritis = new javax.swing.JLabel();
        barangPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        sidebarPanel = new javax.swing.JPanel();
        btnDashboard = new javax.swing.JButton();
        lblAppName = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        btnBarang = new javax.swing.JButton();
        btnStokMasuk = new javax.swing.JButton();
        btnStokKeluar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 700));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setToolTipText("");
        jPanel1.setPreferredSize(new java.awt.Dimension(1327, 60));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Logout");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("|");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Admin");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Smart Inventory Management");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 929, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4))
                    .addComponent(jLabel1))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        contentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        contentPanel.setPreferredSize(new java.awt.Dimension(220, 673));
        contentPanel.setLayout(new java.awt.CardLayout());

        jPanel5.setOpaque(false);

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel24.setText("STOK MASUK");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel24)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel24)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jLabel25.setText("Barang");

        jLabel26.setText("Supplier");

        jLabel27.setText("Jumlah");

        jLabel28.setText("Tanggal");

        btnSimpanStokMasuk.setText("Simpan");

        btnClearStokMasuk.setText("Clear");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("FORM STOK MASUK");

        cmbBarangMasuk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnSimpanStokMasuk)
                        .addGap(18, 18, 18)
                        .addComponent(btnClearStokMasuk))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel26)
                                    .addGap(30, 30, 30))
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel25)
                                    .addGap(36, 36, 36)))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtSupplierMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addComponent(cmbBarangMasuk, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel27)
                                .addComponent(jLabel28))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtJumlahMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addComponent(spnTanggalMasuk)))))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel29)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(cmbBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtSupplierMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtJumlahMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(spnTanggalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanStokMasuk)
                    .addComponent(btnClearStokMasuk))
                .addContainerGap(292, Short.MAX_VALUE))
        );

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setText("RIWAYAT STOK MASUK");

        jLabel31.setText("Search");

        jLabel32.setText("Dari Tanggal");

        jLabel33.setText("Sampai Tanggal");

        btnExportPdfStokMasuk.setText("Cetak PDF");
        btnExportPdfStokMasuk.addActionListener(this::btnExportPdfStokMasukActionPerformed);

        btnCariStokMasuk.setText("Cari");

        tblRiwayatStokMasuk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblRiwayatStokMasuk);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnExportPdfStokMasuk)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel30)
                                        .addGap(448, 448, 448))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel31)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtSearchStokMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel32)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spnDariTanggalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel33)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(spnSampaiTanggalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addComponent(btnCariStokMasuk)))
                        .addGap(0, 141, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel30)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtSearchStokMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(btnCariStokMasuk)
                    .addComponent(spnDariTanggalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnSampaiTanggalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(btnExportPdfStokMasuk)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout stokMasukPanelLayout = new javax.swing.GroupLayout(stokMasukPanel);
        stokMasukPanel.setLayout(stokMasukPanelLayout);
        stokMasukPanelLayout.setHorizontalGroup(
            stokMasukPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stokMasukPanelLayout.createSequentialGroup()
                .addGroup(stokMasukPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(stokMasukPanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        stokMasukPanelLayout.setVerticalGroup(
            stokMasukPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stokMasukPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stokMasukPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 672, Short.MAX_VALUE))
        );

        contentPanel.add(stokMasukPanel, "stokMasuk");

        stokKeluarPanel.setOpaque(false);

        jPanel3.setOpaque(false);

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel35.setText("STOK KELUAR");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel35)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel35)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel8.setPreferredSize(new java.awt.Dimension(326, 318));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setText("FORM STOK KELUAR");

        jLabel36.setText("Barang");

        jLabel37.setText("Tujuan");

        jLabel38.setText("Jumlah");

        jLabel39.setText("Tanggal");

        btnClearStokKeluar.setText("Clear");

        btnSimpanStokKeluar.setText("Simpan");

        cmbBarangKeluar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cmbBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(btnSimpanStokKeluar)
                                .addGap(18, 18, 18)
                                .addComponent(btnClearStokKeluar))
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(jLabel37)
                                    .addGap(30, 30, 30)
                                    .addComponent(txtTujuanKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel38)
                                        .addComponent(jLabel39))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtJumlahKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                        .addComponent(spnTanggalKeluar))))
                            .addComponent(jLabel34))))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(cmbBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(txtTujuanKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(txtJumlahKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(spnTanggalKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanStokKeluar)
                    .addComponent(btnClearStokKeluar))
                .addContainerGap(311, Short.MAX_VALUE))
        );

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel40.setText("RIWAYAT STOK KELUAR");

        jLabel41.setText("Search");

        jLabel42.setText("Dari Tanggal");

        jLabel43.setText("Sampai Tanggal");

        btnExportPdfStokKeluar.setText("Cetak PDF");
        btnExportPdfStokKeluar.addActionListener(this::btnExportPdfStokKeluarActionPerformed);

        btnCariStokKeluar.setText("Cari");

        tblRiwayatStokKeluar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblRiwayatStokKeluar);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnExportPdfStokKeluar)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel41)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtSearchStokKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel42)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spnDariTanggalKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel43)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spnSampaiTanggalKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCariStokKeluar)))
                        .addGap(0, 139, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel40)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txtSearchStokKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel43)
                    .addComponent(btnCariStokKeluar)
                    .addComponent(spnDariTanggalKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnSampaiTanggalKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(btnExportPdfStokKeluar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout stokKeluarPanelLayout = new javax.swing.GroupLayout(stokKeluarPanel);
        stokKeluarPanel.setLayout(stokKeluarPanelLayout);
        stokKeluarPanelLayout.setHorizontalGroup(
            stokKeluarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stokKeluarPanelLayout.createSequentialGroup()
                .addGroup(stokKeluarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(stokKeluarPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(stokKeluarPanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        stokKeluarPanelLayout.setVerticalGroup(
            stokKeluarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stokKeluarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stokKeluarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
                .addContainerGap(661, Short.MAX_VALUE))
        );

        contentPanel.add(stokKeluarPanel, "stokKeluar");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("DASHBOARD");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Barang dengan Stok Kritis");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Cari");

        jScrollPane1.setBorder(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Nama Barang", "Kategori", "Stok", "Minimum", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        panelChartKategori.setPreferredSize(new java.awt.Dimension(660, 280));

        javax.swing.GroupLayout panelChartKategoriLayout = new javax.swing.GroupLayout(panelChartKategori);
        panelChartKategori.setLayout(panelChartKategoriLayout);
        panelChartKategoriLayout.setHorizontalGroup(
            panelChartKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );
        panelChartKategoriLayout.setVerticalGroup(
            panelChartKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        panelChartStatusStok.setPreferredSize(new java.awt.Dimension(420, 280));

        javax.swing.GroupLayout panelChartStatusStokLayout = new javax.swing.GroupLayout(panelChartStatusStok);
        panelChartStatusStok.setLayout(panelChartStatusStokLayout);
        panelChartStatusStokLayout.setHorizontalGroup(
            panelChartStatusStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );
        panelChartStatusStokLayout.setVerticalGroup(
            panelChartStatusStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );

        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new java.awt.Dimension(1136, 130));

        cardTotalBarang.setPreferredSize(new java.awt.Dimension(190, 110));

        lblTitleTotalBarang.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTitleTotalBarang.setText("Total Barang");

        lblValueTotalBarang.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblValueTotalBarang.setText("120");

        lblInfoTotalBarang.setText("Data inventory");

        javax.swing.GroupLayout cardTotalBarangLayout = new javax.swing.GroupLayout(cardTotalBarang);
        cardTotalBarang.setLayout(cardTotalBarangLayout);
        cardTotalBarangLayout.setHorizontalGroup(
            cardTotalBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTotalBarangLayout.createSequentialGroup()
                .addGroup(cardTotalBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardTotalBarangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitleTotalBarang))
                    .addGroup(cardTotalBarangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInfoTotalBarang))
                    .addGroup(cardTotalBarangLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(lblValueTotalBarang)))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        cardTotalBarangLayout.setVerticalGroup(
            cardTotalBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTotalBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleTotalBarang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValueTotalBarang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfoTotalBarang)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardKategori.setPreferredSize(new java.awt.Dimension(190, 110));

        lblTitleKategori.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTitleKategori.setText("Kategori");

        lblValueKategori.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblValueKategori.setText("5");

        lblInfoKategori.setText("Jenis Barang");

        javax.swing.GroupLayout cardKategoriLayout = new javax.swing.GroupLayout(cardKategori);
        cardKategori.setLayout(cardKategoriLayout);
        cardKategoriLayout.setHorizontalGroup(
            cardKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardKategoriLayout.createSequentialGroup()
                .addGroup(cardKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardKategoriLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitleKategori))
                    .addGroup(cardKategoriLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInfoKategori))
                    .addGroup(cardKategoriLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(lblValueKategori)))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        cardKategoriLayout.setVerticalGroup(
            cardKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardKategoriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleKategori)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValueKategori)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfoKategori)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardStokMasuk.setPreferredSize(new java.awt.Dimension(190, 110));

        lblTitleStokMasuk.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTitleStokMasuk.setText("Stok Masuk");

        lblValueStokMasuk.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblValueStokMasuk.setText("35");

        lblInfoStokMasuk.setText("Transaksi Masuk");

        javax.swing.GroupLayout cardStokMasukLayout = new javax.swing.GroupLayout(cardStokMasuk);
        cardStokMasuk.setLayout(cardStokMasukLayout);
        cardStokMasukLayout.setHorizontalGroup(
            cardStokMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardStokMasukLayout.createSequentialGroup()
                .addGroup(cardStokMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardStokMasukLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitleStokMasuk))
                    .addGroup(cardStokMasukLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInfoStokMasuk))
                    .addGroup(cardStokMasukLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(lblValueStokMasuk)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        cardStokMasukLayout.setVerticalGroup(
            cardStokMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardStokMasukLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleStokMasuk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValueStokMasuk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfoStokMasuk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardStokKeluar.setPreferredSize(new java.awt.Dimension(190, 110));

        lblTitleStokKeluar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTitleStokKeluar.setText("Stok Keluar");

        lblValueStokKeluar.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblValueStokKeluar.setText("20");

        lblInfoStokKeluar.setText("Transaksi Keluar");

        javax.swing.GroupLayout cardStokKeluarLayout = new javax.swing.GroupLayout(cardStokKeluar);
        cardStokKeluar.setLayout(cardStokKeluarLayout);
        cardStokKeluarLayout.setHorizontalGroup(
            cardStokKeluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardStokKeluarLayout.createSequentialGroup()
                .addGroup(cardStokKeluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardStokKeluarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitleStokKeluar))
                    .addGroup(cardStokKeluarLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(lblValueStokKeluar))
                    .addGroup(cardStokKeluarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInfoStokKeluar)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        cardStokKeluarLayout.setVerticalGroup(
            cardStokKeluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardStokKeluarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleStokKeluar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValueStokKeluar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfoStokKeluar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardStokKritis.setPreferredSize(new java.awt.Dimension(190, 110));

        lblTitleStokKritis.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTitleStokKritis.setText("Stok Kritis");

        lblValueStokKritis.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblValueStokKritis.setText("20");

        lblInfoStokKritis.setText("Perlu Perhatian");

        javax.swing.GroupLayout cardStokKritisLayout = new javax.swing.GroupLayout(cardStokKritis);
        cardStokKritis.setLayout(cardStokKritisLayout);
        cardStokKritisLayout.setHorizontalGroup(
            cardStokKritisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardStokKritisLayout.createSequentialGroup()
                .addGroup(cardStokKritisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardStokKritisLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(lblValueStokKritis))
                    .addGroup(cardStokKritisLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInfoStokKritis))
                    .addGroup(cardStokKritisLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitleStokKritis)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        cardStokKritisLayout.setVerticalGroup(
            cardStokKritisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardStokKritisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleStokKritis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValueStokKritis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfoStokKritis)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsPanelLayout = new javax.swing.GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardTotalBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(cardKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(cardStokMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(cardStokKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(cardStokKritis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        statsPanelLayout.setVerticalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardStokKritis, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                    .addComponent(cardStokKeluar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                    .addComponent(cardStokMasuk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                    .addComponent(cardTotalBarang, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                    .addComponent(cardKategori, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, dashboardPanelLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addComponent(panelChartKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(panelChartStatusStok, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addComponent(statsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(statsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelChartStatusStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelChartKategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        contentPanel.add(dashboardPanel, "dashboard");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("MANAJEMEN BARANG");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("FORM BARANG");

        jLabel15.setText("Kode Barang");

        jTextField2.setText("jTextField2");

        jLabel16.setText("Nama Barang");

        jTextField3.setText("jTextField2");

        jLabel17.setText("Kategori");

        jTextField4.setText("jTextField2");

        jLabel18.setText("Stok Awal");

        jTextField5.setText("jTextField2");

        jLabel19.setText("Stok Minimum");

        jTextField6.setText("jTextField2");

        jTextField7.setText("jTextField2");

        jLabel20.setText("Satuan");

        jButton1.setText("Tambah");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Update");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Hapus");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setText("Clear");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel14)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(19, 19, 19)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4)
                            .addComponent(jButton2))
                        .addGap(100, 100, 100)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(610, Short.MAX_VALUE))
        );

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("DAFTAR BARANG");

        jTextField8.setText("jTextField2");

        jLabel22.setText("Search");

        jLabel23.setText("Kategori");

        jTextField9.setText("jTextField2");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addGap(55, 55, 55)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(jLabel23)
                                .addGap(55, 55, 55)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel21))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(513, 513, 513))
        );

        javax.swing.GroupLayout barangPanelLayout = new javax.swing.GroupLayout(barangPanel);
        barangPanel.setLayout(barangPanelLayout);
        barangPanelLayout.setHorizontalGroup(
            barangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barangPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(barangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(barangPanelLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(0, 998, Short.MAX_VALUE))
                    .addGroup(barangPanelLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        barangPanelLayout.setVerticalGroup(
            barangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barangPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel13)
                .addGap(31, 31, 31)
                .addGroup(barangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        contentPanel.add(barangPanel, "barang");

        sidebarPanel.setBackground(new java.awt.Color(255, 255, 255));
        sidebarPanel.setPreferredSize(new java.awt.Dimension(220, 0));

        btnDashboard.setBackground(new java.awt.Color(255, 255, 255));
        btnDashboard.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(71, 85, 105));
        btnDashboard.setText("Dashboard");
        btnDashboard.setBorder(null);
        btnDashboard.setFocusPainted(false);
        btnDashboard.setOpaque(true);
        btnDashboard.setPreferredSize(new java.awt.Dimension(170, 42));
        btnDashboard.addActionListener(this::btnDashboardActionPerformed);

        lblAppName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblAppName.setForeground(new java.awt.Color(5, 150, 105));
        lblAppName.setText("Gudang Digital");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(107, 114, 128));
        jLabel44.setText("MENU");

        btnBarang.setBackground(new java.awt.Color(255, 255, 255));
        btnBarang.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnBarang.setForeground(new java.awt.Color(71, 85, 105));
        btnBarang.setText("Barang");
        btnBarang.setBorder(null);
        btnBarang.setFocusPainted(false);
        btnBarang.setOpaque(true);
        btnBarang.setPreferredSize(new java.awt.Dimension(170, 42));
        btnBarang.addActionListener(this::btnBarangActionPerformed);

        btnStokMasuk.setBackground(new java.awt.Color(255, 255, 255));
        btnStokMasuk.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnStokMasuk.setForeground(new java.awt.Color(71, 85, 105));
        btnStokMasuk.setText("Stok Masuk");
        btnStokMasuk.setBorder(null);
        btnStokMasuk.setFocusPainted(false);
        btnStokMasuk.setOpaque(true);
        btnStokMasuk.setPreferredSize(new java.awt.Dimension(170, 42));
        btnStokMasuk.addActionListener(this::btnStokMasukActionPerformed);

        btnStokKeluar.setBackground(new java.awt.Color(255, 255, 255));
        btnStokKeluar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnStokKeluar.setForeground(new java.awt.Color(71, 85, 105));
        btnStokKeluar.setText("Stok Keluar");
        btnStokKeluar.setBorder(null);
        btnStokKeluar.setFocusPainted(false);
        btnStokKeluar.setOpaque(true);
        btnStokKeluar.setPreferredSize(new java.awt.Dimension(170, 42));
        btnStokKeluar.addActionListener(this::btnStokKeluarActionPerformed);

        javax.swing.GroupLayout sidebarPanelLayout = new javax.swing.GroupLayout(sidebarPanel);
        sidebarPanel.setLayout(sidebarPanelLayout);
        sidebarPanelLayout.setHorizontalGroup(
            sidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(sidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStokKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStokMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44)
                    .addComponent(lblAppName)
                    .addComponent(btnDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        sidebarPanelLayout.setVerticalGroup(
            sidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblAppName)
                .addGap(40, 40, 40)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnStokMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnStokKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1440, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1348, Short.MAX_VALUE)
                    .addComponent(sidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1348, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        showPage("dashboard");
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnExportPdfStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportPdfStokMasukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExportPdfStokMasukActionPerformed

    private void btnExportPdfStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportPdfStokKeluarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExportPdfStokKeluarActionPerformed

    private void btnBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarangActionPerformed
        showPage("barang");
    }//GEN-LAST:event_btnBarangActionPerformed

    private void btnStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStokMasukActionPerformed
        showPage("stokMasuk");
    }//GEN-LAST:event_btnStokMasukActionPerformed

    private void btnStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStokKeluarActionPerformed
        showPage("stokKeluar");
    }//GEN-LAST:event_btnStokKeluarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new NewJFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barangPanel;
    private javax.swing.JButton btnBarang;
    private javax.swing.JButton btnCariStokKeluar;
    private javax.swing.JButton btnCariStokMasuk;
    private javax.swing.JButton btnClearStokKeluar;
    private javax.swing.JButton btnClearStokMasuk;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnExportPdfStokKeluar;
    private javax.swing.JButton btnExportPdfStokMasuk;
    private javax.swing.JButton btnSimpanStokKeluar;
    private javax.swing.JButton btnSimpanStokMasuk;
    private javax.swing.JButton btnStokKeluar;
    private javax.swing.JButton btnStokMasuk;
    private javax.swing.JPanel cardKategori;
    private javax.swing.JPanel cardStokKeluar;
    private javax.swing.JPanel cardStokKritis;
    private javax.swing.JPanel cardStokMasuk;
    private javax.swing.JPanel cardTotalBarang;
    private javax.swing.JComboBox<String> cmbBarangKeluar;
    private javax.swing.JComboBox<String> cmbBarangMasuk;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JLabel lblAppName;
    private javax.swing.JLabel lblInfoKategori;
    private javax.swing.JLabel lblInfoStokKeluar;
    private javax.swing.JLabel lblInfoStokKritis;
    private javax.swing.JLabel lblInfoStokMasuk;
    private javax.swing.JLabel lblInfoTotalBarang;
    private javax.swing.JLabel lblTitleKategori;
    private javax.swing.JLabel lblTitleStokKeluar;
    private javax.swing.JLabel lblTitleStokKritis;
    private javax.swing.JLabel lblTitleStokMasuk;
    private javax.swing.JLabel lblTitleTotalBarang;
    private javax.swing.JLabel lblValueKategori;
    private javax.swing.JLabel lblValueStokKeluar;
    private javax.swing.JLabel lblValueStokKritis;
    private javax.swing.JLabel lblValueStokMasuk;
    private javax.swing.JLabel lblValueTotalBarang;
    private javax.swing.JPanel panelChartKategori;
    private javax.swing.JPanel panelChartStatusStok;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JSpinner spnDariTanggalKeluar;
    private javax.swing.JSpinner spnDariTanggalMasuk;
    private javax.swing.JSpinner spnSampaiTanggalKeluar;
    private javax.swing.JSpinner spnSampaiTanggalMasuk;
    private javax.swing.JSpinner spnTanggalKeluar;
    private javax.swing.JSpinner spnTanggalMasuk;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JPanel stokKeluarPanel;
    private javax.swing.JPanel stokMasukPanel;
    private javax.swing.JTable tblRiwayatStokKeluar;
    private javax.swing.JTable tblRiwayatStokMasuk;
    private javax.swing.JTextField txtJumlahKeluar;
    private javax.swing.JTextField txtJumlahMasuk;
    private javax.swing.JTextField txtSearchStokKeluar;
    private javax.swing.JTextField txtSearchStokMasuk;
    private javax.swing.JTextField txtSupplierMasuk;
    private javax.swing.JTextField txtTujuanKeluar;
    // End of variables declaration//GEN-END:variables
}
