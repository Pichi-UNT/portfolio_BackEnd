package argentinaprograma.cvdigital.usuario.services.interfaces;

import argentinaprograma.cvdigital.usuario.DTO.AuthCredentials;
import argentinaprograma.cvdigital.usuario.models.Usuario;

public interface AuthService {

    void registrarUsuario(Usuario usuario);

    void confirmarRegistro(AuthCredentials authCredentials);

    String iniciarSesion(AuthCredentials authCredentials);

    void solicitarRestablecimientoDeContraseña(AuthCredentials authCredentials);

    void RestablecimientoDeContraseña(AuthCredentials authCredentials);


}
