package com.goodrec.config.datapopulator;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.goodrec.config.serializer.BinaryDeserializer;
import org.bson.types.Binary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@Profile("dev")
class DevDataPopulator {

    private final ObjectMapper mapper;

    public DevDataPopulator(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Bean
    Jackson2RepositoryPopulatorFactoryBean getRepositoryPopulator() {
        var resourcesArr = getResources();
        var factory = new Jackson2RepositoryPopulatorFactoryBean();

        mapper.registerModule(binaryDeserializer());
        factory.setResources(resourcesArr);
        factory.setMapper(mapper);

        return factory;
    }

    @Bean
    Module binaryDeserializer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Binary.class, new BinaryDeserializer());
        return module;
    }

    private Resource[] getResources() {
        var userSourceData = new ClassPathResource("user_data.json");
        var categorySourceData = new ClassPathResource("category_data.json");
        var recipeSourceData = new ClassPathResource("recipe_data.json");
        return new Resource[]{userSourceData, categorySourceData, recipeSourceData};
    }

}
