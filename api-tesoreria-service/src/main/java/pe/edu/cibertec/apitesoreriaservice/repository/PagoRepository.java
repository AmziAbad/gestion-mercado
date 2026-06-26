package pe.edu.cibertec.apitesoreriaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apitesoreriaservice.entity.EstadoPago;
import pe.edu.cibertec.apitesoreriaservice.entity.Pago;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByIdTurnoAndEstadoPago(Integer idTurno, EstadoPago estadoPago);

    List<Pago> findByFechaPagoBetween(LocalDateTime desde, LocalDateTime hasta);

    Optional<Pago> findFirstByIdCuotaAndEstadoPago(Integer idCuota, EstadoPago estadoPago);
}
