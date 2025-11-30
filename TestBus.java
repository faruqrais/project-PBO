import java.util.Scanner;

public class TestBus {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bus bus = new Bus();

        while (true) {
            System.out.println();
            System.out.println(Bus.ANSI_CYAN + "===== BUS TRANS KOETARADJA ======" + Bus.ANSI_RESET);
            System.out.println("1. Naikkan Penumpang");
            System.out.println("2. Turunkan Penumpang");
            System.out.println("3. Lihat Penumpang");
            System.out.println("4. Top-up Saldo (penumpang di bus)");
            System.out.println("5. Lihat Log Transaksi");
            System.out.println("6. Keluar");
            System.out.print("Pilihan: ");

            String input = sc.nextLine();
            int pilihan;
            try {
                pilihan = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println(Bus.ANSI_YELLOW + "Masukkan angka yang valid!" + Bus.ANSI_RESET);
                continue;
            }
        }
    }
}