package pe.edu.cibertec.apipagosservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.cibertec.apipagosservice.dto.ServicioDTO;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;
import pe.edu.cibertec.apipagosservice.entity.EstadoPago;

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
}
