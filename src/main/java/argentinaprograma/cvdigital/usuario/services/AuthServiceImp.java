package argentinaprograma.cvdigital.usuario.services;

import argentinaprograma.cvdigital.exceptions.BadRequestException;
import argentinaprograma.cvdigital.usuario.DTO.AuthCredentials;
import argentinaprograma.cvdigital.usuario.models.Usuario;
import argentinaprograma.cvdigital.usuario.services.interfaces.AuthService;
import argentinaprograma.cvdigital.usuario.services.interfaces.UsuarioService;
import argentinaprograma.cvdigital.utils.ConfirmationCodeGenerator;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AuthServiceImp implements AuthService {

    // Repositorio para interactuar con la base de datos de usuarios
    private UsuarioService usuarioService;

    private UsuarioValidator usuarioValidator;
    // Generador de tokens JWT
    private JwtBuilder jwtBuilder;

    @Autowired
    public AuthServiceImp(UsuarioService usuarioService, UsuarioValidator usuarioValidator, JwtBuilder jwtBuilder) {
        this.usuarioService = usuarioService;
        this.usuarioValidator = usuarioValidator;
        this.jwtBuilder = jwtBuilder;
    }

    // Registra un usuario nuevo en la base de datos
    public void registrarUsuario(Usuario usuario) {

        // Verifica si ya existe un usuario con el mismo nombre de usuario
        this.usuarioValidator.existeUsuarioConNick(usuario.getNick());

        // Verifica si ya existe un usuario con el mismo correo electrónico
        this.usuarioValidator.existeUsuarioConEmail(usuario.getEmail());

        // Establece el rol del usuario como "U"(USUARIO) y el estado como "P" ("PENDIENTE")
        usuario.setRol("U");
        usuario.setEstado("P");

        // Genera un código de confirmación y establece la fecha de generación del código en la fecha actual
        String codigoConfirmacion = ConfirmationCodeGenerator.generateCode();
        usuario.setCodigoConfirmacion(codigoConfirmacion);
        usuario.setFechaGeneracionCodigo(LocalDateTime.now());

        // Almacena el usuario en la base de datos
        this.usuarioService.CreateUsuario(usuario);
    }

    // Confirma el registro de un usuario estableciendo su estado como "A" ("ACTIVO")
    public void confirmarRegistro(AuthCredentials authCredentials) {
        // Obtiene el usuario y valida su código de confirmación
        Usuario usuarioObtenido = getUsuarioConCodigoConfirmacionValidado(authCredentials);

        // Establece el estado del usuario como "A"
        usuarioObtenido.setEstado("A");

        // Actualiza el usuario en la base de datos
        this.usuarioService.updateUsuario(usuarioObtenido);
    }

    // Inicia sesión de un usuario y devuelve un token JWT
    public String iniciarSesion(AuthCredentials authCredentials) {
        // Obtiene el usuario con el nombre de usuario especificado
        Usuario usuarioObtenido = this.usuarioService.getUsuarioByNick(authCredentials.getNick());

        // Si el usuario no existe o la contraseña no coincide, lanza una excepción BadRequestException
        if (!usuarioObtenido.getPass().equals(authCredentials.getPass())) {
            throw new BadRequestException("Usuario o Contraseña incorrectos");
        }
        // Si el usuario está en estado "P", lanza una excepción BadRequestException
        if (usuarioObtenido.getEstado().equals("P")) {
            throw new BadRequestException("Email sin confirmar");
        }

        // Si el usuario está en un estado válido, genera y devuelve un token JWT
        return jwtBuilder.setSubject(usuarioObtenido.getNick())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
    }

    // Solicita el restablecimiento de contraseña de un usuario
    public void solicitarRestablecimientoDeContraseña(AuthCredentials authCredentials) {
        // Obtiene el usuario con el nombre de usuario especificado
        Usuario usuarioObtenido = this.usuarioService.getUsuarioByNick(authCredentials.getNick());

        // Genera un código de confirmación y establece la fecha de generación del código en la fecha actual
        String codigoConfirmacion = ConfirmationCodeGenerator.generateCode();
        usuarioObtenido.setCodigoConfirmacion(codigoConfirmacion);
        usuarioObtenido.setFechaGeneracionCodigo(LocalDateTime.now());

        // Actualiza el usuario en la base de datos
        this.usuarioService.updateUsuario(usuarioObtenido);
    }

    // Restablece la contraseña de un usuario
    public void RestablecimientoDeContraseña(AuthCredentials authCredentials) {
        // Obtiene el usuario y valida su código de confirmación
        Usuario usuarioObtenido = getUsuarioConCodigoConfirmacionValidado(authCredentials);

        // Establece la nueva contraseña del usuario
        usuarioObtenido.setPass(authCredentials.getPass());

        // Actualiza el usuario en la base de datos
        this.usuarioService.updateUsuario(usuarioObtenido);
    }

    private Usuario getUsuarioConCodigoConfirmacionValidado(AuthCredentials authCredentials) {
        Usuario usuarioObtenido = this.usuarioService.getUsuarioByNick(authCredentials.getNick());
        this.validarCodigoConfirmacion(authCredentials,usuarioObtenido);
        usuarioObtenido.setCodigoConfirmacion(null);
        usuarioObtenido.setFechaGeneracionCodigo(null);
        return usuarioObtenido;
    }

    private void validarCodigoConfirmacion(AuthCredentials authCredentials,Usuario usuario){
        if (!usuario.getCodigoConfirmacion().equals(authCredentials.getCodigoConfirmacion()) ||
                LocalDateTime.now().isAfter(usuario.getFechaGeneracionCodigo().plusMinutes(20))) {
            throw new BadRequestException("Codigo de confirmacion no válido");
        }
    }






}
