<?php
class Barang extends Model {

    public $id;
    public $kategori;
    public $nama;
    public $jumlah;

    public function __construct($db) {
        parent::__construct($db);
        $this->table = "barang";
    }

    public function getAll() {
        $query = "SELECT * FROM {$this->table} ORDER BY id ASC";
        return $this->executeQuery($query);
    }

    public function getById() {
        $query = "SELECT * FROM {$this->table} WHERE id = :id LIMIT 1";
        $stmt = $this->executeQuery($query, [':id' => $this->id]);
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($row) {
            $this->kategori = $row['kategori'];
            $this->nama = $row['nama'];
            $this->jumlah = $row['jumlah'];
            return $row;
        }

        return false;
    }

    public function create() {
        $query = "INSERT INTO {$this->table} 
                  (kategori, nama, jumlah) 
                  VALUES (:kategori, :nama, :jumlah)";

        $params = [
            ':kategori' => $this->kategori,
            ':nama' => $this->nama,
            ':jumlah' => $this->jumlah
        ];

        $stmt = $this->executeQuery($query, $params);

        if ($stmt) {
            $this->id = $this->conn->lastInsertId();
            return true;
        }

        return false;
    }

    public function update() {
        $query = "UPDATE {$this->table} 
                  SET kategori = :kategori, 
                      nama = :nama, 
                      jumlah = :jumlah
                  WHERE id = :id";

        $params = [
            ':id' => $this->id,
            ':kategori' => $this->kategori,
            ':nama' => $this->nama,
            ':jumlah' => $this->jumlah
        ];

        $stmt = $this->executeQuery($query, $params);
        return $stmt->rowCount() > 0;
    }

    public function delete() {
        $query = "DELETE FROM {$this->table} WHERE id = :id";
        $stmt = $this->executeQuery($query, [':id' => $this->id]);
        return $stmt->rowCount() > 0;
    }
}
