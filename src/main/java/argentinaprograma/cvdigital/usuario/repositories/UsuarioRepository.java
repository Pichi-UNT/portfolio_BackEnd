package argentinaprograma.cvdigital.usuario.repositories;

import argentinaprograma.cvdigital.usuario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByNick(String nick);

    boolean existsByEmail(String email);

    Usuario getByNick(String nick);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByNickAndIdNot(String email, Integer id);

}
