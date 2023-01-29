package argentinaprograma.cvdigital.usuario.services;

import argentinaprograma.cvdigital.exceptions.BadRequestException;
import argentinaprograma.cvdigital.exceptions.NotFoundException;
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

    public void existeUsuario(String nick){
        if (!this.usuarioRepository.existsByNick(nick)) {
            throw new NotFoundException("El usuario no existe");
        }
    }

    public void existeUsuario(Integer id){
        if (!this.usuarioRepository.existsById(id)) {
            throw new NotFoundException("El usuario no existe");
        }
    }

    public void existeUsuarioConEmailYIdDistinto(String email,Integer id){
        if (this.usuarioRepository.existsByEmailAndIdNot(email,id)) {
            throw new BadRequestException("El email se encuentra en uso");
        }
    }

    public void existeUsuarioConNickYIdDistinto(String nick,Integer id){
        if (this.usuarioRepository.existsByNickAndIdNot(nick,id)) {
            throw new BadRequestException("El nick se encuentra en uso");
        }
    }

    public void usuarioNulo(Object usuario) {
        if (usuario == null) {
            throw new BadRequestException("Error");
        }
    }
    public void esNulo(Object object,String msj) {
        if (object == null) {
            throw new NotFoundException(msj);
        }
    }

    public void noExisteUsuarioConId(Integer id) {
        if (!this.usuarioRepository.existsById(id)) {
            throw new NotFoundException("El usuario no existe");
        }
    }


    public void controlNombresUsuario(String nombres) {
        if (nombres==null||nombres.isEmpty()||nombres.isBlank()) {
            throw new BadRequestException("El campo nombres no puede estar vacio");
        }
        if(nombres.length()>160){
            throw new BadRequestException("El campo nombres no puede tener una extension de mas de 160 caracteres");
        }
    }

    public void controlApellidosUsuario(String apellidos) {
        if (apellidos==null||apellidos.isEmpty()||apellidos.isBlank()) {
            throw new BadRequestException("El campo apellidos no puede estar vacio");
        }
        if(apellidos.length()>160){
            throw new BadRequestException("El campo apellidos no puede tener una extension de mas de 160 caracteres");
        }
    }

    public void controlEmail(String email) {
        if (email==null||email.isEmpty()||email.isBlank()) {
            throw new BadRequestException("El email no puede estar vacio");
        }
        if(!email.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")){
            throw new BadRequestException("formato de email incorrecto");
        }
    }

    public void controlNick(String nick) {
        if (nick==null||nick.isEmpty()||nick.isBlank()) {
            throw new BadRequestException("El nick no puede estar vacio");
        }
        if(nick.length()>30){
            throw new BadRequestException("El nick no puede tener una longitud mayor 30 caracteres");
        }
    }

    public void controlPais(String pais) {
        if (pais==null||pais.isEmpty()||pais.isBlank()) {
            throw new BadRequestException("El campo pais no puede estar vacio");
        }
        if(pais.length()>60){
            throw new BadRequestException("El campo pais no puede tener una longitud mayor a 60 caracteres");
        }
    }
    public void controlProvincia(String prov) {
        if (prov==null||prov.isEmpty()||prov.isBlank()) {
            throw new BadRequestException("El email se encuentra en uso");
        }
        if(prov.length()>60){
            throw new BadRequestException("El campo provincia no puede tener una longitud mayor a 60 caracteres");
        }

    }
    public void controlPass(String pass) {
        if (pass==null||pass.isEmpty()||pass.isBlank()) {
            throw new BadRequestException("la contraseña no puede estar vacia");
        }
        if (pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,16}$")) {
            throw new BadRequestException(
                    "La contraseña debe tener al entre 8 y 16 caracteres, al menos un dígito, al menos una minúscula y al menos una mayúscula.");
        }

    }


}
