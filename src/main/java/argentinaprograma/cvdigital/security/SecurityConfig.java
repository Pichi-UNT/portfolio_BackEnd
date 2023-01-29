package argentinaprograma.cvdigital.security;

//import argentinaprograma.cvdigital.security.filters.TokenFilter;


import argentinaprograma.cvdigital.security.filters.ExceptionFilter;
import argentinaprograma.cvdigital.security.filters.JWTFilter;
import argentinaprograma.cvdigital.security.handlers.MyAuthenticationEntryPoint;
import argentinaprograma.cvdigital.security.handlers.MyDeniedHandler;
import argentinaprograma.cvdigital.security.jwt.JWTAuthenticationProvider;
import argentinaprograma.cvdigital.security.jwt.JwtConfig;
import org.hibernate.annotations.ConverterRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Import(JwtConfig.class)
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JWTFilter JWTFilter;
    @Autowired
    private JWTAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    MyAuthenticationEntryPoint authEntryPoint;
    @Autowired
    MyDeniedHandler personFilter;
    @Autowired
    ExceptionFilter exceptionFilter;
//    @Autowired
//    JWTAuthenticationFilter jwtAuthenticationFilter;


    //
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_A > ROLE_U");
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    //
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilterBefore(JWTFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionFilter, argentinaprograma.cvdigital.security.filters.JWTFilter.class)
                .exceptionHandling().authenticationEntryPoint(authEntryPoint).accessDeniedHandler(personFilter).and()
                .authenticationProvider(jwtAuthenticationProvider)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .build();
    }



}
