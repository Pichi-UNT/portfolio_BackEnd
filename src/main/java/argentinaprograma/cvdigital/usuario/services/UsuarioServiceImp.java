package argentinaprograma.cvdigital.usuario.services;

import argentinaprograma.cvdigital.exceptions.NotFoundException;
import argentinaprograma.cvdigital.usuario.DTO.UsuarioDTO;
import argentinaprograma.cvdigital.usuario.DTO.UsuarioFullDTO;
import argentinaprograma.cvdigital.usuario.models.Estado;
import argentinaprograma.cvdigital.usuario.models.Usuario;
import argentinaprograma.cvdigital.usuario.repositories.UsuarioRepository;
import argentinaprograma.cvdigital.usuario.services.interfaces.UsuarioIdiomaService;
import argentinaprograma.cvdigital.usuario.services.interfaces.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImp implements UsuarioService, UsuarioIdiomaService {

    UsuarioRepository usuarioRepository;

    private UsuarioValidator usuarioValidator;

    private ModelMapper modelMapper;

    @Autowired
    public UsuarioServiceImp(UsuarioRepository usuarioRepository, UsuarioValidator usuarioValidator) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioValidator = usuarioValidator;
        this.modelMapper = new ModelMapper();
    }


    @Override
    public UsuarioDTO getUsuarioById(Integer usuarioId) {
        Usuario usuario = this.usuarioRepository.findById(usuarioId).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return modelMapper.map(usuario, UsuarioFullDTO.class);
    }

    // Obtiene un usuario de la base de datos a partir de su nombre de usuario
    @Override
    public UsuarioDTO getUsuarioByNick(String nick) {
        // Obtiene el usuario con el nombre de usuario especificado
        Usuario usuario = this.usuarioRepository.getByNick(nick);
        // Si el usuario no existe, lanza una excepción NotFoundException
        if (usuario == null) {
            throw new NotFoundException("Usuario inexistente");
        }
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Override
    public UsuarioDTO actualizarUsuario(Usuario usuario) {
        if (usuario.getEmail() != null) {
            this.usuarioValidator.existeUsuarioConEmailYIdDistinto(usuario.getEmail(), usuario.getId());
        }
        if (usuario.getNick() != null) {
            this.usuarioValidator.existeUsuarioConNickYIdDistinto(usuario.getNick(), usuario.getId());
        }
        return modelMapper.map(usuario, UsuarioDTO.class);
    }



    @Override
    public void desactivarUsuario(Usuario usuario) {
        this.usuarioValidator.noExisteUsuarioConId(usuario.getId());
        usuario.setEstado(Estado.DESACTIVADO);
        this.usuarioRepository.save(usuario);
    }

    @Override
    public void activarUsuario(Usuario usuario) {
        this.usuarioValidator.noExisteUsuarioConId(usuario.getId());
        usuario.setEstado(Estado.ACTIVADO);
        this.usuarioRepository.save(usuario);
    }



}
