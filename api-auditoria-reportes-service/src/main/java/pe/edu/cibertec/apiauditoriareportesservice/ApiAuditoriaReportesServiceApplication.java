package pe.edu.cibertec.apiauditoriareportesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiAuditoriaReportesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAuditoriaReportesServiceApplication.class, args);
    }

}
