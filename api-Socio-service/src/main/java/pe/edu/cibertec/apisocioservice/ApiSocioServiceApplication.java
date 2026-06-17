package pe.edu.cibertec.apisocioservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiSocioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSocioServiceApplication.class, args);
    }

}
