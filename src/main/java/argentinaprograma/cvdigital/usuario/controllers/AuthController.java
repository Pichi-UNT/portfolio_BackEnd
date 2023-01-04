package argentinaprograma.cvdigital.usuario.controllers;

import argentinaprograma.cvdigital.usuario.DTO.AuthCredentials;
import argentinaprograma.cvdigital.usuario.models.Usuario;
import argentinaprograma.cvdigital.usuario.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signin")
    public ResponseEntity<Void> registrar(@RequestBody Usuario usuario) {
        authService.registrarUsuario(usuario);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthCredentials authCredentials) {
        String token= authService.iniciarSesion(authCredentials);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return ResponseEntity.ok().headers(headers).build();
    }



}
