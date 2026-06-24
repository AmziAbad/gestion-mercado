package pe.edu.cibertec.apipagosservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apipagosservice.dto.ComprobanteDTO;
import pe.edu.cibertec.apipagosservice.dto.DetalleDeudaDTO;
import pe.edu.cibertec.apipagosservice.dto.DetalleFlujoCajaDTO;
import pe.edu.cibertec.apipagosservice.dto.DeudaDTO;
import pe.edu.cibertec.apipagosservice.dto.DeudorDTO;
import pe.edu.cibertec.apipagosservice.dto.EstadoDeudoresDTO;
import pe.edu.cibertec.apipagosservice.dto.FlujoCajaDiarioDTO;
import pe.edu.cibertec.apipagosservice.dto.PuestoDTO;
import pe.edu.cibertec.apipagosservice.dto.ServicioDTO;
import pe.edu.cibertec.apipagosservice.dto.SocioDTO;
import pe.edu.cibertec.apipagosservice.entity.CuotaPago;
import pe.edu.cibertec.apipagosservice.entity.EstadoPago;
import pe.edu.cibertec.apipagosservice.entity.MetodoPago;
import pe.edu.cibertec.apipagosservice.remote.client.PuestoClient;
import pe.edu.cibertec.apipagosservice.remote.client.ServicioClient;
import pe.edu.cibertec.apipagosservice.remote.client.SocioClient;
import pe.edu.cibertec.apipagosservice.repository.CuotaPagoRepository;
import pe.edu.cibertec.apipagosservice.service.PagoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {
    private final ServicioClient servicioClient;
    private final CuotaPagoRepository cuotaRepo;
    private final PuestoClient puestoClient;
    private final SocioClient socioClient;

    @Override
    public List<CuotaPago> listarPagos() {
        return cuotaRepo.findAll();
    }

    @Override
    @Transactional
    public int generarCuotasMensuales(int mes, int anio) {
        int cont = 0;

        List<ServicioDTO> servicios = servicioClient.getServiciosActivos();
        List<PuestoDTO> puestosOcupados = puestoClient.getPuestosOcupados();

        if (puestosOcupados.isEmpty()) return 0;

        for (ServicioDTO serv : servicios) {
            double montoCuota;
            if ("FIJO".equals(serv.getTipoCobro())) {
                montoCuota = serv.getMontoFijoPuesto();
            } else {
                montoCuota = serv.getCostoTotalExterno() / puestosOcupados.size();
            }

            for (PuestoDTO puesto : puestosOcupados) {
                boolean existe = cuotaRepo.existsByIdPuestoAndIdServicioAndMesAndAnio(
                        puesto.getIdPuesto(), serv.getIdServicio(), mes, anio
                );

                if (!existe) {
                    CuotaPago cuota = CuotaPago.builder()
                            .idPuesto(puesto.getIdPuesto())
                            .idServicio(serv.getIdServicio())
                            .monto(montoCuota)
                            .mes(mes)
                            .anio(anio)
                            .estado(EstadoPago.PENDIENTE)
                            .build();
                    cuotaRepo.save(cuota);
                    cont++;
                }
            }
        }
        return cont;
    }

    @Override
    public CuotaPago pagarCuota(Integer id, Map<String, String> pago) {
        return cuotaRepo.findById(id).map(c -> {
            if (c.getEstado() == EstadoPago.PAGADO
                    || c.getEstado() == EstadoPago.EXONERADO
                    || c.getEstado() == EstadoPago.ANULADO) {
                throw new RuntimeException("La cuota ya está pagada, exonerada o anulada");
            }

            c.setEstado(EstadoPago.PAGADO);
            c.setFechaPago(LocalDateTime.now());

            String metodo = pago != null ? pago.get("metodoPago") : null;
            if (metodo != null && !metodo.isBlank()) {
                c.setMetodoPago(MetodoPago.valueOf(metodo));
            } else {
                c.setMetodoPago(MetodoPago.EFECTIVO);
            }

            if (pago != null && pago.get("numeroOperacion") != null && !pago.get("numeroOperacion").isBlank()) {
                c.setNumeroOperacion(pago.get("numeroOperacion"));
            }
            c.setNumeroComprobante(generarNumeroComprobante(c));

            return cuotaRepo.save(c);
        }).orElseThrow(() -> new RuntimeException("Cuota no encontrada"));
    }

    @Override
    public List<CuotaPago> listarCuotasPorPuesto(Integer idPuesto) {
        return cuotaRepo.findByIdPuesto(idPuesto);
    }

    @Override
    public List<CuotaPago> listarCuotasPendientesPorPuesto(Integer idPuesto) {
        return cuotaRepo.findByIdPuestoAndEstado(idPuesto, EstadoPago.PENDIENTE);
    }

    @Override
    public DeudaDTO obtenerDeudaPorPuesto(Integer idPuesto) {
        Double deuda = cuotaRepo.sumMontoPendienteByPuesto(idPuesto);
        deuda = deuda != null ? deuda : 0.0;
        return DeudaDTO.builder()
                .idPuesto(idPuesto)
                .totalDeuda(deuda)
                .tieneDeuda(deuda > 0)
                .build();
    }

    @Override
    public DeudaDTO obtenerDeudaPorSocio(Integer idSocio) {
        double total = puestoClient.getPuestosPorSocio(idSocio).stream()
                .map(PuestoDTO::getIdPuesto)
                .map(this::obtenerDeudaPorPuesto)
                .mapToDouble(DeudaDTO::getTotalDeuda)
                .sum();

        return DeudaDTO.builder()
                .idSocio(idSocio)
                .totalDeuda(total)
                .tieneDeuda(total > 0)
                .build();
    }

    @Override
    public CuotaPago generarDeudaEspecifica(Integer idPuesto, Integer idServicio, Double monto, Integer mes, Integer anio) {
        CuotaPago cuota = CuotaPago.builder()
                .idPuesto(idPuesto)
                .idServicio(idServicio)
                .monto(monto)
                .mes(mes)
                .anio(anio)
                .estado(EstadoPago.PENDIENTE)
                .build();
        return cuotaRepo.save(cuota);
    }

    @Override
    public CuotaPago exonerarCuota(Integer id, String motivo) {
        return cuotaRepo.findById(id).map(c -> {
            if (c.getEstado() == EstadoPago.PAGADO) {
                throw new RuntimeException("No se puede exonerar una cuota que ya está pagada");
            }
            if (c.getEstado() == EstadoPago.ANULADO) {
                throw new RuntimeException("No se puede exonerar una cuota anulada");
            }
            c.setEstado(EstadoPago.EXONERADO);
            c.setMotivoExoneracion(motivo);
            c.setFechaExoneracion(LocalDateTime.now());
            return cuotaRepo.save(c);
        }).orElseThrow(() -> new RuntimeException("Cuota no encontrada"));
    }

    @Override
    public CuotaPago anularCuota(Integer id, String motivo) {
        return cuotaRepo.findById(id).map(c -> {
            validarCuotaAnulable(c);
            c.setEstado(EstadoPago.ANULADO);
            c.setMotivoAnulacion(motivo);
            c.setFechaAnulacion(LocalDateTime.now());
            return cuotaRepo.save(c);
        }).orElseThrow(() -> new RuntimeException("Cuota no encontrada"));
    }

    @Override
    @Transactional
    public CuotaPago anularYReemplazarCuota(Integer id, String motivo, Integer idServicio, Double monto,
                                            Integer mes, Integer anio) {
        CuotaPago cuotaOriginal = cuotaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));

        validarCuotaAnulable(cuotaOriginal);

        cuotaOriginal.setEstado(EstadoPago.ANULADO);
        cuotaOriginal.setMotivoAnulacion(motivo);
        cuotaOriginal.setFechaAnulacion(LocalDateTime.now());

        CuotaPago cuotaNueva = CuotaPago.builder()
                .idPuesto(cuotaOriginal.getIdPuesto())
                .idServicio(idServicio != null ? idServicio : cuotaOriginal.getIdServicio())
                .monto(monto != null ? monto : cuotaOriginal.getMonto())
                .mes(mes != null ? mes : cuotaOriginal.getMes())
                .anio(anio != null ? anio : cuotaOriginal.getAnio())
                .estado(EstadoPago.PENDIENTE)
                .idCuotaOrigen(cuotaOriginal.getIdCuota())
                .build();

        cuotaNueva = cuotaRepo.save(cuotaNueva);
        cuotaOriginal.setIdCuotaReemplazo(cuotaNueva.getIdCuota());
        cuotaRepo.save(cuotaOriginal);

        return cuotaNueva;
    }

    @Override
    public CuotaPago revertirPago(Integer id, String motivo) {
        return cuotaRepo.findById(id).map(c -> {
            if (c.getEstado() != EstadoPago.PAGADO) {
                throw new RuntimeException("Solo se puede revertir una cuota pagada");
            }
            c.setEstado(EstadoPago.PENDIENTE);
            c.setMotivoAnulacionPago(motivo);
            c.setFechaAnulacionPago(LocalDateTime.now());
            c.setFechaPago(null);
            c.setMetodoPago(null);
            c.setNumeroOperacion(null);
            c.setNumeroComprobante(null);
            c.setMotivoExoneracion(null);
            c.setFechaExoneracion(null);
            return cuotaRepo.save(c);
        }).orElseThrow(() -> new RuntimeException("Cuota no encontrada"));
    }

    @Override
    public ComprobanteDTO generarComprobante(Integer idCuota) {
        CuotaPago cuota = cuotaRepo.findById(idCuota)
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));

        if (cuota.getEstado() != EstadoPago.PAGADO) {
            throw new RuntimeException("No se puede generar comprobante de una cuota que no ha sido pagada");
        }

        String numeroComprobante = cuota.getNumeroComprobante();
        if (numeroComprobante == null || numeroComprobante.isBlank()) {
            numeroComprobante = generarNumeroComprobante(cuota);
        }

        String numeroPuesto = null;
        String estadoPuesto = null;
        String detallePuesto = "Puesto asociado ID: " + cuota.getIdPuesto();
        String nombreServicio = null;
        String detalleServicio = "Servicio ID: " + cuota.getIdServicio();

        try {
            PuestoDTO puesto = puestoClient.getPuestoById(cuota.getIdPuesto());
            if (puesto != null) {
                numeroPuesto = puesto.getNumeroPuesto();
                estadoPuesto = puesto.getEstadoPuesto();
                detallePuesto = "Puesto " + numeroPuesto + " (" + estadoPuesto + ")";
            }
        } catch (Exception e) {
            // Se mantiene el detalle por ID si el microservicio de puestos no responde.
        }

        try {
            ServicioDTO servicio = servicioClient.getServicioById(cuota.getIdServicio());
            if (servicio != null) {
                nombreServicio = servicio.getNombreServicio();
                detalleServicio = nombreServicio;
            }
        } catch (Exception e) {
            // Se mantiene el detalle por ID si el microservicio de servicios no responde.
        }

        return ComprobanteDTO.builder()
                .titulo("COMPROBANTE DE PAGO - ASOCIACIÓN DE COMERCIANTES")
                .idCuota(cuota.getIdCuota())
                .numeroOperacion(cuota.getNumeroOperacion())
                .numeroComprobante(numeroComprobante)
                .fechaEmision(cuota.getFechaPago())
                .montoPagado(cuota.getMonto())
                .metodoPago(cuota.getMetodoPago())
                .idPuesto(cuota.getIdPuesto())
                .numeroPuesto(numeroPuesto)
                .estadoPuesto(estadoPuesto)
                .idServicio(cuota.getIdServicio())
                .nombreServicio(nombreServicio)
                .detallePuesto(detallePuesto)
                .detalleServicio(detalleServicio)
                .periodo(cuota.getMes() + "/" + cuota.getAnio())
                .mensaje("¡Gracias por su pago puntual!")
                .build();
    }

    @Override
    public FlujoCajaDiarioDTO obtenerFlujoCajaDiario(LocalDate fecha) {
        LocalDate fechaReporte = fecha != null ? fecha : LocalDate.now();
        LocalDateTime desde = fechaReporte.atStartOfDay();
        LocalDateTime hasta = fechaReporte.plusDays(1).atStartOfDay();

        Map<Integer, PuestoDTO> puestos = new HashMap<>();
        Map<Integer, SocioDTO> socios = new HashMap<>();
        Map<Integer, ServicioDTO> servicios = new HashMap<>();

        List<DetalleFlujoCajaDTO> pagos = cuotaRepo
                .findByEstadoAndFechaPagoBetween(EstadoPago.PAGADO, desde, hasta)
                .stream()
                .sorted(Comparator.comparing(CuotaPago::getFechaPago))
                .map(cuota -> {
                    PuestoDTO puesto = obtenerPuestoSeguro(cuota.getIdPuesto(), puestos);
                    ServicioDTO servicio = obtenerServicioSeguro(cuota.getIdServicio(), servicios);
                    SocioDTO socio = puesto != null && puesto.getIdSocioActual() != null
                            ? obtenerSocioSeguro(puesto.getIdSocioActual(), socios)
                            : null;

                    return DetalleFlujoCajaDTO.builder()
                            .idCuota(cuota.getIdCuota())
                            .idPuesto(cuota.getIdPuesto())
                            .numeroPuesto(puesto != null ? puesto.getNumeroPuesto() : null)
                            .idSocio(puesto != null ? puesto.getIdSocioActual() : null)
                            .nombreSocio(nombreSocio(socio))
                            .idServicio(cuota.getIdServicio())
                            .nombreServicio(servicio != null ? servicio.getNombreServicio() : null)
                            .monto(cuota.getMonto())
                            .metodoPago(cuota.getMetodoPago())
                            .numeroOperacion(cuota.getNumeroOperacion())
                            .numeroComprobante(cuota.getNumeroComprobante())
                            .fechaPago(cuota.getFechaPago())
                            .build();
                })
                .toList();

        double total = pagos.stream()
                .mapToDouble(pago -> pago.getMonto() != null ? pago.getMonto() : 0.0)
                .sum();

        return FlujoCajaDiarioDTO.builder()
                .fecha(fechaReporte)
                .totalPagos(pagos.size())
                .totalRecaudado(total)
                .pagos(pagos)
                .build();
    }

    @Override
    public EstadoDeudoresDTO obtenerEstadoDeudores() {
        Map<Integer, PuestoDTO> puestos = new HashMap<>();
        Map<Integer, SocioDTO> socios = new HashMap<>();
        Map<Integer, ServicioDTO> servicios = new HashMap<>();

        Map<Integer, List<CuotaPago>> cuotasPorPuesto = cuotaRepo.findByEstado(EstadoPago.PENDIENTE)
                .stream()
                .collect(Collectors.groupingBy(CuotaPago::getIdPuesto));

        List<DeudorDTO> deudores = cuotasPorPuesto.entrySet()
                .stream()
                .map(entry -> construirDeudor(entry.getKey(), entry.getValue(), puestos, socios, servicios))
                .sorted(Comparator.comparing(DeudorDTO::getNumeroPuesto, Comparator.nullsLast(String::compareTo)))
                .toList();

        int totalCuotas = deudores.stream()
                .mapToInt(DeudorDTO::getTotalCuotasPendientes)
                .sum();
        double totalDeuda = deudores.stream()
                .mapToDouble(deudor -> deudor.getTotalDeuda() != null ? deudor.getTotalDeuda() : 0.0)
                .sum();

        return EstadoDeudoresDTO.builder()
                .totalPuestosConDeuda(deudores.size())
                .totalCuotasPendientes(totalCuotas)
                .totalDeuda(totalDeuda)
                .deudores(deudores)
                .build();
    }

    private void validarCuotaAnulable(CuotaPago cuota) {
        if (cuota.getEstado() == EstadoPago.PAGADO) {
            throw new RuntimeException("No se puede anular una cuota pagada. Primero revierta el pago.");
        }
        if (cuota.getEstado() == EstadoPago.ANULADO) {
            throw new RuntimeException("La cuota ya se encuentra anulada");
        }
    }

    private String generarNumeroComprobante(CuotaPago cuota) {
        return "CP-" + cuota.getAnio() + String.format("%02d", cuota.getMes())
                + "-" + String.format("%06d", cuota.getIdCuota());
    }

    private DeudorDTO construirDeudor(Integer idPuesto, List<CuotaPago> cuotas,
                                      Map<Integer, PuestoDTO> puestos,
                                      Map<Integer, SocioDTO> socios,
                                      Map<Integer, ServicioDTO> servicios) {
        PuestoDTO puesto = obtenerPuestoSeguro(idPuesto, puestos);
        SocioDTO socio = puesto != null && puesto.getIdSocioActual() != null
                ? obtenerSocioSeguro(puesto.getIdSocioActual(), socios)
                : null;

        List<DetalleDeudaDTO> detalle = cuotas.stream()
                .sorted(Comparator.comparing(CuotaPago::getAnio).thenComparing(CuotaPago::getMes))
                .map(cuota -> {
                    ServicioDTO servicio = obtenerServicioSeguro(cuota.getIdServicio(), servicios);
                    return DetalleDeudaDTO.builder()
                            .idCuota(cuota.getIdCuota())
                            .idServicio(cuota.getIdServicio())
                            .nombreServicio(servicio != null ? servicio.getNombreServicio() : null)
                            .mes(cuota.getMes())
                            .anio(cuota.getAnio())
                            .monto(cuota.getMonto())
                            .build();
                })
                .toList();

        double total = cuotas.stream()
                .mapToDouble(cuota -> cuota.getMonto() != null ? cuota.getMonto() : 0.0)
                .sum();

        return DeudorDTO.builder()
                .idPuesto(idPuesto)
                .numeroPuesto(puesto != null ? puesto.getNumeroPuesto() : null)
                .pabellon(puesto != null ? puesto.getPabellon() : null)
                .idSocio(puesto != null ? puesto.getIdSocioActual() : null)
                .nombreSocio(nombreSocio(socio))
                .totalCuotasPendientes(cuotas.size())
                .totalDeuda(total)
                .cuotas(detalle)
                .build();
    }

    private PuestoDTO obtenerPuestoSeguro(Integer idPuesto, Map<Integer, PuestoDTO> cache) {
        if (idPuesto == null) {
            return null;
        }
        if (cache.containsKey(idPuesto)) {
            return cache.get(idPuesto);
        }
        try {
            PuestoDTO puesto = puestoClient.getPuestoById(idPuesto);
            cache.put(idPuesto, puesto);
            return puesto;
        } catch (Exception e) {
            cache.put(idPuesto, null);
            return null;
        }
    }

    private ServicioDTO obtenerServicioSeguro(Integer idServicio, Map<Integer, ServicioDTO> cache) {
        if (idServicio == null) {
            return null;
        }
        if (cache.containsKey(idServicio)) {
            return cache.get(idServicio);
        }
        try {
            ServicioDTO servicio = servicioClient.getServicioById(idServicio);
            cache.put(idServicio, servicio);
            return servicio;
        } catch (Exception e) {
            cache.put(idServicio, null);
            return null;
        }
    }

    private SocioDTO obtenerSocioSeguro(Integer idSocio, Map<Integer, SocioDTO> cache) {
        if (idSocio == null) {
            return null;
        }
        if (cache.containsKey(idSocio)) {
            return cache.get(idSocio);
        }
        try {
            SocioDTO socio = socioClient.getSocioById(idSocio);
            cache.put(idSocio, socio);
            return socio;
        } catch (Exception e) {
            cache.put(idSocio, null);
            return null;
        }
    }

    private String nombreSocio(SocioDTO socio) {
        if (socio == null) {
            return null;
        }
        String nombre = socio.getNombre() != null ? socio.getNombre() : "";
        String apellido = socio.getApellido() != null ? socio.getApellido() : "";
        String nombreCompleto = (nombre + " " + apellido).trim();
        return nombreCompleto.isBlank() ? null : nombreCompleto;
    }
}
