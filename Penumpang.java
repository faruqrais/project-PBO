import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Penumpang {
    private static int nextId = 1;
    private final int id;
    private String nama;
    private int umur;
    private boolean hamil;
    private int saldo;
    private final LocalDateTime createdAt;

    public Penumpang(String nama, int umur, boolean hamil) {
        this.id = nextId++;
        this.nama = nama;
        this.umur = umur;
        this.hamil = hamil;
        this.saldo = 10000;  // saldo awal 
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public int getUmur() { return umur; }
    public boolean isHamil() { return hamil; }
    public int getSaldo() { return saldo; }

    public void tambahSaldo(int s) {
        if (s <= 0) throw new IllegalArgumentException("Nominal top-up harus > 0");
        saldo += s;
    }

    public void kurangiSaldo(int ongkos) {
        if (ongkos < 0) throw new IllegalArgumentException("Ongkos negatif");
        if (saldo < ongkos) throw new IllegalStateException("Saldo tidak cukup");
        saldo -= ongkos;
    }

    // prioritas: lansia (>60), anak (<10), atau hamil
    public boolean isPrioritasByRule() {
        return (umur > 60) || (umur < 10) || hamil;
    }

    public String getCreatedAtString() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public abstract PenumpangType getType();

    @Override
    public String toString() {
        return String.format("%s[id=%d, umur=%d, hamil=%s, saldo=%d]",
                nama, id, umur, hamil ? "y" : "n", saldo);
    }
}
