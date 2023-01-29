package argentinaprograma.cvdigital.usuario.services;

import argentinaprograma.cvdigital.exceptions.BadRequestException;
import argentinaprograma.cvdigital.exceptions.NotFoundException;
import argentinaprograma.cvdigital.usuario.DTO.UsuarioAuthCredentialsDTO;
import argentinaprograma.cvdigital.usuario.DTO.UsuarioFullDTO;
import argentinaprograma.cvdigital.usuario.models.Estado;
import argentinaprograma.cvdigital.usuario.models.Rol;
import argentinaprograma.cvdigital.usuario.models.Usuario;
import argentinaprograma.cvdigital.usuario.repositories.UsuarioRepository;
import argentinaprograma.cvdigital.usuario.services.interfaces.AuthService;
import argentinaprograma.cvdigital.utils.ConfirmationCodeGenerator;
import io.jsonwebtoken.JwtBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AuthServiceImp implements AuthService {

    // Repositorio para interactuar con la base de datos de usuarios
    private UsuarioRepository usuarioRepository;

    private UsuarioValidator usuarioValidator;
    // Generador de tokens JWT
    private JwtBuilder jwtBuilder;

    private ModelMapper modelMapper;

    private EmailService emailService;

    @Autowired
    public AuthServiceImp(UsuarioRepository usuarioRepository, UsuarioValidator usuarioValidator, JwtBuilder jwtBuilder,EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioValidator = usuarioValidator;
        this.jwtBuilder = jwtBuilder;
        this.modelMapper = new ModelMapper();
        this.emailService=emailService;
    }

    // Registra un usuario nuevo en la base de datos
    public void registrarUsuario(UsuarioFullDTO usuarioFullDTO) {
        Usuario usuario =modelMapper.map(usuarioFullDTO,Usuario.class);

        // Establece el rol del usuario como "U"(USUARIO) y el estado como "P" ("PENDIENTE")
        usuario.setRol(Rol.USER);
        usuario.setEstado(Estado.PENDIENTE);

        // Genera un código de confirmación y establece la fecha de generación del código en la fecha actual
        String codigoConfirmacion = ConfirmationCodeGenerator.generateCode();
        usuario.setCodigoConfirmacion(codigoConfirmacion);
        usuario.setFechaGeneracionCodigo(LocalDateTime.now());

        // Almacena el usuario en la base de datos
        this.crearUsuario(usuario);
        this.emailService.sendEmail(usuario.getEmail(), "Confirmacion de Registro", usuario.getCodigoConfirmacion());
    }

    // Confirma el registro de un usuario estableciendo su estado como "A" ("ACTIVO")
    public void confirmarRegistro(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO) {
        // Obtiene el usuario y valida su código de confirmación
        this.usuarioValidator.controlNick(usuarioAuthCredentialsDTO.getNick());
        this.usuarioValidator.existeUsuario(usuarioAuthCredentialsDTO.getNick());
        Usuario usuarioObtenido = getUsuarioConCodigoConfirmacionValidado(usuarioAuthCredentialsDTO);

        // Establece el estado del usuario como "A"
        usuarioObtenido.setEstado(Estado.ACTIVADO);

        // Actualiza el usuario en la base de datos
        this.usuarioRepository.save(usuarioObtenido);
    }

    // Inicia sesión de un usuario y devuelve un token JWT
    public String iniciarSesion(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO) {
        // Obtiene el usuario con el nombre de usuario especificado
        Usuario usuarioObtenido = this.usuarioRepository.getByNick(usuarioAuthCredentialsDTO.getNick());

        if (!usuarioObtenido.getPass().equals(usuarioAuthCredentialsDTO.getPass()) || usuarioObtenido==null) {
            throw new BadRequestException("Usuario o Contraseña incorrectos");
        }
        // Si el usuario está en estado "P", lanza una excepción BadRequestException
        if (usuarioObtenido.getEstado().equals(Estado.PENDIENTE)) {
            throw new BadRequestException("Email sin confirmar");
        }

        // Si el usuario está en un estado válido, genera y devuelve un token JWT
        return jwtBuilder.setSubject(usuarioObtenido.getNick())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
    }

    // Solicita el restablecimiento de contraseña de un usuario
    public void solicitarRestablecimientoDeContraseña(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO) {
        // Obtiene el usuario con el nombre de usuario especificado
        Usuario usuarioObtenido = this.usuarioRepository.getByNick(usuarioAuthCredentialsDTO.getNick());
        this.usuarioValidator.esNulo(usuarioObtenido,"Usuario inexistente");
        // Genera un código de confirmación y establece la fecha de generación del código en la fecha actual
        String codigoConfirmacion = ConfirmationCodeGenerator.generateCode();
        usuarioObtenido.setCodigoConfirmacion(codigoConfirmacion);
        usuarioObtenido.setFechaGeneracionCodigo(LocalDateTime.now());

        // Actualiza el usuario en la base de datos
        this.usuarioRepository.save(usuarioObtenido);
    }

    // Restablece la contraseña de un usuario
    public void RestablecimientoDeContraseña(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO) {
        // Obtiene el usuario y valida su código de confirmación
        Usuario usuarioObtenido = getUsuarioConCodigoConfirmacionValidado(usuarioAuthCredentialsDTO);
        usuarioValidator.controlPass(usuarioAuthCredentialsDTO.getPass());
        // Establece la nueva contraseña del usuario
        usuarioObtenido.setPass(usuarioAuthCredentialsDTO.getPass());

        // Actualiza el usuario en la base de datos
        this.usuarioRepository.save(usuarioObtenido);
    }

    private Usuario getUsuarioConCodigoConfirmacionValidado(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO) {
        Usuario usuarioObtenido = this.usuarioRepository.getByNick(usuarioAuthCredentialsDTO.getNick());
        this.usuarioValidator.esNulo(usuarioObtenido,"usuario inexistente");
        this.validarCodigoConfirmacion(usuarioAuthCredentialsDTO,usuarioObtenido);
        usuarioObtenido.setCodigoConfirmacion(null);
        usuarioObtenido.setFechaGeneracionCodigo(null);
        return usuarioObtenido;
    }

    private void validarCodigoConfirmacion(UsuarioAuthCredentialsDTO usuarioAuthCredentialsDTO, Usuario usuario){
        if(usuarioAuthCredentialsDTO.getCodigoConfirmacion()==null
                || usuarioAuthCredentialsDTO.getCodigoConfirmacion().isEmpty()
                || usuarioAuthCredentialsDTO.getCodigoConfirmacion().isBlank()){
            throw new BadRequestException("Codigo de confirmación no válido.");
        }
        if(usuario.getCodigoConfirmacion()==null ||usuario.getCodigoConfirmacion().isBlank() ||usuario.getCodigoConfirmacion().isEmpty()){
            throw new NotFoundException("Codigo de confirmacion no ha sido generado.");
        }
        if (!usuario.getCodigoConfirmacion().equals(usuarioAuthCredentialsDTO.getCodigoConfirmacion()) ||
                LocalDateTime.now().isAfter(usuario.getFechaGeneracionCodigo().plusMinutes(20))) {
            throw new BadRequestException("Codigo de confirmacion no válido.");
        }
    }

    private Usuario crearUsuario(Usuario usuario) {
        this.validarUsuarioParaCreacion(usuario);
        Usuario usuarioObtenido = this.usuarioRepository.save(usuario);
        this.usuarioValidator.usuarioNulo(usuarioObtenido);
        return usuarioObtenido;
    }

    private void validarUsuarioParaCreacion(Usuario usuario) {
        this.usuarioValidator.usuarioNulo(usuario);
        this.usuarioValidator.controlNick(usuario.getNick());
        this.usuarioValidator.existeUsuarioConNick(usuario.getNick());
        this.usuarioValidator.existeUsuarioConEmail(usuario.getEmail());
        this.usuarioValidator.controlApellidosUsuario(usuario.getApellidos());
        this.usuarioValidator.controlNombresUsuario(usuario.getNombres());
        this.usuarioValidator.controlPass(usuario.getPass());
        this.usuarioValidator.controlProvincia(usuario.getProvincia());
        this.usuarioValidator.controlPais(usuario.getPais());
    }

}
