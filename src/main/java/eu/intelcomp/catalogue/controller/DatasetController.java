package eu.intelcomp.catalogue.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.intelcomp.catalogue.domain.ModelAnswer;
import gr.athenarc.catalogue.service.GenericItemService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("datasets")
public class DatasetController {

    private static final Logger logger = LogManager.getLogger(DatasetController.class);

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
            logger.error(e);
        }

        return dataset;
    }

    // TODO: helper method, remove when job filter is implemented correctly from @CITE
    @GetMapping("instances/{id}/internalid")
    public String getCoreId(@PathVariable("id") String id) {
        return genericItemService.searchResource("dataset_instance", id, true).getId();
    }
}
