package pe.edu.cibertec.apipagosservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;
import pe.edu.cibertec.apipagosservice.entity.EstadoPago;

import java.time.LocalDateTime;
import java.util.List;

public interface CuotaPagoRepository extends JpaRepository<CuotaPago, Integer> {

    boolean existsByIdPuestoAndIdServicioAndMesAndAnio(
            Integer idPuesto, Integer idServicio, int mes, int anio
    );
    boolean existsByIdPuestoAndEstado(Integer idPuesto, EstadoPago estado);

    @Query("SELECT SUM(c.monto) FROM CuotaPago c WHERE c.idPuesto = :idPuesto AND c.estado = 'PENDIENTE'")
    Double sumMontoPendienteByPuesto(@Param("idPuesto") Integer idPuesto);

    //Opcional: para listar cuotas de un puesto
    List<CuotaPago> findByIdPuesto(Integer idPuesto);

    List<CuotaPago> findByIdPuestoAndEstado(Integer idPuesto, EstadoPago estado);

    List<CuotaPago> findByEstado(EstadoPago estado);

    List<CuotaPago> findByEstadoAndFechaPagoBetween(
            EstadoPago estado, LocalDateTime desde, LocalDateTime hasta
    );
}
