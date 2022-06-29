package eu.intelcomp.catalogue.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.intelcomp.catalogue.domain.ModelAnswer;
import eu.openminted.registry.core.domain.Resource;
import eu.openminted.registry.core.service.SearchService;
import gr.athenarc.catalogue.service.GenericItemService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> getCoreId(@PathVariable("type") String type, @PathVariable("version") String version) {
        SearchService.KeyValue typeKeyValue = new SearchService.KeyValue("type", type);
        SearchService.KeyValue versionKeyValue = new SearchService.KeyValue("version", version);
        Resource resource = genericItemService.searchResource("dataset_instance", typeKeyValue, versionKeyValue);
        if (resource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(resource.getId(), HttpStatus.OK);
    }
}
