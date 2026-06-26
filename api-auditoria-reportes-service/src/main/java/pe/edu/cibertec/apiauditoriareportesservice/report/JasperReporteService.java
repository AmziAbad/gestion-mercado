package pe.edu.cibertec.apiauditoriareportesservice.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apiauditoriareportesservice.dto.ReporteFila;
import pe.edu.cibertec.apiauditoriareportesservice.exception.ReglaNegocioException;

import java.util.HashMap;
import java.util.List;

@Service
public class JasperReporteService {

    public byte[] generarPdf(String titulo, List<ReporteFila> filas) {
        try {
            JasperDesign design = crearDiseno(titulo);
            JasperReport report = JasperCompileManager.compileReport(design);
            JasperPrint print = JasperFillManager.fillReport(
                    report,
                    new HashMap<>(),
                    new JRBeanCollectionDataSource(filas)
            );
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException ex) {
            throw new ReglaNegocioException("No se pudo generar el PDF del reporte.");
        }
    }

    private JasperDesign crearDiseno(String titulo) throws JRException {
        JasperDesign design = new JasperDesign();
        design.setName("reporte");
        design.setPageWidth(595);
        design.setPageHeight(842);
        design.setColumnWidth(515);
        design.setLeftMargin(40);
        design.setRightMargin(40);
        design.setTopMargin(30);
        design.setBottomMargin(30);

        addField(design, "columna1");
        addField(design, "columna2");
        addField(design, "columna3");

        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(45);
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0);
        titleText.setY(0);
        titleText.setWidth(515);
        titleText.setHeight(30);
        titleText.setFontSize(16f);
        titleText.setBold(true);
        titleText.setText(titulo);
        titleBand.addElement(titleText);
        design.setTitle(titleBand);

        JRDesignBand header = new JRDesignBand();
        header.setHeight(24);
        header.addElement(staticText("Referencia", 0, 0, 170, true));
        header.addElement(staticText("Detalle", 175, 0, 220, true));
        header.addElement(staticText("Monto/Estado", 400, 0, 115, true));
        design.setColumnHeader(header);

        JRDesignBand detail = new JRDesignBand();
        detail.setHeight(22);
        detail.addElement(textField("$F{columna1}", 0, 0, 170));
        detail.addElement(textField("$F{columna2}", 175, 0, 220));
        detail.addElement(textField("$F{columna3}", 400, 0, 115));
        ((JRDesignSection) design.getDetailSection()).addBand(detail);

        return design;
    }

    private void addField(JasperDesign design, String name) throws JRException {
        JRDesignField field = new JRDesignField();
        field.setName(name);
        field.setValueClass(String.class);
        design.addField(field);
    }

    private JRDesignStaticText staticText(String text, int x, int y, int width, boolean bold) {
        JRDesignStaticText staticText = new JRDesignStaticText();
        staticText.setX(x);
        staticText.setY(y);
        staticText.setWidth(width);
        staticText.setHeight(20);
        staticText.setBold(bold);
        staticText.setText(text);
        return staticText;
    }

    private JRDesignTextField textField(String expression, int x, int y, int width) {
        JRDesignTextField textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(y);
        textField.setWidth(width);
        textField.setHeight(20);
        textField.setExpression(new JRDesignExpression(expression));
        return textField;
    }
}
