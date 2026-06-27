package pe.edu.cibertec.apitesoreriaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apitesoreriaservice.entity.EstadoTurno;
import pe.edu.cibertec.apitesoreriaservice.entity.TurnoCaja;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TurnoCajaRepository extends JpaRepository<TurnoCaja, Integer> {

    Optional<TurnoCaja> findFirstByIdUsuarioAndEstadoTurno(Integer idUsuario, EstadoTurno estadoTurno);

    List<TurnoCaja> findByFechaAperturaBetween(LocalDateTime desde, LocalDateTime hasta);
}
