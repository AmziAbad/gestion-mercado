package pe.edu.cibertec.apitesoreriaservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apitesoreriaservice.dto.DeudaResumenResponse;
import pe.edu.cibertec.apitesoreriaservice.dto.EstadoCuentaResponse;
import pe.edu.cibertec.apitesoreriaservice.service.TesoreriaService;

@RestController
@RequestMapping("/api/v1/estados-cuenta")
public class EstadoCuentaController {

    private final TesoreriaService tesoreriaService;

    public EstadoCuentaController(TesoreriaService tesoreriaService) {
        this.tesoreriaService = tesoreriaService;
    }

    @GetMapping("/puesto/{idPuesto}")
    public ResponseEntity<EstadoCuentaResponse> porPuesto(@PathVariable Integer idPuesto) {
        return ResponseEntity.ok(tesoreriaService.estadoCuentaPorPuesto(idPuesto));
    }

    @GetMapping("/puesto/{idPuesto}/resumen")
    public ResponseEntity<DeudaResumenResponse> resumenPorPuesto(@PathVariable Integer idPuesto) {
        return ResponseEntity.ok(tesoreriaService.resumenDeudaPorPuesto(idPuesto));
    }

    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<EstadoCuentaResponse> porSocio(@PathVariable Integer idSocio) {
        return ResponseEntity.ok(tesoreriaService.estadoCuentaPorSocio(idSocio));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<EstadoCuentaResponse> porDni(@PathVariable String dni) {
        return ResponseEntity.ok(tesoreriaService.estadoCuentaPorDni(dni));
    }
}
