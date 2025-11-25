package com.example.abc_onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.abc_onboarding")
public class AbcSignupApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbcSignupApplication.class, args);
    }
}
