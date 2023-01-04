package argentinaprograma.cvdigital.usuario.services.interfaces;

import argentinaprograma.cvdigital.usuario.models.Usuario;

public interface UsuarioService {

    Usuario CreateUsuario(Usuario usuario);

    Usuario updateUsuario(Usuario usuario);

    Usuario getUsuarioById(Integer usuarioId);

    Usuario getUsuarioByNick(String username);

    Usuario BanearUsuario(Usuario usuario);

    Usuario ActivarUsuario(Usuario usuario);


}
