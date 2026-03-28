# Mental Health Automated Matcher

Proyek Java yang mengotomatisasi pencocokan jadwal konseling antara mahasiswa dan konselor dengan sistem alternatif yang cerdas.

## Fitur Utama
- 3 Konselor Builtin: Dr. Siti, Dr. Rini, Dr. Budi dengan jadwal kerja dan jam konseling masing-masing
- Pilihan Konselor: Mahasiswa bisa memilih konselor pilihan (1-3)
- Input Jam Kuliah Fleksibel: Mahasiswa memilih jam kuliah dari pilihan (1-9 = 08:00-17:00, tanpa 12:00)
- Inputan Keluhan: Mahasiswa bisa menginput alasan/keluhan konseling
- Auto Matching: Sistem otomatis mencari slot yang sesuai
- Pilihan Alternatif: Jika slot tidak cocok di konselor pilihan, sistem menampilkan 2-3 alternatif dari konselor lain di hari yang sama
- Output Rapi: Konfirmasi jadwal dengan format indentasi yang jelas

## Struktur Folder
```
src/
├── model/
│   ├── User.java           - Abstract class dasar (id, name, schedule)
│   ├── Student.java        - Mahasiswa (requestDay, complain)
│   ├── Counselor.java      - Konselor (title, workDays, officeHours)
│   └── Appointment.java    - Jadwal terkonfirmasi
│
└── driver/
    └── Main.java           - Program utama dengan CLI interaktif

bin/                         - Compiled class files
```

## Cara Menjalankan

### Compile:
```bash
cd c:\Users\user\Downloads\miniproject_griselda\miniproject_griselda
javac -d bin src/model/*.java src/driver/*.java
```

### Jalankan:
```bash
java -cp bin driver.Main
```

## Flow Program

1. Input Mahasiswa
   - NIM dan Nama
   - Keluhan/Alasan Konseling
   - Pilih jam kuliah (ketik: 1,3,5 untuk 08:00, 10:00, 13:00)

2. Pilih Konselor
   - Tampilkan daftar 3 konselor: Dr. Siti, Dr. Rini, Dr. Budi
   - Student memilih (1-3)

3. Input Hari Konseling
   - Student input hari yang diinginkan (Senin/Selasa/Rabu/Kamis/Jumat)

4. Auto Matching
   - Jika cocok: Tampilkan jadwal dengan format rapi
   - Jika tidak cocok: Tampilkan 2-3 alternatif dari konselor lain
   - Student bisa memilih alternatif atau cancel

## Data Konselor

| No | Nama | Gelar | Hari Kerja | Jam Konseling |
|----|------|-------|-----------|---------------|
| 1 | Dr. Siti | S.Psi, M.Psi | Senin, Rabu, Jumat | 09:00, 11:00, 14:00 |
| 2 | Dr. Rini | S.Psi | Senin, Selasa, Kamis | 08:00, 10:00, 15:00 |
| 3 | Dr. Budi | S.Psi, Ph.D | Selasa, Rabu, Jumat | 08:00, 13:00, 16:00 |

## Jam Operasional Kampus
- 08:00 → 17:00 (Senin-Jumat)
- Jam makan siang (12:00) dihapus

## Catatan Teknis

### Konsep OOP yang Diterapkan
- Inheritance: Student & Counselor extends User
- Encapsulation: Private fields dengan getter/setter
- Polymorphism: Method overriding (toString)
- Collection: ArrayList untuk manajemen data

### Static vs Instance Context
- Static: `CAMPUS_HOURS` (jam operasional global)
- Instance: `officeHours` (jadwal konseling per konselor), `schedule` (jadwal sibuk mahasiswa)

### Method Penting
- `findBestSlot()`: Mencari slot jam yang cocok untuk konselor pilihan
- `findAlternativeSlots()`: Mencari alternatif dari konselor lain
- `parseSlots()`: Parse input jam dari format pilihan (1-9) menjadi waktu (08:00, 09:00, dst)

## Komentar & Dokumentasi
Semua file kode dilengkapi komentar dalam Bahasa Indonesia untuk memudahkan pembelajaran OOP dan struktur sistem.

---
**Dikembangkan untuk pembelajaran OOP dan Sistem Informasi Kesehatan Mental Kampus**
