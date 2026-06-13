package pe.edu.cibertec.apisocioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apisocioservice.entity.Socio;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Integer> {
    Optional<Socio> findByDni(String dni);
}
