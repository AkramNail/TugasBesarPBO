package worker.barang;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.BarangApiClient;
import model.Barang;
import view.BarangFrame;

public class UpdateBarangWorker extends SwingWorker<Void, Void> {
    private final BarangFrame frame;
    private final BarangApiClient barangApiClient;
    private final Barang barang;

    public UpdateBarangWorker(BarangFrame frame, BarangApiClient barangApiClient, Barang barang) {
        this.frame = frame;
        this.barangApiClient = barangApiClient;
        this.barang = barang;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Updating mahasiswa data...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        barangApiClient.update(barang);
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get();
            frame.getProgressBar().setString("Mahasiswa updated successfully");
            JOptionPane.showMessageDialog(frame,
                    "Mahasiswa record has been updated.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to update mahasiswa");
            JOptionPane.showMessageDialog(frame,
                    "Error updating data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}