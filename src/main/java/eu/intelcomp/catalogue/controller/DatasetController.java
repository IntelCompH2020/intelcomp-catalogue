package eu.intelcomp.catalogue.controller;

import eu.intelcomp.xsd2java.DatasetType;
import eu.openminted.registry.core.domain.Resource;
import eu.openminted.registry.core.service.SearchService;
import gr.athenarc.catalogue.service.GenericItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final GenericItemService genericItemService;

    public DatasetController(GenericItemService genericItemService) {
        this.genericItemService = genericItemService;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id") String id) {
        DatasetType dataset = genericItemService.get("dataset_type", id);
        return ResponseEntity.ok(dataset);
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
