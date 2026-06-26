package pe.edu.cibertec.apiauthservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apiauthservice.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByDni(String dni);

    boolean existsByCorreo(String correo);
}
