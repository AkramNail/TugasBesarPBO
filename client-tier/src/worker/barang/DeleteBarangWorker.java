package worker.barang;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.BarangApiClient;
import model.Barang;
import view.BarangFrame;

public class DeleteBarangWorker extends SwingWorker<Void, Void> {

    private final BarangFrame frame;
    private final BarangApiClient barangApiClient;
    private final Barang barang;

    public DeleteBarangWorker(BarangFrame frame, BarangApiClient barangApiClient, Barang barang) {
        this.frame = frame;
        this.barangApiClient = barangApiClient;
        this.barang = barang;
    }

    @Override
    protected Void doInBackground() throws Exception {
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Menghapus data barang");

        barangApiClient.delete(barang.getId());
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        frame.getProgressBar().setString("");

        try {
            get();
            JOptionPane.showMessageDialog(
                    frame,
                    "Data barang berhasil dihapus",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Gagal menghapus data\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static boolean confirmDelete(BarangFrame frame, Barang barang) {
        if (barang == null) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Pilih data barang terlebih dahulu",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        int result = JOptionPane.showConfirmDialog(
                frame,
                "Yakin ingin menghapus barang ini",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        return result == JOptionPane.YES_OPTION;
    }
}
