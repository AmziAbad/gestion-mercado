package pe.edu.cibertec.apitransferenciaservice.service;

import pe.edu.cibertec.apitransferenciaservice.dto.TransferenciaRequest;
import pe.edu.cibertec.apitransferenciaservice.entity.Transferencia;

import java.util.List;
import java.util.Optional;

public interface TransferenciaService {
    List<Transferencia> listar();

    Optional<Transferencia> obtener(Integer id);

    List<Transferencia> listarPorPuesto(Integer idPuesto);

    Transferencia registrar(TransferenciaRequest request);
}
