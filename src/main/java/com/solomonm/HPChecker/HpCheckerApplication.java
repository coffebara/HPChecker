package com.solomonm.HPChecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling // 스케줄러 활성화
public class HpCheckerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HpCheckerApplication.class, args);
    }
}
