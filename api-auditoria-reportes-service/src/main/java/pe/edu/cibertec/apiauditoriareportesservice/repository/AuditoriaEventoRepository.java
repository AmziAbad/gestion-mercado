package pe.edu.cibertec.apiauditoriareportesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apiauditoriareportesservice.entity.AuditoriaEvento;

import java.util.List;

public interface AuditoriaEventoRepository extends JpaRepository<AuditoriaEvento, Integer> {

    List<AuditoriaEvento> findByEntidadAfectadaAndIdRegistroAfectado(String entidadAfectada, Integer idRegistroAfectado);
}
