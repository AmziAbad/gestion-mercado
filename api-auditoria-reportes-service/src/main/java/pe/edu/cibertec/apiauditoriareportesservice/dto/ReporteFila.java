package pe.edu.cibertec.apiauditoriareportesservice.dto;

public class ReporteFila {

    private final String columna1;
    private final String columna2;
    private final String columna3;

    public ReporteFila(String columna1, String columna2, String columna3) {
        this.columna1 = columna1;
        this.columna2 = columna2;
        this.columna3 = columna3;
    }

    public String getColumna1() {
        return columna1;
    }

    public String getColumna2() {
        return columna2;
    }

    public String getColumna3() {
        return columna3;
    }
}
