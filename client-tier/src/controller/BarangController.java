package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import api.BarangApiClient;
import api.WebSocketClientHandler;
import model.Barang;
import view.BarangDialog;
import view.BarangFrame;
import worker.barang.DeleteBarangWorker;
import worker.barang.LoadBarangWorker;
import worker.barang.SaveBarangWorker;
import worker.barang.UpdateBarangWorker;

public class BarangController {

    private final BarangFrame frame;
    private final BarangApiClient barangApiClient = new BarangApiClient();

    private List<Barang> allBarang = new ArrayList<>();
    private List<Barang> displayedBarang = new ArrayList<>();

    private WebSocketClientHandler wsClient;

    public BarangController(BarangFrame frame) {
        this.frame = frame;
        setupEventListeners();
        setupWebSocket();
        loadAllBarang();
    }

    // ===================== WAKTU REALTIME =====================
    private String getWaktuSekarang() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    // ==========================================================

    private void setupWebSocket() {
        try {
            URI uri = new URI("ws://localhost:3000");
            wsClient = new WebSocketClientHandler(uri, new Consumer<String>() {
                @Override
                public void accept(String message) {
                    handleWebSocketMessage(message);
                }
            });
            wsClient.connect();
        } catch (URISyntaxException e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Gagal koneksi real time\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleWebSocketMessage(String message) {
        SwingUtilities.invokeLater(() -> loadAllBarang());
    }

    private void setupEventListeners() {

        frame.getAddButton().addActionListener(e -> openBarangDialog(null));
        frame.getRefreshButton().addActionListener(e -> loadAllBarang());
        frame.getDeleteButton().addActionListener(e -> deleteSelectedBarang());
        frame.getUpdateButton().addActionListener(e -> {
            int row = frame.getBarangTable().getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Pilih data barang dulu");
                return;
            }

            Barang barang = displayedBarang.get(row);
            openBarangDialog(barang);
        });

        frame.getBarangTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = frame.getBarangTable().getSelectedRow();
                    if (row >= 0) {
                        openBarangDialog(displayedBarang.get(row));
                    }
                }
            }
        });

        frame.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }

            private void applyFilter() {
                String keyword = frame.getSearchField().getText().toLowerCase().trim();
                displayedBarang = new ArrayList<>();

                for (Barang barang : allBarang) {
                    if (barang.getKategori().toLowerCase().contains(keyword)
                            || barang.getNama().toLowerCase().contains(keyword)
                            || barang.getJumlah() != null
                            && barang.getJumlah().toLowerCase().contains(keyword)) {
                        displayedBarang.add(barang);
                    }
                }

                frame.getBarangTableModel().setBarangList(displayedBarang);
                updateTotalRecordsLabel();
            }
        });
    }

    private void openBarangDialog(Barang barangToEdit) {

        BarangDialog dialog = barangToEdit == null
                ? new BarangDialog(frame)
                : new BarangDialog(frame, barangToEdit);

        dialog.getSaveButton().addActionListener(e -> {

            Barang barang = dialog.getBarang();

            // ⬅️ SET WAKTU SAAT SIMPAN / UPDATE
            barang.setWaktu(getWaktuSekarang());

            SwingWorker<Void, Void> worker;

            if (barangToEdit == null) {
                worker = new SaveBarangWorker(frame, barangApiClient, barang);
            } else {
                worker = new UpdateBarangWorker(frame, barangApiClient, barang);
            }

            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    dialog.dispose();
                    loadAllBarang();
                }
            });

            worker.execute();
        });

        dialog.setVisible(true);
    }

    private void deleteSelectedBarang() {

        int row = frame.getBarangTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Pilih data barang yang akan dihapus"
            );
            return;
        }

        Barang barang = displayedBarang.get(row);

        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Hapus barang:\n" + barang.getKategori() + " - " + barang.getNama(),
                "Hapus Data Barang",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DeleteBarangWorker worker =
                    new DeleteBarangWorker(frame, barangApiClient, barang);

            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    loadAllBarang();
                }
            });

            worker.execute();
        }
    }

    private void loadAllBarang() {

        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Loading data");

        LoadBarangWorker worker = new LoadBarangWorker(frame, barangApiClient);

        worker.addPropertyChangeListener(evt -> {
            if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                try {
                    allBarang = worker.get();

                    // ⬅️ ISI WAKTU UNTUK DATA YANG DILOAD
                    for (Barang barang : allBarang) {
                        if (barang.getWaktu() == null) {
                            barang.setWaktu(getWaktuSekarang());
                        }
                    }

                    displayedBarang = new ArrayList<>(allBarang);
                    frame.getBarangTableModel().setBarangList(displayedBarang);
                    updateTotalRecordsLabel();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Gagal memuat data");
                } finally {
                    frame.getProgressBar().setIndeterminate(false);
                    frame.getProgressBar().setString("Ready");
                }
            }
        });

        worker.execute();
    }

    private void updateTotalRecordsLabel() {
        frame.getTotalRecordsLabel().setText(displayedBarang.size() + " Records");
    }
}
