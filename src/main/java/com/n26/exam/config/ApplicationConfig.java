package com.n26.exam.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan("com.n26.exam")
@EnableAsync
public class ApplicationConfig {

}
