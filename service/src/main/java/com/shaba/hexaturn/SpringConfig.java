package com.shaba.hexaturn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan ( basePackageClasses = { SpringConfig.class } )
@PropertySource ( { "classpath:application.properties" } )
@EnableHypermediaSupport ( type = EnableHypermediaSupport.HypermediaType.HAL )
@EnableAutoConfiguration(exclude = JooqAutoConfiguration.class)
@EnableWebMvc
@EnableEntityLinks
public class SpringConfig
{
    public static void main( final String [] args )
    {
        SpringApplication.run( SpringConfig.class, args );
    }

    @Bean
    public String appConfigSubdir()
    {
        return "hexaturn-solver";
    }
}
