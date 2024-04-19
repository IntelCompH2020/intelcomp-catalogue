/*
 * Copyright 2021-2024 OpenAIRE AMKE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
