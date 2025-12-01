public class PenumpangPrioritas extends Penumpang {
    public PenumpangPrioritas(String nama, int umur, boolean hamil) {
        super(nama, umur, hamil);
    }

    @Override
    public PenumpangType getType() {
        return PenumpangType.PRIORITAS;
    }
}
