package pe.edu.cibertec.apipatrimonioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apipatrimonioservice.entity.EstadoPuesto;
import pe.edu.cibertec.apipatrimonioservice.entity.Puesto;

import java.util.List;
import java.util.Optional;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    Optional<Puesto> findByCodigoPuesto(String codigoPuesto);

    boolean existsByCodigoPuesto(String codigoPuesto);

    List<Puesto> findByEstadoPuesto(EstadoPuesto estadoPuesto);

    List<Puesto> findByPabellon(String pabellon);
}
