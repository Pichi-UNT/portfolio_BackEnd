package argentinaprograma.cvdigital.services;

import argentinaprograma.cvdigital.exceptions.BadRequestException;
import argentinaprograma.cvdigital.models.Rol;
import argentinaprograma.cvdigital.models.Usuario;
import argentinaprograma.cvdigital.repositories.UsuarioRepository;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UsuarioServices {

    private UsuarioRepository usuarioRepository;

    private JwtBuilder jwtBuilder;

    @Autowired
    public UsuarioServices(UsuarioRepository usuarioRepository,JwtBuilder jwtBuilder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtBuilder=jwtBuilder;
    }

    public Usuario registrarUsuario(Usuario usuario) {

        if(this.usuarioRepository.existsByNick(usuario.getNick())){
            throw new BadRequestException("El nick se encuentra en uso");
        }

        if(this.usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new BadRequestException("El email se encuentra en uso");
        }
        usuario.setRol("U");
        usuario.setEstado("P");
        Usuario usuarioGuardado=this.usuarioRepository.save(usuario);
        //Enviar email de confirmacion url+"?token"

        return usuarioGuardado;
    }

    public String iniciarSesion(Usuario usuario) {

        Usuario usuarioObtenido=this.usuarioRepository.getByNick(usuario.getNick());
        if(usuarioObtenido==null || !usuarioObtenido.getPass().equals(usuario.getPass())){
            throw new BadRequestException("Usuario o Contraseña incorrectos");
        }

        return jwtBuilder.setSubject(usuarioObtenido.getNick())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
    }

}
