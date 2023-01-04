package argentinaprograma.cvdigital.usuario.services;

import argentinaprograma.cvdigital.exceptions.BadRequestException;
import argentinaprograma.cvdigital.usuario.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidator {

    UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioValidator(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void existeUsuarioConEmail(String email){
        if (this.usuarioRepository.existsByEmail(email)) {
            throw new BadRequestException("El email se encuentra en uso");
        }
    }

    public void existeUsuarioConNick(String nick){
        if (this.usuarioRepository.existsByNick(nick)) {
            throw new BadRequestException("El nick se encuentra en uso");
        }
    }

}
