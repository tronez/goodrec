package com.goodrec.config.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoggingConfiguration {

    @Bean
    LoggingAspect getLoggingAspect() {
        return new LoggingAspect();
    }
}
