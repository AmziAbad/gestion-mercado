package pe.edu.cibertec.apipuestoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.cibertec.apipuestoservice.entity.EstadoPuesto;
import pe.edu.cibertec.apipuestoservice.entity.Puesto;

import java.util.List;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {
    @Query("SELECT p FROM Puesto p ORDER BY p.numeroPuesto ASC")
    List<Puesto> findAllOrdered();

    List<Puesto> findByEstadoPuesto(EstadoPuesto estadoPuesto);

    List<Puesto> findByIdSocioActual(Integer idSocioActual);

    List<Puesto> findByPabellonIgnoreCase(String pabellon);
}
