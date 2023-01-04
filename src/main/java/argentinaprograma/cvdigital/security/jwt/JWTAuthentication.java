package argentinaprograma.cvdigital.security.jwt;

import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
@ToString
public class JWTAuthentication implements Authentication {

    private Integer id;
    private String username;
    private String jwt;
    private Object details;
    private Collection<? extends GrantedAuthority> authorities;


    public JWTAuthentication(String jwt,Integer id, String username, Collection<? extends GrantedAuthority> authorities) {
        this.id=id;
        this.jwt = jwt;
        this.username = username;
        this.authorities = authorities;
    }

    public JWTAuthentication(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return this.jwt;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }


    @Override
    public Object getPrincipal() {
        return this.id;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Cannot change authentication");
    }


    @Override
    public String getName() {
        return this.username;
    }

    public void setDetails(Object details) {
        this.details=details;
    }
}
