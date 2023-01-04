package argentinaprograma.cvdigital.usuario.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private Integer id;
    @Column(name = "Nombres")
    private String nombres;
    @Column(name = "Apellidos")
    private String apellidos;
    @Column(name = "Email")
    private String email;
    @Column(name = "Nick")
    private String nick;
    @Column(name = "Pass")
    private String pass;
    @Column(name = "Telefono")
    private String telefono;
    @Column(name = "Direccion")
    private String direccion;
    @Column(name = "Pais")
    private String pais;
    @Column(name = "Provincia")
    private String provincia;
    @Column(name = "Rol")
//    @Enumerated(EnumType.STRING)
    private String rol;
    @Column(name = "EstadoUsuario")
    private String estado;
    @Column(name = "CodigoConfirmacion")
    private String codigoConfirmacion;
    @Column(columnDefinition = "DATETIME", name = "FechaGeneracionCodigo")
    private LocalDateTime fechaGeneracionCodigo;


}
