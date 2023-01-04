package argentinaprograma.cvdigital.repositories;

import argentinaprograma.cvdigital.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    public boolean existsByNick(String nick);

    public boolean existsByEmail(String email);

    public Usuario getByNick(String nick);
}
