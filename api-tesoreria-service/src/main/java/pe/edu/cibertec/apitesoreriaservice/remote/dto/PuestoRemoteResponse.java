package pe.edu.cibertec.apitesoreriaservice.remote.dto;

public record PuestoRemoteResponse(
        Integer idPuesto,
        String codigoPuesto,
        String pabellon,
        String medidas,
        String giro,
        String estadoPuesto
) {
}
