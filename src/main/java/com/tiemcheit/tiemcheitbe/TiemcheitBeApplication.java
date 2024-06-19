package com.tiemcheit.tiemcheitbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TiemcheitBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TiemcheitBeApplication.class, args);
    }
}
