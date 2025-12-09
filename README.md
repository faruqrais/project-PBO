Program ini adalah simulasi pengelolaan penumpang pada Bus Trans Koetaradja menggunakan konsep OOP di Java. Sistem ini mengatur cara penumpang naik, turun, top-up saldo, serta bagaimana mereka ditempatkan di dalam bus sesuai aturan prioritas.

Struktur program terdiri dari beberapa class utama. Class Penumpang menyimpan data dasar seperti nama, umur, status hamil, dan saldo. Dua subclass, yaitu PenumpangBiasa dan PenumpangPrioritas, digunakan agar aturan tempat duduk bisa diterapkan dengan rapi. Semua aktivitas seperti naik, turun, dan top-up dicatat oleh class LogTransaksi.

Class Bus mengelola seluruh logika, termasuk tiga jenis tempat: kursi prioritas, kursi biasa, dan ruang berdiri. Aturan prioritas otomatis menentukan apakah penumpang termasuk lansia, anak-anak, atau ibu hamil, dan menempatkannya ke posisi yang sesuai. Sistem juga memotong saldo secara otomatis saat penumpang naik dan menambah pendapatan bus.

Tampilan status bus dibuat dalam bentuk tabel berwarna menggunakan ANSI color dan padding agar kolom tetap sejajar. Informasi seperti alasan prioritas (“LANSIA”, “ANAK”, atau “HAMIL”) ditampilkan langsung di dalam tabel.

Class TestBus berfungsi sebagai menu interaktif untuk menjalankan fitur-fitur utama seperti menambah penumpang, menurunkan penumpang, melihat status bus, top-up saldo, dan melihat log transaksi.

Program ini dapat dijalankan lewat terminal/command line dan dirancang agar mudah dipahami, modular, dan sesuai prinsip dasar OOP.
