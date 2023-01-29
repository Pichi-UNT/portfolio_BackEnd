package argentinaprograma.cvdigital.usuario.models;




public enum Rol {
    ADMIN("A"),
    USER("U");

    private final String valor;

    private Rol(String valor) {
        this.valor = valor.toUpperCase();
    }

    public String getValor() {
        return valor.toUpperCase();
    }

    public static Rol getEnum(String valor) {
        Rol rol=null;
        for (Rol r : Rol.values()) {
            if (r.getValor().equals(valor)) {
                rol=r;
            }
        }
        return rol;
    }

}
