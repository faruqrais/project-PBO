import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogTransaksi {
    private final List<String> logs = new ArrayList<>();

    public void add(String action, Penumpang p, int amount) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = String.format("[%s] %s - id:%d nama:%s tipe:%s amount:%d saldoNow:%d",
                time, action, p.getId(), p.getNama(), p.getType(), amount, p.getSaldo());
        logs.add(entry);
    }

    public List<String> getAll() {
        return new ArrayList<>(logs);
    }
}
