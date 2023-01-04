package argentinaprograma.cvdigital.controllers;

import argentinaprograma.cvdigital.models.Usuario;
import argentinaprograma.cvdigital.services.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/auth")
public class AuthController {

    UsuarioServices usuarioServices;

    @Autowired
    public AuthController(UsuarioServices usuarioServices) {
        this.usuarioServices = usuarioServices;
    }


    @PostMapping("/signin")
    public ResponseEntity<Void> registrar(@RequestBody Usuario usuario) {
        Usuario usuarioCreado = usuarioServices.registrarUsuario(usuario);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Usuario usuario) {
        String token=usuarioServices.iniciarSesion(usuario);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return ResponseEntity.ok().headers(headers).build();
    }

//    @PutMapping("/pass-reset")
//    public ResponseEntity<Void> recuperarContraseña(@RequestBody Usuario usuario, @RequestBody String url) {
//        Usuario usuarioCreado = usuarioServices.registrarUsuario(usuario, url);
//
//    }



}
