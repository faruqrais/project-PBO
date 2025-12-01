public class PenumpangBiasa extends Penumpang {
    public PenumpangBiasa(String nama, int umur, boolean hamil) {
        super(nama, umur, hamil);
    }

    @Override
    public PenumpangType getType() {
        return PenumpangType.BIASA;
    }
}
