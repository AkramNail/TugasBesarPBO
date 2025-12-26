package worker.barang;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.BarangApiClient;
import model.Barang;
import view.BarangFrame;

public class SaveBarangWorker extends SwingWorker<Void, Void> {

    private final BarangFrame frame;
    private final BarangApiClient barangApiClient;
    private final Barang barang;

    public SaveBarangWorker(BarangFrame frame, BarangApiClient barangApiClient, Barang barang) {
        this.frame = frame;
        this.barangApiClient = barangApiClient;
        this.barang = barang;

        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Menyimpan data barang");
    }

    @Override
    protected Void doInBackground() throws Exception {
        barangApiClient.create(barang);
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);

        try {
            get();
            frame.getProgressBar().setString("Data barang berhasil disimpan");
            JOptionPane.showMessageDialog(
                    frame,
                    "Data barang berhasil disimpan",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            frame.getProgressBar().setString("Gagal menyimpan data barang");
            JOptionPane.showMessageDialog(
                    frame,
                    "Gagal menyimpan data\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
