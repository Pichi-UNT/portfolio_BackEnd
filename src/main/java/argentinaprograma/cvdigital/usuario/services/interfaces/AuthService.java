package argentinaprograma.cvdigital.usuario.services.interfaces;

import argentinaprograma.cvdigital.usuario.DTO.UsuarioAuthCredentialsDTO;
import argentinaprograma.cvdigital.usuario.DTO.UsuarioFullDTO;

public interface AuthService {

    void registrarUsuario(UsuarioFullDTO usuario);

    void confirmarRegistro(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO);

    String iniciarSesion(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO);

    void solicitarRestablecimientoDeContrase├▒a(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO);

    void RestablecimientoDeContrase├▒a(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO);


}
