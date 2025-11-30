import java.util.Scanner;

public class TestBus {
    private static final String LINE = "────────────────────────────────";
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
            while (true) {  //supaya bisa tambbah beberapa penumpang sekaligus
            System.out.println("  > Tambah Penumpang\n" + LINE);

            try {
                System.out.print("  Nama           : ");
                String nama = sc.nextLine().trim();

                System.out.print("  Umur           : ");
                int umur = Integer.parseInt(sc.nextLine().trim());

                System.out.print("  Hamil (y/n)    : ");
                boolean hamil = sc.nextLine().equalsIgnoreCase("y");

                boolean priorRule = (umur > 60) || (umur < 10) || hamil;

                Penumpang p = priorRule ?
                    new PenumpangPrioritas(nama, umur, hamil) :
                    new PenumpangBiasa(nama, umur, hamil);

                System.out.println("\n" + bus.naikkanPenumpang(p));}
                catch (Exception ex) {
                System.out.println(Bus.ANSI_RED +
                    "\n  Terjadi error saat input: " + ex.getMessage() +
                    Bus.ANSI_RESET);
                }

                System.out.print("\nTambah penumpang lagi? (y/n): ");
                String lagi = sc.nextLine().trim();

                if (!lagi.equalsIgnoreCase("y")) break;

                System.out.println();
                }

                System.out.println("\nTekan ENTER untuk kembali...");
                sc.nextLine();

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