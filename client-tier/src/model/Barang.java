package model;

public class Barang {

    private int id;
    private String nama;
    private String kategori;
    private String jumlah;
    private String waktu; // ⬅️ FIELD BARU

    public Barang() {
    }

    // ===== GETTER =====
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getKategori() {
        return kategori;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getWaktu() {      // ⬅️ GETTER BARU
        return waktu;
    }

    // ===== SETTER =====
    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public void setWaktu(String waktu) {  // ⬅️ SETTER BARU
        this.waktu = waktu;
    }
}
