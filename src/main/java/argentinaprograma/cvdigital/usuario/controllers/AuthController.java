package argentinaprograma.cvdigital.usuario.controllers;

import argentinaprograma.cvdigital.usuario.DTO.UsuarioAuthCredentialsDTO;
import argentinaprograma.cvdigital.usuario.DTO.UsuarioFullDTO;
import argentinaprograma.cvdigital.usuario.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    //Probado
    @PostMapping("/register")
    public ResponseEntity<Void> registrar(@RequestBody UsuarioFullDTO usuario) {
        authService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register/confirm")
    public ResponseEntity<Void> confirmarRegistro(@RequestBody UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO){
        this.authService.confirmarRegistro(usuarioAuthCredentialsDTO);
        return ResponseEntity.ok().build();

    }

    //Probado
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO) {
        String token= authService.iniciarSesion(usuarioAuthCredentialsDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/pass-reset")
    public ResponseEntity<Void> solicitarRestablecimientoDeContraseña(@RequestBody UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO){
        this.authService.solicitarRestablecimientoDeContraseña(usuarioAuthCredentialsDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pass-reset/confirm")
    public ResponseEntity<Void> RestablecimientoDeContraseña(@RequestBody UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO){
        this.authService.RestablecimientoDeContraseña(usuarioAuthCredentialsDTO);
        return ResponseEntity.ok().build();
    }





}
