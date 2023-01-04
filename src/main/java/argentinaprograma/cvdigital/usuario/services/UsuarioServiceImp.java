package argentinaprograma.cvdigital.usuario.services;

import argentinaprograma.cvdigital.exceptions.NotFoundException;
import argentinaprograma.cvdigital.usuario.models.Usuario;
import argentinaprograma.cvdigital.usuario.repositories.UsuarioRepository;
import argentinaprograma.cvdigital.usuario.services.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImp implements UsuarioService {

    UsuarioRepository  usuarioRepository;

    @Autowired
    public UsuarioServiceImp(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario getUsuarioById(Integer usuarioId) {
        return this.usuarioRepository.findById(usuarioId).orElseThrow(()-> new NotFoundException("Usuario não encontrado"));
    }

    // Obtiene un usuario de la base de datos a partir de su nombre de usuario
    @Override
    public Usuario getUsuarioByNick(String nick) {
        // Obtiene el usuario con el nombre de usuario especificado
        Usuario usuario = this.usuarioRepository.getByNick(nick);
        // Si el usuario no existe, lanza una excepción NotFoundException
        if (usuario == null) {
            throw new NotFoundException("Usuario inexistente");
        }
        return usuario;
    }

    @Override
    public Usuario updateUsuario(Usuario usuario) {
        Usuario usuarioObtenido =this.getUsuarioById(usuario.getId());



        return null;
    }

    @Override
    public Usuario CreateUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public Usuario BanearUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public Usuario ActivarUsuario(Usuario usuario) {
        return null;
    }



}
