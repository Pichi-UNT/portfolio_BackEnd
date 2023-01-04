package argentinaprograma.cvdigital.usuario.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthCredentials {
    private String nick;
    private String pass;
    private String codigoConfirmacion;




}
