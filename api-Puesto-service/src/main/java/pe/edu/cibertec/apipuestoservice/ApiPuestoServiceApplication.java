package pe.edu.cibertec.apipuestoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiPuestoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPuestoServiceApplication.class, args);
    }

}
