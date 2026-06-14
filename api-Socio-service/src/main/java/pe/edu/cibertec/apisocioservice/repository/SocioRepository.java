package pe.edu.cibertec.apisocioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.cibertec.apisocioservice.entity.Socio;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Integer> {
    @Query("SELECT s FROM Socio s WHERE TRIM(s.dni) = :dni")
    Optional<Socio> findByDni(@Param("dni") String dni);
}
