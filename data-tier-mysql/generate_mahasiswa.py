import random
from faker import Faker

fake = Faker('id_ID') # Menggunakan lokal Indonesia untuk nama yang lebih relevan
num_records = 500000
file_name = 'mahasiswa_data_500k.sql'

# Jurusan yang bervariasi
majors = [
    'Teknik Informatika', 'Sistem Informasi', 'Manajemen', 'Akuntansi',
    'Hukum', 'Psikologi', 'Desain Komunikasi Visual', 'Arsitektur',
    'Teknik Sipil', 'Ilmu Komunikasi', 'Kedokteran', 'Agribisnis', 'Animasi', 'Teknik Media Digital', 'Pertambangan', 'Geografi', 'Pendidikan Vokasi', 'Elektro', 'Ilmu Komputer', 'Desain Komunikasi Visual', 'Seni Rupa', 'Bisnis Manajemen'
]

with open(file_name, 'w', encoding='utf-8') as f:
    # 1. Perintah DDL
    f.write("CREATE DATABASE IF NOT EXISTS realtime_db;\n")
    f.write("USE realtime_db;\n")
    f.write("CREATE TABLE IF NOT EXISTS mahasiswas (\n")
    f.write("    id INT AUTO_INCREMENT PRIMARY KEY,\n")
    f.write("    nim VARCHAR(20) UNIQUE NOT NULL,\n")
    f.write("    nama VARCHAR(100) NOT NULL,\n")
    f.write("    jurusan VARCHAR(50) NOT NULL\n")
    f.write(");\n\n")

    # 2. Perintah DML
    f.write("INSERT INTO mahasiswas (nim, nama, jurusan) VALUES\n")

    for i in range(1, num_records + 1):
        nim = f"202401{i:06d}"
        
        # --- PERBAIKAN DI SINI ---
        # Menggunakan first_name() dan last_name() dari lokal id_ID
        # Ini menghasilkan nama seperti "Budi Santoso", "Siti Rahayu"
        nama = f"{fake.first_name()} {fake.last_name()}".replace("'", "''")
        # --------------------------
        
        jurusan = random.choice(majors)

        line = f"('{nim}', '{nama}', '{jurusan}')"

        if i < num_records:
            line += ",\n"
        else:
            line += ";\n"

        f.write(line)

print(f"File '{file_name}' dengan {num_records} data nama Indonesia telah berhasil dibuat.")