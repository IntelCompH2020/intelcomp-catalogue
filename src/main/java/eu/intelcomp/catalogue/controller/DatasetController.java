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

package eu.intelcomp.catalogue.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.intelcomp.catalogue.domain.ModelAnswer;
import eu.openminted.registry.core.service.SearchService;
import gr.athenarc.catalogue.service.GenericItemService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("datasets")
public class DatasetController {

    private static final Logger logger = LoggerFactory.getLogger(DatasetController.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GenericItemService genericItemService;

    @Autowired
    public DatasetController(GenericItemService genericItemService) {
        this.genericItemService = genericItemService;
    }

    @GetMapping("{id}")
    public ModelAnswer get(@PathVariable("id") String id) {
        ModelAnswer dataset = null;
        try {
            String json = objectMapper.writeValueAsString(genericItemService.get("dataset_type", id));
            Object answer = JSONValue.parse(json);
            dataset = new ModelAnswer((JSONObject) answer);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return dataset;
    }

    // TODO: helper method, remove when job filter is implemented correctly from @CITE
    @GetMapping("instances/{type}/{version}/internalid")
    public String getCoreId(@PathVariable("type") String type, @PathVariable("version") String version) {
        SearchService.KeyValue typeKeyValue = new SearchService.KeyValue("type", type);
        SearchService.KeyValue versionKeyValue = new SearchService.KeyValue("version", version);
        return genericItemService.searchResource("dataset_instance", typeKeyValue, versionKeyValue).getId();
    }
}
