package argentinaprograma.cvdigital.usuario.services.interfaces;

import argentinaprograma.cvdigital.usuario.DTO.UsuarioDTO;
import argentinaprograma.cvdigital.usuario.models.Usuario;

public interface UsuarioService {

    UsuarioDTO actualizarUsuario(Usuario usuario);

    UsuarioDTO getUsuarioById(Integer usuarioId);

    UsuarioDTO getUsuarioByNick(String username);

    void desactivarUsuario(Usuario usuario);

    void activarUsuario(Usuario usuario);


}
