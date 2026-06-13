package pe.edu.cibertec.apiservicioservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiServicioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServicioServiceApplication.class, args);
    }

}
