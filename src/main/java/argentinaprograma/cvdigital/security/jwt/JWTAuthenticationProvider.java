package argentinaprograma.cvdigital.security.jwt;

import argentinaprograma.cvdigital.usuario.models.Usuario;
import argentinaprograma.cvdigital.usuario.repositories.UsuarioRepository;
import argentinaprograma.cvdigital.exceptions.TokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {


    private JwtParserBuilder jwtParserBuilder;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public JWTAuthenticationProvider(JwtParserBuilder jwtParserBuilder, UsuarioRepository usuarioRepository) {
        this.jwtParserBuilder = jwtParserBuilder;
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public Authentication authenticate(Authentication authentication) {
        String jwt = (String) authentication.getCredentials();

        Jws<Claims> claims = this.GetClaims(jwt);

        String username = claims.getBody().getSubject();

        if (username == null) {
            throw new TokenException("Token invalido");
        }
        if (claims.getBody().getExpiration().before(new Date())) {
            throw new TokenException("Token Expirado");
        }
        Usuario usuario = this.usuarioRepository.getByNick(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario inexistente");
        }

        return new JWTAuthentication(jwt, usuario.getId(), usuario.getNick(), Collections.singleton(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())));

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthentication.class.isAssignableFrom(authentication);
    }


    private Jws<Claims> GetClaims(String authToken) {
        try {
            return jwtParserBuilder.build().parseClaimsJws(authToken);
        } catch (JwtException e) {
            throw new TokenException(e.getClass().getSimpleName()+": "+e.getMessage());
        }
    }
}
