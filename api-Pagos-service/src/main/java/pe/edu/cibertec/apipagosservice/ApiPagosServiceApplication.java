package pe.edu.cibertec.apipagosservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiPagosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPagosServiceApplication.class, args);
    }

}
