package argentinaprograma.cvdigital.security.filters;

import argentinaprograma.cvdigital.security.jwt.JWTAuthentication;
import argentinaprograma.cvdigital.security.jwt.JWTAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.reflect.Method;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    ApplicationContext context;

    @Autowired
    public JWTFilter(JWTAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(shouldNotFilter(request)){
            filterChain.doFilter(request, response);
            return;
        }

        // obtener el token JWT de la cabecera de la solicitud
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = header.substring(7);
        JWTAuthentication authentication = new JWTAuthentication(jwt);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


        Authentication authenticationVerified = this.jwtAuthenticationProvider.authenticate(authentication);
        // establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authenticationVerified);

        // continuar con el flujo de filtros
        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);

        HandlerMethod handlerMethod = null;
        try {
            handlerMethod = (HandlerMethod)requestMappingHandlerMapping.getHandler(request).getHandler();
            Method method=handlerMethod.getMethod();
            PreAuthorize preAuthorizeMethod= AnnotationUtils.findAnnotation(method, PreAuthorize.class);
            Class<?> declaringClass = method.getDeclaringClass();
            PreAuthorize preAuthorizeClass = AnnotationUtils.findAnnotation(declaringClass, PreAuthorize.class);
            if((preAuthorizeClass!=null && preAuthorizeClass.value().equals("permitAll()"))
                    ||(preAuthorizeMethod!=null && preAuthorizeMethod.value().equals("permitAll()"))){
                return true;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return false;
    }


}
