package pe.edu.cibertec.apipatrimonioservice.mapper;

import pe.edu.cibertec.apipatrimonioservice.dto.ContratoResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.PuestoResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.SocioResponse;
import pe.edu.cibertec.apipatrimonioservice.dto.TransferenciaResponse;
import pe.edu.cibertec.apipatrimonioservice.entity.ContratoOcupacion;
import pe.edu.cibertec.apipatrimonioservice.entity.Puesto;
import pe.edu.cibertec.apipatrimonioservice.entity.Socio;
import pe.edu.cibertec.apipatrimonioservice.entity.TransferenciaTitularidad;

public final class PatrimonioMapper {

    private PatrimonioMapper() {
    }

    public static SocioResponse toSocioResponse(Socio socio) {
        return new SocioResponse(
                socio.getIdSocio(),
                socio.getDni(),
                socio.getRuc(),
                socio.getNombres(),
                socio.getApellidos(),
                socio.getTelefono(),
                socio.getCorreo(),
                socio.getDireccion(),
                socio.getEstado(),
                socio.getEsAsociacion(),
                socio.getFechaRegistro()
        );
    }

    public static PuestoResponse toPuestoResponse(Puesto puesto) {
        return new PuestoResponse(
                puesto.getIdPuesto(),
                puesto.getCodigoPuesto(),
                puesto.getPabellon(),
                puesto.getMedidas(),
                puesto.getGiro(),
                puesto.getEstadoPuesto(),
                puesto.getFechaRegistro()
        );
    }

    public static ContratoResponse toContratoResponse(ContratoOcupacion contrato, Puesto puesto, Socio socio) {
        return new ContratoResponse(
                contrato.getIdContrato(),
                contrato.getIdPuesto(),
                puesto != null ? puesto.getCodigoPuesto() : null,
                contrato.getIdSocio(),
                socio != null ? socio.getNombres() + " " + socio.getApellidos() : null,
                contrato.getFechaInicio(),
                contrato.getFechaFin(),
                contrato.getEstadoContrato(),
                contrato.getMotivoCierre(),
                contrato.getIdUsuarioRegistro(),
                contrato.getFechaRegistro()
        );
    }

    public static TransferenciaResponse toTransferenciaResponse(TransferenciaTitularidad transferencia) {
        return new TransferenciaResponse(
                transferencia.getIdTransferencia(),
                transferencia.getIdPuesto(),
                transferencia.getIdContratoSaliente(),
                transferencia.getIdSocioSaliente(),
                transferencia.getIdSocioEntrante(),
                transferencia.getIdContratoEntrante(),
                transferencia.getIdUsuarioTramite(),
                transferencia.getCostoTransferencia(),
                transferencia.getDeudaValidada(),
                transferencia.getAsumeDeuda(),
                transferencia.getMontoDeudaAsumida(),
                transferencia.getEstadoTransferencia(),
                transferencia.getObservacion(),
                transferencia.getFechaTramite()
        );
    }
}
