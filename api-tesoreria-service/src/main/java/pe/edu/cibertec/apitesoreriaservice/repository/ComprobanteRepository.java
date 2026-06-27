package pe.edu.cibertec.apitesoreriaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apitesoreriaservice.entity.Comprobante;

import java.util.Optional;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Integer> {

    Optional<Comprobante> findByIdPago(Integer idPago);

    Optional<Comprobante> findByIdCuota(Integer idCuota);

    Optional<Comprobante> findByNumeroComprobante(String numeroComprobante);
}
