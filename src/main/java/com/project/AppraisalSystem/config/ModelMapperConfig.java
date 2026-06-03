package com.project.AppraisalSystem.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper Mapper = new ModelMapper();
        Mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return Mapper;
    }
}
