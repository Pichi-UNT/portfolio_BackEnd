package argentinaprograma.cvdigital.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenException extends AuthenticationException {

    public TokenException(String msg) {
        super(msg);
    }
}
