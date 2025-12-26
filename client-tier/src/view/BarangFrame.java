package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;
import view.tablemodel.BarangTableModel;

public class BarangFrame extends JFrame {

    private JTextField searchField;
    private JButton addButton;
    private JButton updateButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JLabel totalRecordsLabel;
    private JTable barangTable;
    private BarangTableModel barangTableModel;
    private JProgressBar progressBar;

    public BarangFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Pendataan Barang Gudang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 620));
        setLocationRelativeTo(null);

        setLayout(new MigLayout("fill", "[grow]", "[]20[]20[grow]15[]"));
        getContentPane().setBackground(new Color(236, 239, 241));

        // HEADER
        JPanel header = new JPanel(new MigLayout("fill"));
        header.setBackground(new Color(38, 50, 56));

        JLabel title = new JLabel("Pendataan Barang Gudang");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        header.add(title, "left");
        add(header, "growx, wrap");

        // CONTROL
        JPanel control = new JPanel(new MigLayout("fill, insets 15", "[grow][right]"));
        control.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Cari barang");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(0, 34));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel left = new JPanel(new MigLayout("fill", "[]10[grow]"));
        left.setBackground(Color.WHITE);
        left.add(searchLabel);
        left.add(searchField, "growx");

        addButton = new JButton("Tambah");
        updateButton = new JButton("Ubah");
        refreshButton = new JButton("Muat Ulang");
        deleteButton = new JButton("Hapus");

        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        updateButton.setBackground(new Color(255, 193, 7));
        updateButton.setFocusPainted(false);
        updateButton.setEnabled(false);

        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        refreshButton.setBackground(new Color(189, 189, 189));
        refreshButton.setFocusPainted(false);

        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deleteButton.setBackground(new Color(229, 57, 53));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setEnabled(false);

        JPanel right = new JPanel(new MigLayout("right"));
        right.setBackground(Color.WHITE);
        right.add(addButton);
        right.add(updateButton);
        right.add(refreshButton);
        right.add(deleteButton);

        control.add(left, "growx");
        control.add(right, "right");
        add(control, "growx, wrap");

        // TABLE
        barangTableModel = new BarangTableModel();
        barangTable = new JTable(barangTableModel);
        barangTable.setRowHeight(30);
        barangTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        barangTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        barangTable.setGridColor(new Color(224, 224, 224));
        barangTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        barangTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = barangTable.getSelectedRow() >= 0;
            updateButton.setEnabled(selected);
            deleteButton.setEnabled(selected);
        });

        JPanel tablePanel = new JPanel(new MigLayout("fill"));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(new JScrollPane(barangTable), "grow");

        add(tablePanel, "grow, wrap");

        // FOOTER
        JPanel footer = new JPanel(new MigLayout("fill", "[][grow]"));
        footer.setBackground(new Color(236, 239, 241));

        totalRecordsLabel = new JLabel("Total 0 data");

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(180, 14));
        progressBar.setStringPainted(true);

        footer.add(totalRecordsLabel, "left");
        footer.add(progressBar, "right");

        add(footer, "growx");
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JTable getBarangTable() {
        return barangTable;
    }

    public BarangTableModel getBarangTableModel() {
        return barangTableModel;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getTotalRecordsLabel() {
        return totalRecordsLabel;
    }
}
