package argentinaprograma.cvdigital.usuario.controllers;

import argentinaprograma.cvdigital.exceptions.NotFoundException;
import argentinaprograma.cvdigital.usuario.models.Rol;
import argentinaprograma.cvdigital.usuario.models.Usuario;
import argentinaprograma.cvdigital.usuario.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/prueba")
public class PruebaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostAuthorize("permitAll()")
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable("id") Integer id) {
        System.out.println(Rol.USER.getValor());
        System.out.println(Rol.USER.name());
        return Usuario.builder().rol(Rol.USER).build();
    }
//    @GetMapping("/2")
//    public String obtenerUsuario() {
//        return Rol.ADMIN.getValor();
//    }
}
