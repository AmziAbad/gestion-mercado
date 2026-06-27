package pe.edu.cibertec.apipatrimonioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apipatrimonioservice.entity.ContratoOcupacion;
import pe.edu.cibertec.apipatrimonioservice.entity.EstadoContrato;

import java.util.List;
import java.util.Optional;

public interface ContratoOcupacionRepository extends JpaRepository<ContratoOcupacion, Integer> {

    Optional<ContratoOcupacion> findFirstByIdPuestoAndEstadoContrato(Integer idPuesto, EstadoContrato estadoContrato);

    List<ContratoOcupacion> findByIdSocioAndEstadoContrato(Integer idSocio, EstadoContrato estadoContrato);

    List<ContratoOcupacion> findByEstadoContrato(EstadoContrato estadoContrato);
}
