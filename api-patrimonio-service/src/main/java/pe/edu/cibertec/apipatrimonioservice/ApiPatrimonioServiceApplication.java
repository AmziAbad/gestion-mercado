package pe.edu.cibertec.apipatrimonioservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiPatrimonioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPatrimonioServiceApplication.class, args);
    }

}
