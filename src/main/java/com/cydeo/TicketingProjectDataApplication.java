package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication // covers @configuration
public class TicketingProjectDataApplication {


    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataApplication.class, args);
    }

    //I am trying to add bean in the container through @Bean annotation

    //1. Create a class annotated with @configuration
    //2. Write a method which returns the object that you're trying to add in the container
    //3. annotate this method with @Bean
//1
    ModelMapper mapper = new ModelMapper();
//2
//3
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }



}
