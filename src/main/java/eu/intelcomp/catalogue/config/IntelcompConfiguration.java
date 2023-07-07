package eu.intelcomp.catalogue.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableConfigurationProperties(IntelcompProperties.class)
@ComponentScan(value = {"eu.openminted.registry.core"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = eu.openminted.registry.core.controllers.GenericController.class)})
public class IntelcompConfiguration {
}
