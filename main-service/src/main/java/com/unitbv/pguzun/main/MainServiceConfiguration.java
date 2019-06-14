package com.unitbv.pguzun.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.unitbv.pguzun.main.web.MainController;

@Configuration
@Import(SecurityConfig.class)
public class MainServiceConfiguration {

    @Bean
    public MainController mainController() {
        return new MainController();
    }
}
