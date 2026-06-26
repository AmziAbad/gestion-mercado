package pe.edu.cibertec.apitesoreriaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apitesoreriaservice.entity.ConceptoCobro;

import java.util.List;

public interface ConceptoCobroRepository extends JpaRepository<ConceptoCobro, Integer> {

    boolean existsByNombreConceptoIgnoreCase(String nombreConcepto);

    List<ConceptoCobro> findByActivoTrue();
}
