package pe.edu.cibertec.apitesoreriaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apitesoreriaservice.entity.CuotaDeuda;
import pe.edu.cibertec.apitesoreriaservice.entity.EstadoCuota;

import java.time.LocalDate;
import java.util.List;

public interface CuotaDeudaRepository extends JpaRepository<CuotaDeuda, Integer> {

    List<CuotaDeuda> findByIdPuestoAndEstadoCuota(Integer idPuesto, EstadoCuota estadoCuota);

    List<CuotaDeuda> findByEstadoCuotaAndFechaVencimientoBefore(EstadoCuota estadoCuota, LocalDate fechaVencimiento);

    boolean existsByIdContratoAndIdConceptoAndPeriodoMesAndPeriodoAnio(
            Integer idContrato,
            Integer idConcepto,
            Integer periodoMes,
            Integer periodoAnio
    );
}
