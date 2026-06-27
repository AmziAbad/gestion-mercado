package pe.edu.cibertec.apiauditoriareportesservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaAnulacionRequest;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaAnulacionResponse;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaEventoRequest;
import pe.edu.cibertec.apiauditoriareportesservice.dto.AuditoriaEventoResponse;
import pe.edu.cibertec.apiauditoriareportesservice.service.AuditoriaReporteService;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

    private final AuditoriaReporteService auditoriaReporteService;

    public AuditoriaController(AuditoriaReporteService auditoriaReporteService) {
        this.auditoriaReporteService = auditoriaReporteService;
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<AuditoriaEventoResponse>> listarEventos() {
        return ResponseEntity.ok(auditoriaReporteService.listarEventos());
    }

    @GetMapping("/eventos/{entidadAfectada}/{idRegistroAfectado}")
    public ResponseEntity<List<AuditoriaEventoResponse>> listarEventosPorRegistro(
            @PathVariable String entidadAfectada,
            @PathVariable Integer idRegistroAfectado) {
        return ResponseEntity.ok(auditoriaReporteService.listarEventosPorRegistro(entidadAfectada, idRegistroAfectado));
    }

    @PostMapping("/eventos")
    public ResponseEntity<AuditoriaEventoResponse> registrarEvento(@Valid @RequestBody AuditoriaEventoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditoriaReporteService.registrarEvento(request));
    }

    @GetMapping("/anulaciones")
    public ResponseEntity<List<AuditoriaAnulacionResponse>> listarAnulaciones() {
        return ResponseEntity.ok(auditoriaReporteService.listarAnulaciones());
    }

    @GetMapping("/anulaciones/{tipoAnulacion}/{idRegistroAfectado}")
    public ResponseEntity<List<AuditoriaAnulacionResponse>> listarAnulacionesPorRegistro(
            @PathVariable String tipoAnulacion,
            @PathVariable Integer idRegistroAfectado) {
        return ResponseEntity.ok(auditoriaReporteService.listarAnulacionesPorRegistro(tipoAnulacion, idRegistroAfectado));
    }

    @PostMapping("/anulaciones")
    public ResponseEntity<AuditoriaAnulacionResponse> registrarAnulacion(@Valid @RequestBody AuditoriaAnulacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditoriaReporteService.registrarAnulacion(request));
    }
}
