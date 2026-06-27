package pe.edu.cibertec.apipatrimonioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apipatrimonioservice.entity.Socio;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Integer> {

    Optional<Socio> findByDni(String dni);

    boolean existsByDni(String dni);

    boolean existsByRuc(String ruc);

    boolean existsByCorreo(String correo);
}
