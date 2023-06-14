package eu.intelcomp.catalogue.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IntelcompProperties.class)
@ComponentScan(value = {"eu.openminted.registry.core"})
public class IntelcompConfiguration {
}
