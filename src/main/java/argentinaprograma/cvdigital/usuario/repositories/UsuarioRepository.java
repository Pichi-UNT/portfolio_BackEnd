package argentinaprograma.cvdigital.usuario.repositories;

import argentinaprograma.cvdigital.usuario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    public boolean existsByNick(String nick);

    public boolean existsByEmail(String email);

    public Usuario getByNick(String nick);


}
