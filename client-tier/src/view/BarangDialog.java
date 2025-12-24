package view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import model.Barang;
import net.miginfocom.swing.MigLayout;

public class BarangDialog extends JDialog {

    private final JTextField kategoriField = new JTextField(25);
    private final JTextField namaField = new JTextField(25);
    private final JTextField jumlahField = new JTextField(25);
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");

    private Barang barang;

    public BarangDialog(JFrame owner) {
        super(owner, "Add New barang", true);
        this.barang = new Barang();
        setupComponents();
    }

    public BarangDialog(JFrame owner, Barang barangToEdit) {
        super(owner, "Edit barang", true);
        this.barang = barangToEdit;
        setupComponents();
        
        kategoriField.setText(barangToEdit.getKategori());
        namaField.setText(barangToEdit.getNama());
        jumlahField.setText(barangToEdit.getJumlah());
    }

    private void setupComponents() {
        setLayout(new MigLayout("fill, insets 30", "[right]20[grow]"));
        add(new JLabel("NIM"), "");
        add(kategoriField, "growx, wrap");
        add(new JLabel("Nama Lengkap"), "");
        add(namaField, "growx, wrap");
        add(new JLabel("Jurusan"), "");
        add(jumlahField, "growx, wrap");

        saveButton.setBackground(UIManager.getColor("Button.default.background"));
        saveButton.setForeground(UIManager.getColor("Button.default.foreground"));
        saveButton.setFont(saveButton.getFont().deriveFont(Font.BOLD));

        JPanel buttonPanel = new JPanel(new MigLayout("", "[]10[]"));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, "span, right");

        pack();
        setMinimumSize(new Dimension(500, 400));
        setLocationRelativeTo(getOwner());
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public Barang getBarang() {
        barang.setKategori(kategoriField.getText().trim());
        barang.setNama(namaField.getText().trim());
        barang.setJumlah(jumlahField.getText().trim());
        return barang;
    }
}