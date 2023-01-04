package argentinaprograma.cvdigital.models;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum Rol {
    ADMIN('A'),
    USER('U');

    private final char valor;

    Rol(char valor) {
        this.valor = valor;
    }

    public char getValor() {
        return valor;
    }
}
