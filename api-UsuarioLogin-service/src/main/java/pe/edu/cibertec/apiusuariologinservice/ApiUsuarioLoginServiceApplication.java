package pe.edu.cibertec.apiusuariologinservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiUsuarioLoginServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUsuarioLoginServiceApplication.class, args);
    }

}
