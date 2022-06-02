package eu.intelcomp.catalogue.config;

import eu.openminted.registry.core.controllers.ResourceSyncController;
import gr.athenarc.catalogue.CatalogueApplication;
import gr.athenarc.catalogue.config.CatalogueLibConfiguration;
import gr.athenarc.catalogue.config.LibConfiguration;
import gr.athenarc.catalogue.config.RegistryCoreConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value = {
        "gr.athenarc",
        "eu.openminted.registry.core",
},
excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CatalogueApplication.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = LibConfiguration.class), // TODO: remove if lib is fixed
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RegistryCoreConfiguration.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ResourceSyncController.class)
})
public class IntelcompConfiguration implements CatalogueLibConfiguration {

    @Override
    public String generatedClassesPackageName() {
        return "eu.intelcomp.xsd2java";
    }
}
