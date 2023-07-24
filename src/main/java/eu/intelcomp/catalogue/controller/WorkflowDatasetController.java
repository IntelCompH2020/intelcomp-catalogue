package eu.intelcomp.catalogue.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.intelcomp.catalogue.domain.Dataset;
import eu.intelcomp.catalogue.domain.User;
import eu.intelcomp.catalogue.service.DatasetService;
import eu.intelcomp.catalogue.service.FileService;
import eu.intelcomp.catalogue.service.JobProperties;
import eu.openminted.registry.core.domain.FacetFilter;
import eu.openminted.registry.core.domain.Paging;
import gr.athenarc.catalogue.annotations.Browse;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import static gr.athenarc.catalogue.utils.PagingUtils.createFacetFilter;

@RestController
@RequestMapping("workflow-datasets")
public class WorkflowDatasetController {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowDatasetController.class);

    private final DatasetService datasetService;
    private final FileService fileService;
    private final JobProperties jobProperties;
    private final ObjectMapper mapper = new ObjectMapper();

    public WorkflowDatasetController(DatasetService datasetService,
                                     FileService fileService,
                                     JobProperties jobProperties) {
        this.datasetService = datasetService;
        this.fileService = fileService;
        this.jobProperties = jobProperties;
    }

    @GetMapping("{datasetId}")
    @PreAuthorize("isAuthenticated() and not anonymous")
    public ResponseEntity<Dataset> get(@PathVariable("datasetId") String id) {
        return ResponseEntity.ok(datasetService.get(id));
    }

    @GetMapping("{datasetId}/file")
    @PreAuthorize("isAuthenticated() and not anonymous")
    public ResponseEntity<Resource> getFile(@PathVariable("datasetId") String id) {
        Dataset dataset = datasetService.get(id);
        File file = new File(dataset.getFileLocation());
        Resource resource = fileService.get(dataset.getFileLocation());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", file.getName()));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(resource);
    }

    @Browse
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Paging<Dataset>> browse(@Parameter(hidden = true) @RequestParam Map<String, Object> allRequestParams) {
        FacetFilter ff = createFacetFilter(allRequestParams);
        ff.addFilter("owner", User.getId(SecurityContextHolder.getContext().getAuthentication()));
        return ResponseEntity.ok(datasetService.browse(ff));
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated() and not anonymous")
    public ResponseEntity<Dataset> add(@RequestParam(name = "dataset") String dataset, @RequestPart MultipartFile file, @Parameter(hidden = true) Authentication authentication) throws JsonProcessingException {
        // save
        Dataset datasetObj = mapper.readValue(dataset, Dataset.class);
        String id = UUID.randomUUID().toString();
        logger.info("created dataset id: {}", id);
        datasetObj.setFileLocation(createFileLocation(id, file));
        Dataset saved = datasetService.add(id, datasetObj);
        fileService.save(file, saved.getFileLocation());
        return ResponseEntity.ok(saved);
    }

    @PutMapping("{datasetId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Dataset> update(@PathVariable("datasetId") String id, @RequestParam(name = "dataset") String dataset, @RequestPart MultipartFile file) throws JsonProcessingException {
        Dataset datasetObj = mapper.readValue(dataset, Dataset.class);
        if (file != null) {
            fileService.delete(datasetObj.getFileLocation());
            datasetObj.setFileLocation(createFileLocation(id, file));
            fileService.save(file, datasetObj.getFileLocation());
        }
        return ResponseEntity.ok(datasetService.update(id, datasetObj));
    }

    @DeleteMapping("{datasetId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable("datasetId") String id) {
        Dataset dataset = datasetService.get(id);
        datasetService.delete(id);
        fileService.delete(dataset.getFileLocation());

        return ResponseEntity.ok().build();
    }

    private String createFileLocation(String datasetId, MultipartFile file) {
        String location = null;
        if (file != null && datasetId != null) {
            location = String.format("%s/%s/%s/%s",
                    jobProperties.getData().getDirectories().getBase(),
                    jobProperties.getData().getDirectories().getInputRelativePath(),
                    datasetId,
                    file.getOriginalFilename()
            );
        }
        return location;
    }

}
