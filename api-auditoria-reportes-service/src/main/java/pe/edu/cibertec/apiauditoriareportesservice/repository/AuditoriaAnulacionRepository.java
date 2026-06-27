package pe.edu.cibertec.apiauditoriareportesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apiauditoriareportesservice.entity.AuditoriaAnulacion;
import pe.edu.cibertec.apiauditoriareportesservice.entity.TipoAnulacion;

import java.util.List;

public interface AuditoriaAnulacionRepository extends JpaRepository<AuditoriaAnulacion, Integer> {

    List<AuditoriaAnulacion> findByTipoAnulacionAndIdRegistroAfectado(TipoAnulacion tipoAnulacion, Integer idRegistroAfectado);
}
