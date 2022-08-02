package kr.co.goplan.mtgame.config;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Configuration
@Data
public class ConfigProperties {

    @Bean(name = "config")
    public PropertiesFactoryBean propertiesFactoryBean() throws Exception{
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("config.properties");
        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }

}
