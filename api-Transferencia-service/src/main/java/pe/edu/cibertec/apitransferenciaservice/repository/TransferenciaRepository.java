package pe.edu.cibertec.apitransferenciaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.apitransferenciaservice.entity.Transferencia;

import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Integer> {
    List<Transferencia> findByIdPuesto(Integer idPuesto);
}
