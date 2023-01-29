package argentinaprograma.cvdigital.usuario.services.interfaces;

import argentinaprograma.cvdigital.usuario.DTO.UsuarioAuthCredentialsDTO;
import argentinaprograma.cvdigital.usuario.DTO.UsuarioFullDTO;

public interface AuthService {

    void registrarUsuario(UsuarioFullDTO usuario);

    void confirmarRegistro(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO);

    String iniciarSesion(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO);

    void solicitarRestablecimientoDeContraseña(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO);

    void RestablecimientoDeContraseña(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO);


}
