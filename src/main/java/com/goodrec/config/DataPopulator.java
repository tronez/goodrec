package com.goodrec.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.goodrec.config.serialization.BinaryDeserializer;
import org.bson.types.Binary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
class DataPopulator {

    private final ObjectMapper mapper;

    public DataPopulator(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Bean
    @Profile("dev")
    Jackson2RepositoryPopulatorFactoryBean getRepositoryPopulator() {

        var userSourceData = new ClassPathResource("user_data.json");
        var recipeSourceData = new ClassPathResource("recipe_data.json");
        var factory = new Jackson2RepositoryPopulatorFactoryBean();
        Resource[] resourceFilesArr = {userSourceData, recipeSourceData};

        mapper.registerModule(binaryDeserializer());
        factory.setResources(resourceFilesArr);
        factory.setMapper(mapper);

        return factory;
    }

    @Bean
    @Profile("dev")
    Module binaryDeserializer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Binary.class, new BinaryDeserializer());
        return module;
    }
}
