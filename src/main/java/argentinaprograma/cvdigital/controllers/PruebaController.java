package argentinaprograma.cvdigital.controllers;

import argentinaprograma.cvdigital.exceptions.NotFoundException;
import argentinaprograma.cvdigital.models.Usuario;
import argentinaprograma.cvdigital.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
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
    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable("id") Integer id) {
//        throw new RuntimeException("Hola prueba");
        return this.usuarioRepository.findById(id).orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
    }
}
