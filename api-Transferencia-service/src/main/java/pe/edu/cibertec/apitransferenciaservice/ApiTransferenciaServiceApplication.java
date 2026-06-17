package pe.edu.cibertec.apitransferenciaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiTransferenciaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiTransferenciaServiceApplication.class, args);
    }

}
