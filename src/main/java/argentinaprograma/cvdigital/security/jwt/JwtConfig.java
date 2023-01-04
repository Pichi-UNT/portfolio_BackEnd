package argentinaprograma.cvdigital.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret.key}")
    private String signingKey;

    @Bean
    public JwtBuilder jwtBuilder() {
        // Creamos un builder de JWT y le asignamos la clave de firma leída
        // del archivo de propiedades

        JwtBuilder builder = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey.getBytes()));
        return builder;
    }

    @Bean
    public JwtParserBuilder jwtParserBuilder() {
        // Creamos un ParserBuilder de JWT y le asignamos la clave de firma leída
        // del archivo de propiedades

        JwtParserBuilder builder = Jwts.parserBuilder().setSigningKey(signingKey.getBytes());
        return builder;
    }
}
