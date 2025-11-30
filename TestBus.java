import java.util.Scanner;

public class TestBus {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bus bus = new Bus();

        while (true) {
            System.out.println();
            System.out.println(Bus.ANSI_CYAN + 
"┌────────────────────────────────────────────┐\n" +
"│         BUS TRANS KOETARADJA SYSTEM        │\n" +
"└────────────────────────────────────────────┘" +
             Bus.ANSI_RESET);

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

            if (pilihan == 1) {
                try {
                    System.out.print("Nama: ");
                    String nama = sc.nextLine().trim();

                    System.out.print("Umur: ");
                    int umur = Integer.parseInt(sc.nextLine().trim());

                    System.out.print("Hamil (y/n): ");
                    boolean hamil = sc.nextLine().trim().equalsIgnoreCase("y");

                    Penumpang candidate;
                    boolean isPrior = (umur > 60) || (umur < 10) || hamil;
                    if (isPrior) candidate = new PenumpangPrioritas(nama, umur, hamil);
                    else candidate = new PenumpangBiasa(nama, umur, hamil);

                    String result = bus.naikkanPenumpang(candidate);
                    System.out.println(result);

                } catch (NumberFormatException ex) {
                    System.out.println(Bus.ANSI_YELLOW + "Umur harus angka!" + Bus.ANSI_RESET);
                } catch (Exception ex) {
                    System.out.println(Bus.ANSI_RED + "Terjadi error: " + ex.getMessage() + Bus.ANSI_RESET);
                }

            } else if (pilihan == 2) {
                System.out.print("Nama yang ingin turun: ");
                String nama = sc.nextLine().trim();
                System.out.println(bus.turunkanPenumpangByName(nama));

            } else if (pilihan == 3) {
                System.out.println(bus.statusString());

            } else if (pilihan == 4) {
                System.out.print("Nama yang ingin top-up (harus ada di bus): ");
                String nama = sc.nextLine().trim();
                System.out.print("Nominal top-up (Rp): ");
                try {
                    int nominal = Integer.parseInt(sc.nextLine().trim());
                    System.out.println(bus.topUpSaldo(nama, nominal));
                } catch (NumberFormatException ex) {
                    System.out.println(Bus.ANSI_YELLOW + "Nominal harus angka!" + Bus.ANSI_RESET);
                }

            } else if (pilihan == 5) {
                System.out.println(Bus.ANSI_CYAN +
                "┌────────────────────────────────┐\n" +
                "│         LOG TRANSAKSI          │\n" +
                "└────────────────────────────────┘\n"
                + Bus.ANSI_RESET);

                for (String s : bus.getLog().getAll()) {
                    System.out.println(" " + s);
                }

            } else if (pilihan == 6) {
                System.out.println(Bus.ANSI_CYAN + "Program selesai." + Bus.ANSI_RESET);
                break;

            } else {
                System.out.println(Bus.ANSI_YELLOW + "Pilihan tidak valid!" + Bus.ANSI_RESET);
            }
        }
        sc.close();
    }
}