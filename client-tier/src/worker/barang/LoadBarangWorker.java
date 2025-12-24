package worker.barang;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.BarangApiClient;
import model.Barang;
import view.BarangFrame;

public class LoadBarangWorker extends SwingWorker<List<Barang>, Void> {
    private final BarangFrame frame;
    private final BarangApiClient mahasiswaApiClient;

    public LoadBarangWorker(BarangFrame frame, BarangApiClient mahasiswaApiClient) {
        this.frame = frame;
        this.mahasiswaApiClient = mahasiswaApiClient;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Loading mahasiswa data...");
    }

    @Override
    protected List<Barang> doInBackground() throws Exception {
        return mahasiswaApiClient.findAll();
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            List<Barang> result = get();
            frame.getProgressBar().setString(result.size() + " records loaded");
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to load data");
            JOptionPane.showMessageDialog(frame,
                    "Error loading data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}