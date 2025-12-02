import java.util.ArrayList;
import java.util.List;

public class Bus {
    private List<Penumpang> penumpangBiasa;
    private List<Penumpang> penumpangPrioritas;
    private List<Penumpang> penumpangBerdiri;
    public static final int ONGKOS_BUS = 2000;
    private int totalPendapatan;
    private LogTransaksi log;

    // ANSI color codes (works on many terminals)
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private String pad(String text, int width) {
    return String.format("%-" + width + "s", text);
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

    private boolean adaNamaDuplikat(String nama) { //untuk ngecek nama yang duplikat
        String target = nama.trim().toLowerCase();
        for (Penumpang p : allPenumpang()) {
            if (p.getNama().trim().toLowerCase().equals(target)) return true;
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

    public synchronized String naikkanPenumpang(Penumpang p) { //naiikin penumpang
        if (totalSemuaPenumpang() >= 40) {
            return ANSI_RED + "Gagal: Bus sudah penuh!" + ANSI_RESET;
        }
        if (adaNamaDuplikat(p.getNama())) {
            return ANSI_YELLOW + "Gagal: Nama penumpang sudah ada (duplikat)." + ANSI_RESET;
        }
        if (p.getSaldo() < ONGKOS_BUS) {
            return ANSI_YELLOW + "Gagal: Saldo tidak cukup." + ANSI_RESET;
        }

        boolean isPriorRule = p.isPrioritasByRule();

        try {
            p.kurangiSaldo(ONGKOS_BUS);
        } catch (IllegalStateException ex) {
            return ANSI_YELLOW + "Gagal saat mengurangi saldo: " + ex.getMessage() + ANSI_RESET;
        }

        totalPendapatan += ONGKOS_BUS;

        // tempatin sesuai aturan:
        if (isPriorRule) {
            if (penumpangPrioritas.size() < 4) {
                penumpangPrioritas.add(p);
            } else if (penumpangBiasa.size() < 16) {
                penumpangBiasa.add(p); // prioritas boleh duduk di kursi biasa jika masih kosong 
            } else {
                penumpangBerdiri.add(p);
            }
        } else { // penumpang biasa
            if (penumpangBiasa.size() < 16) {
                penumpangBiasa.add(p);
            } else {
                penumpangBerdiri.add(p);
            }
        }

        log.add("NAIK", p, ONGKOS_BUS);

        return ANSI_GREEN + "Berhasil: Penumpang naik. (ongkos terpotong Rp " + ONGKOS_BUS + ")" + ANSI_RESET;
    }

    public synchronized String turunkanPenumpangByName(String nama) {
        Penumpang found = null;
        for (Penumpang p : penumpangPrioritas) {
            if (p.getNama().equalsIgnoreCase(nama)) { found = p; penumpangPrioritas.remove(p); break; }
        }
        if (found == null) {
            for (Penumpang p : penumpangBiasa) {
                if (p.getNama().equalsIgnoreCase(nama)) { found = p; penumpangBiasa.remove(p); break; }
            }
        }
        if (found == null) {
            for (Penumpang p : penumpangBerdiri) {
                if (p.getNama().equalsIgnoreCase(nama)) { found = p; penumpangBerdiri.remove(p); break; }
            }
        }
        if (found != null) {
            log.add("TURUN", found, 0);
            return ANSI_GREEN + "Penumpang Berhasil Turun!" + ANSI_RESET;
        } else {
            return ANSI_YELLOW + "Penumpang Tidak Ditemukan!" + ANSI_RESET;
        }
    }

    // top-up untuk penumpang yang sedang di bus (dari nama penumpang)
    public synchronized String topUpSaldo(String nama, int nominal) {
        for (Penumpang p : allPenumpang()) {
            if (p.getNama().equalsIgnoreCase(nama)) {
                try {
                    p.tambahSaldo(nominal);
                    log.add("TOPUP", p, nominal);
                    return ANSI_CYAN + "Top-up berhasil. Saldo sekarang: Rp " + p.getSaldo() + ANSI_RESET;
                } catch (IllegalArgumentException ex) {
                    return ANSI_YELLOW + "Top-up gagal: " + ex.getMessage() + ANSI_RESET;
                }
            }
        }
        return ANSI_YELLOW + "Penumpang tidak ditemukan untuk top-up." + ANSI_RESET;
    }

    public String statusString() {
    StringBuilder sb = new StringBuilder();

    sb.append(Bus.ANSI_CYAN +
    "┌───────────────────────────────────┐\n" +
    "│            STATUS BUS             │\n" +
    "└───────────────────────────────────┘\n\n"
    + Bus.ANSI_RESET);

        sb.append(Bus.ANSI_CYAN +
    "┌───────────────────────────────────────────────────────────┐\n" +
    "│                 STATUS BUS TRANS KOETARADJA               │\n" +
    "└───────────────────────────────────────────────────────────┘\n\n"
    + Bus.ANSI_RESET);

    sb.append(
        ANSI_BLUE   + pad("KURSI PRIORITAS", 26) + ANSI_RESET + " | " +
        ANSI_GREEN  + pad("KURSI BIASA", 26)     + ANSI_RESET + " | " +
        ANSI_YELLOW + pad("BERDIRI", 26)         + ANSI_RESET + "\n"
    );

    sb.append("--------------------------------------------------------------------------\n");

    int max = 20;

    for (int i = 1; i <= max; i++) {

        // ======================= KURSI PRIORITAS ==========================
         String prioRaw = "";
        if (i <= 4) {
            if (i <= penumpangPrioritas.size()) {
                Penumpang p = penumpangPrioritas.get(i - 1);

                String alasan = "";
                if (p.getUmur() > 60) alasan = "LANSIA";
                else if (p.getUmur() < 10) alasan = "ANAK";
                else if (p.isHamil()) alasan = "HAMIL";

                //tampilin kenapa penumpang ddk di kursi prioritas
                prioRaw = ANSI_BLUE + String.format("(P%02d) %-10s (%d th, %s)", 
                        i, p.getNama(), p.getUmur(), alasan) + ANSI_RESET;

    } else {
        prioRaw = ANSI_BLUE + String.format("(P%02d) <kosong>", i) + ANSI_RESET;
    }
}
        String prio = ANSI_BLUE + pad(prioRaw, 26) + ANSI_RESET;

        // ======================= KURSI BIASA ==========================
        String biasaRaw = "";
        if (i <= 16) {
            if (i <= penumpangBiasa.size()) {
                Penumpang p = penumpangBiasa.get(i - 1);
                biasaRaw = String.format("(B%02d) %-10s (%d th)", i, p.getNama(), p.getUmur());
            } else {
                biasaRaw = String.format("(B%02d) <kosong>", i);
            }
        }
        String biasa = ANSI_GREEN + pad(biasaRaw, 26) + ANSI_RESET;

        // ======================= BERDIRI ==========================
        String berdiriRaw = "";
        if (i <= 20) {
            if (i <= penumpangBerdiri.size()) {
                Penumpang p = penumpangBerdiri.get(i - 1);
                berdiriRaw = String.format("(S%02d) %-10s (%d th)", i, p.getNama(), p.getUmur());
            } else {
                berdiriRaw = String.format("(S%02d) <kosong>", i);
            }
        }
        String berdiri = ANSI_YELLOW + pad(berdiriRaw, 26) + ANSI_RESET;

        sb.append(prio + " | " + biasa + " | " + berdiri + "\n");
    }

    sb.append("\n" + ANSI_CYAN + "Total Penumpang : " + ANSI_RESET + totalSemuaPenumpang() + "\n");
    sb.append(ANSI_CYAN + "Total Pendapatan: " + ANSI_RESET + "Rp " + totalPendapatan + "\n");

    return sb.toString();
}
}
