package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;

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
    private final BarangApiClient mahasiswaApiClient = new BarangApiClient();

    private List<Barang> allMahasiswa = new ArrayList<>();
    private List<Barang> displayedBarang = new ArrayList<>();

    private WebSocketClientHandler wsClient;

    public BarangController(BarangFrame frame) {
        this.frame = frame;
        setupEventListeners();
        setupWebSocket();
        loadAllMahasiswa();
    }

    private void setupWebSocket() {
        try {
            URI uri = new URI("ws://localhost:3000");
            wsClient = new WebSocketClientHandler(uri, new Consumer<String>() {
                @Override
                public void accept(String message) {
                    System.out.println("Received real-time update: " + message);
                    handleWebSocketMessage(message);
                }
            });
            wsClient.connect();
        } catch (URISyntaxException e) {
            JOptionPane.showMessageDialog(frame, "Failed to connect to real-time server: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleWebSocketMessage(String message) {
        // Parse JSON jika dibutuhkan, untuk kasus ini setiap message yang diterima akan men-trigger loadAllMahasiswa
        
        // Run refresh di Swing thread (EDT)
        SwingUtilities.invokeLater(() -> loadAllMahasiswa());
    }

    private void setupEventListeners() {
        frame.getAddButton().addActionListener(e -> openMahasiswaDialog(null));
        frame.getRefreshButton().addActionListener(e -> loadAllMahasiswa());
        frame.getDeleteButton().addActionListener(e -> deleteSelectedMahasiswa());
        frame.getMahasiswaTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = frame.getMahasiswaTable().getSelectedRow();
                    if (selectedRow >= 0) {
                        openMahasiswaDialog(displayedBarang.get(selectedRow));
                    }
                }
            }
        });
        frame.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applySearchFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applySearchFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applySearchFilter();
            }

            private void applySearchFilter() {
                String keyword = frame.getSearchField().getText().toLowerCase().trim();
                displayedBarang = new ArrayList<>();
                for (Barang mahasiswa : allMahasiswa) {
                    if (mahasiswa.getKategori().toLowerCase().contains(keyword) ||
                            mahasiswa.getNama().toLowerCase().contains(keyword) ||
                            (mahasiswa.getJumlah() != null
                                    && mahasiswa.getJumlah().toLowerCase().contains(keyword))) {
                        displayedBarang.add(mahasiswa);
                    }
                }
                frame.getMahasiswaTableModel().setBarangList(displayedBarang);
                updateTotalRecordsLabel();
            }
        });
    }

    private void openMahasiswaDialog(Barang mahasiswaToEdit) {
        BarangDialog dialog;
        if (mahasiswaToEdit == null) {
            dialog = new BarangDialog(frame);
        } else {
            dialog = new BarangDialog(frame, mahasiswaToEdit);
        }
        dialog.getSaveButton().addActionListener(e -> {
            Barang mahasiswa = dialog.getBarang();
            SwingWorker<Void, Void> worker;
            if (mahasiswaToEdit == null) {
                worker = new SaveBarangWorker(frame, mahasiswaApiClient, mahasiswa);
            } else {
                worker = new UpdateBarangWorker(frame, mahasiswaApiClient, mahasiswa);
            }
            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    dialog.dispose();
                    loadAllMahasiswa();
                }
            });
            worker.execute();
        });
        dialog.setVisible(true);
    }

    private void deleteSelectedMahasiswa() {
        int selectedRow = frame.getMahasiswaTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a record to delete.");
            return;
        }
        Barang barang = displayedBarang.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Delete mahasiswa: " + barang.getKategori() + " - " + barang.getNama() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            DeleteBarangWorker worker = new DeleteBarangWorker(frame, mahasiswaApiClient, barang);
            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    loadAllMahasiswa();
                }
            });
            worker.execute();
        }
    }

    private void loadAllMahasiswa() {
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Loading data...");
        LoadBarangWorker worker = new LoadBarangWorker(frame, mahasiswaApiClient);
        worker.addPropertyChangeListener(evt -> {
            if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                try {
                    allMahasiswa = worker.get();
                    displayedBarang = new ArrayList<>(allMahasiswa);
                    frame.getMahasiswaTableModel().setBarangList(displayedBarang);
                    updateTotalRecordsLabel();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to load data.");
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