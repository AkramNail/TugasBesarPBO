package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Barang;
import net.miginfocom.swing.MigLayout;

public class BarangDialog extends JDialog {

    private final JTextField idField = new JTextField(15);
    private final JTextField namaField = new JTextField(20);
    private final JTextField kategoriField = new JTextField(20);
    private final JTextField jumlahField = new JTextField(20);
    private final JTextField waktuField = new JTextField(20); // ⬅️ FIELD BARU

    private final JButton saveButton = new JButton("Simpan");
    private final JButton cancelButton = new JButton("Batal");

    private Barang barang;

    public BarangDialog(JFrame owner) {
        super(owner, "Tambah Barang", true);
        barang = new Barang();
        initUI();

        // ⬅️ ISI WAKTU SAAT TAMBAH
        waktuField.setText(getWaktuSekarang());
    }

    public BarangDialog(JFrame owner, Barang barangToEdit) {
        super(owner, "Ubah Barang", true);
        barang = barangToEdit;
        initUI();

        idField.setText(String.valueOf(barangToEdit.getId()));
        idField.setEnabled(false);

        namaField.setText(barangToEdit.getNama());
        kategoriField.setText(barangToEdit.getKategori());
        jumlahField.setText(barangToEdit.getJumlah());
        waktuField.setText(barangToEdit.getWaktu()); // ⬅️ TAMPILKAN WAKTU LAMA
    }

    private void initUI() {
        setLayout(new MigLayout("fill", "[grow]", "[]15[]15[]"));
        getContentPane().setBackground(new Color(240, 242, 245));

        add(createHeader(), "growx, wrap");
        add(createFormCard(), "growx, wrap");
        add(createButtonBar(), "right");

        setMinimumSize(new Dimension(520, 520)); // ⬅️ DITINGGIKAN
        setLocationRelativeTo(getOwner());
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.setBackground(new Color(30, 41, 59));

        JLabel title = new JLabel(getTitle());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        panel.add(title);
        return panel;
    }

    private JPanel createFormCard() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 20", "[][grow]"));
        panel.setBackground(Color.WHITE);

        styleField(idField);
        styleField(namaField);
        styleField(kategoriField);
        styleField(jumlahField);
        styleField(waktuField);

        waktuField.setEditable(false); // ⬅️ READ ONLY
        waktuField.setBackground(new Color(243, 244, 246));

        panel.add(new JLabel("ID Barang"));
        panel.add(idField, "growx, wrap");

        panel.add(new JLabel("Nama Barang"));
        panel.add(namaField, "growx, wrap");

        panel.add(new JLabel("Kategori"));
        panel.add(kategoriField, "growx, wrap");

        panel.add(new JLabel("Jumlah"));
        panel.add(jumlahField, "growx, wrap");

        panel.add(new JLabel("Waktu"));
        panel.add(waktuField, "growx"); // ⬅️ BARU

        return panel;
    }

    private JPanel createButtonBar() {
        JPanel panel = new JPanel(new MigLayout("", "[]10[]"));
        panel.setOpaque(false);

        styleCancelButton(cancelButton);
        styleSaveButton(saveButton);

        cancelButton.addActionListener(e -> dispose());

        panel.add(cancelButton);
        panel.add(saveButton);
        return panel;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 32));
    }

    private void styleSaveButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(59, 130, 246));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void styleCancelButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(new Color(203, 213, 225));
        button.setFocusPainted(false);
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public Barang getBarang() {
        barang.setId(Integer.parseInt(idField.getText().trim()));
        barang.setNama(namaField.getText().trim());
        barang.setKategori(kategoriField.getText().trim());
        barang.setJumlah(jumlahField.getText().trim());
        barang.setWaktu(waktuField.getText().trim()); // ⬅️ SIMPAN WAKTU
        return barang;
    }

    // ===================== WAKTU REALTIME =====================
    private String getWaktuSekarang() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
