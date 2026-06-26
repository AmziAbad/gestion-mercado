package pe.edu.cibertec.apitesoreriaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apitesoreriaservice.entity.CuotaDeuda;
import pe.edu.cibertec.apitesoreriaservice.entity.EstadoCuota;

import java.util.List;

public interface CuotaDeudaRepository extends JpaRepository<CuotaDeuda, Integer> {

    List<CuotaDeuda> findByIdPuesto(Integer idPuesto);

    List<CuotaDeuda> findByIdPuestoAndEstadoCuota(Integer idPuesto, EstadoCuota estadoCuota);

    List<CuotaDeuda> findByIdContrato(Integer idContrato);

    List<CuotaDeuda> findByEstadoCuota(EstadoCuota estadoCuota);

    boolean existsByIdContratoAndIdConceptoAndPeriodoMesAndPeriodoAnio(
            Integer idContrato,
            Integer idConcepto,
            Integer periodoMes,
            Integer periodoAnio
    );
}
