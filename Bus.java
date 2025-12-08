import java.util.ArrayList;
import java.util.List;

public class Bus {

    private List<Penumpang> penumpangBiasa;
    private List<Penumpang> penumpangPrioritas;
    private List<Penumpang> penumpangBerdiri;
    public static final int ONGKOS_BUS = 2000;
    private int totalPendapatan;
    private LogTransaksi log;

    // ANSI color codes
    public static final String ANSI_RESET  = "\u001B[0m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_GREEN  = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_CYAN   = "\u001B[36m";

    // padding fix agar kolom selalu rapi
    private String pad(String text, int length) {
        if (text.length() >= length) return text;
        return text + " ".repeat(length - text.length());
    }

    public Bus() {
        penumpangBiasa = new ArrayList<>();
        penumpangPrioritas = new ArrayList<>();
        penumpangBerdiri = new ArrayList<>();
        totalPendapatan = 0;
        log = new LogTransaksi();
    }

    public List<Penumpang> getPenumpangBiasa() { return new ArrayList<>(penumpangBiasa); }
    public List<Penumpang> getPenumpangPrioritas() { return new ArrayList<>(penumpangPrioritas); }
    public List<Penumpang> getPenumpangBerdiri() { return new ArrayList<>(penumpangBerdiri); }
    public int getJumlahPenumpangBiasa() { return penumpangBiasa.size(); }
    public int getJumlahPenumpangPrioritas() { return penumpangPrioritas.size(); }
    public int getJumlahPenumpangBerdiri() { return penumpangBerdiri.size(); }
    public int getTotalPendapatan() { return totalPendapatan; }
    public LogTransaksi getLog() { return log; }

    public int totalSemuaPenumpang() {
        return getJumlahPenumpangBiasa() + getJumlahPenumpangPrioritas() + getJumlahPenumpangBerdiri();
    }

    // cek duplikat nama
    private boolean adaNamaDuplikat(String nama) {
        String target = nama.trim().toLowerCase();
        for (Penumpang p : allPenumpang()) {
            if (p.getNama().trim().toLowerCase().equals(target)) {
                return true;
            }
        }
        return false;
    }

    private List<Penumpang> allPenumpang() {
        List<Penumpang> all = new ArrayList<>();
        all.addAll(penumpangPrioritas);
        all.addAll(penumpangBiasa);
        all.addAll(penumpangBerdiri);
        return all;
    }

    // Naikkan penumpang
    public synchronized String naikkanPenumpang(Penumpang p) {

        if (totalSemuaPenumpang() >= 40) {
            return ANSI_RED + "Gagal: Bus sudah penuh!" + ANSI_RESET;
        }
        if (adaNamaDuplikat(p.getNama())) {
            return ANSI_YELLOW + "Gagal: Nama penumpang sudah ada." + ANSI_RESET;
        }
        if (p.getSaldo() < ONGKOS_BUS) {
            return ANSI_YELLOW + "Gagal: Saldo tidak cukup." + ANSI_RESET;
        }

        boolean isPrior = p.isPrioritasByRule();

        try {
            p.kurangiSaldo(ONGKOS_BUS);
        } catch (Exception ex) {
            return ANSI_RED + "Error saldo: " + ex.getMessage() + ANSI_RESET;
        }

        totalPendapatan += ONGKOS_BUS;

        if (isPrior) {
            if (penumpangPrioritas.size() < 4) {
                penumpangPrioritas.add(p);
            } else if (penumpangBiasa.size() < 16) {
                penumpangBiasa.add(p);
            } else {
                penumpangBerdiri.add(p);
            }
        } else {
            if (penumpangBiasa.size() < 16) {
                penumpangBiasa.add(p);
            } else {
                penumpangBerdiri.add(p);
            }
        }

        log.add("NAIK", p, ONGKOS_BUS);

        return ANSI_GREEN + "Berhasil naik. Ongkos terpotong Rp " + ONGKOS_BUS + ANSI_RESET;
    }

    public synchronized String turunkanPenumpangByName(String nama) {
        Penumpang found = null;

        for (Penumpang p : penumpangPrioritas) {
            if (p.getNama().equalsIgnoreCase(nama)) {
                found = p;
                penumpangPrioritas.remove(p);
                break;
            }
        }
        if (found == null) {
            for (Penumpang p : penumpangBiasa) {
                if (p.getNama().equalsIgnoreCase(nama)) {
                    found = p;
                    penumpangBiasa.remove(p);
                    break;
                }
            }
        }
        if (found == null) {
            for (Penumpang p : penumpangBerdiri) {
                if (p.getNama().equalsIgnoreCase(nama)) {
                    found = p;
                    penumpangBerdiri.remove(p);
                    break;
                }
            }
        }

        if (found != null) {
            log.add("TURUN", found, 0);
            return ANSI_GREEN + "Penumpang turun." + ANSI_RESET;
        } else {
            return ANSI_YELLOW + "Nama tidak ditemukan." + ANSI_RESET;
        }
    }

    // Top-up saldo
    public synchronized String topUpSaldo(String nama, int nominal) {
        for (Penumpang p : allPenumpang()) {
            if (p.getNama().equalsIgnoreCase(nama)) {
                try {
                    p.tambahSaldo(nominal);
                    log.add("TOPUP", p, nominal);
                    return ANSI_CYAN + "Top-up berhasil. Saldo sekarang Rp " + p.getSaldo() + ANSI_RESET;
                } catch (Exception e) {
                    return ANSI_YELLOW + "Top-up gagal: " + e.getMessage() + ANSI_RESET;
                }
            }
        }
        return ANSI_YELLOW + "Penumpang tidak ditemukan." + ANSI_RESET;
    }

    // --- TAMPILAN TABEL STATUS ---
    public String statusString() {
        StringBuilder sb = new StringBuilder();

        sb.append(ANSI_CYAN +
                "┌───────────────────────────────────────────────────────────┐\n" +
                "│                 STATUS BUS TRANS KOETARADJA               │\n" +
                "└───────────────────────────────────────────────────────────┘\n\n"
                + ANSI_RESET);

        sb.append(
            ANSI_BLUE   + pad("KURSI PRIORITAS", 30) + ANSI_RESET + " | " +
            ANSI_GREEN  + pad("KURSI BIASA",     30) + ANSI_RESET + " | " +
            ANSI_YELLOW + pad("BERDIRI",         30) + ANSI_RESET + "\n"
        );

        sb.append("──────────────────────────────────────────────────────────────────────────────\n");

        int max = 20;

        for (int i = 1; i <= max; i++) {

            // PRIORITAS
            String prioCell;
            if (i <= 4) {
                if (i <= penumpangPrioritas.size()) {
                    Penumpang p = penumpangPrioritas.get(i - 1);

                    String alasan =
                            (p.getUmur() > 60) ? "LANSIA" :
                            (p.getUmur() < 10) ? "ANAK" :
                            (p.isHamil() ? "HAMIL" : "");

                    String raw = String.format("(P%02d) %-10s (%d th, %s)", 
                                    i, p.getNama(), p.getUmur(), alasan);

                    prioCell = ANSI_BLUE + pad(raw, 30) + ANSI_RESET;

                } else {
                    prioCell = ANSI_BLUE + pad(String.format("(P%02d) <kosong>", i), 30) + ANSI_RESET;
                }
            } else {
                prioCell = " ".repeat(30);
            }

            // BIASA
            String biasaCell;
            if (i <= 16) {
                if (i <= penumpangBiasa.size()) {
                    Penumpang p = penumpangBiasa.get(i - 1);

                    String raw = String.format("(B%02d) %-10s (%d th)",
                                    i, p.getNama(), p.getUmur());

                    biasaCell = ANSI_GREEN + pad(raw, 30) + ANSI_RESET;

                } else {
                    biasaCell = ANSI_GREEN + pad(String.format("(B%02d) <kosong>", i), 30) + ANSI_RESET;
                }
            } else {
                biasaCell = " ".repeat(30);
            }

            // BERDIRI
            String berdiriCell;
            if (i <= 20) {
                if (i <= penumpangBerdiri.size()) {
                    Penumpang p = penumpangBerdiri.get(i - 1);

                    String raw = String.format("(S%02d) %-10s (%d th)",
                                    i, p.getNama(), p.getUmur());

                    berdiriCell = ANSI_YELLOW + pad(raw, 30) + ANSI_RESET;

                } else {
                    berdiriCell = ANSI_YELLOW + pad(String.format("(S%02d) <kosong>", i), 30) + ANSI_RESET;
                }
            } else {
                berdiriCell = " ".repeat(30);
            }

            sb.append(prioCell + " | " + biasaCell + " | " + berdiriCell + "\n");
        }

        sb.append("\n" + ANSI_CYAN + "Total Penumpang : " + ANSI_RESET + totalSemuaPenumpang() + "\n");
        sb.append(ANSI_CYAN + "Total Pendapatan: " + ANSI_RESET + "Rp " + totalPendapatan + "\n");

        return sb.toString();
    }
}
