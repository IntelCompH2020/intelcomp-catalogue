package eu.intelcomp.catalogue.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.intelcomp.catalogue.domain.Job;
import eu.intelcomp.catalogue.domain.JobFilters;
import eu.intelcomp.catalogue.domain.JobInfo;
import eu.intelcomp.catalogue.domain.User;
import eu.intelcomp.catalogue.service.JobService;
import gr.athenarc.catalogue.exception.ResourceException;
import gr.athenarc.catalogue.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("jobs")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("execute")
    @PreAuthorize("(hasAuthority('OPERATOR-WORKFLOW_PROCESSOR') && @jobController.jobIsWorkflow(#job)) || (hasAuthority('OPERATOR_DATA-PROCESSOR') && !@jobController.jobIsWorkflow(#job))")
    public ResponseEntity<JobInfo> add(@RequestBody Job job, @Parameter(hidden = true) Authentication authentication) {
        return new ResponseEntity<>(jobService.add(job, authentication), HttpStatus.OK);
    }

    @PostMapping(value = "execute/custom", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('OPERATOR-WORKFLOW_PROCESSOR') && @jobController.jobIsWorkflow(#jobString))")
    public ResponseEntity<JobInfo> addJobWithFile(@RequestParam(name = "job") String jobString, @RequestPart MultipartFile file, @Parameter(hidden = true) Authentication authentication) throws IOException {
        // save
        Job job = mapper.readValue(jobString, Job.class);
        String location = String.format("/workdir/ui/%s/", UUID.randomUUID());
        Files.createDirectories(Paths.get(location));
        String filepath = location + file.getOriginalFilename();
        File f = new File(filepath);
        Job.JobArgument fileArgument = new Job.JobArgument();
        file.transferTo(f);
        fileArgument.setName("file");
        fileArgument.setValue(List.of(filepath));
        job.getJobArguments().add(fileArgument);
        JobInfo info = jobService.add(job, authentication);
//        Files.createDirectories(Paths.get(String.format("/workdir/%s/%s", job.getServiceArguments().getProcessId(), info.getCreatedAt())));
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @RequestMapping(path = "/{jobId}/output/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable("jobId") String id,
                                             @RequestParam String process,
                                             @RequestParam String filename,
                                             @Parameter(hidden = true) Authentication authentication) {
        JobInfo info = get(id, null, authentication).getBody();
        ResponseEntity<Resource> responseEntity = ResponseEntity.notFound().build();
        if (info != null) {
            try {
                File file = new File(String.format("/workdir/%s/%s/output/%s", process, id, filename));
                Path path = Paths.get(file.getAbsolutePath());

                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s_output.cvs", id));
                responseEntity = ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(file.length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } catch (Exception e) {
                logger.warn("Could not find file : {}", e.getMessage());
            }
        }
        return responseEntity;
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobInfo> get(@PathVariable("jobId") String id,
                                       @RequestParam(value = "user", required = false) String userId,
                                       @Parameter(hidden = true) Authentication authentication) {
        if (userId == null || "".equals(userId)) {
            userId = User.of(authentication).getSub();
        }
        return new ResponseEntity<>(jobService.get(id, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<JobInfo>> browse(@RequestBody JobFilters jobFilters, @Parameter(hidden = true) Authentication authentication) {
        return new ResponseEntity<>(jobService.browse(jobFilters, authentication), HttpStatus.OK);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> delete(@PathVariable("jobId") String id,
                                       @RequestParam(value = "user", required = false) String userId,
                                       @Parameter(hidden = true) Authentication authentication) {
        if (userId == null || "".equals(userId)) {
            userId = User.of(authentication).getSub();
        }
        jobService.delete(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public static boolean jobIsWorkflow(Object obj) throws JsonProcessingException {
        Job job;
        if (obj instanceof String) {
            job = mapper.readValue((String) obj, Job.class);
        } else {
            job = (Job) obj;
        }
        if (job == null || job.getServiceArguments() == null || job.getJobArguments() == null) {
            throw new ResourceException("Incomplete Job information", HttpStatus.BAD_REQUEST);
        }
        if (job.getServiceArguments().getProcessId() == null || "".equals(job.getServiceArguments().getProcessId())) {
            throw new ResourceException("'processId' cannot be empty", HttpStatus.BAD_REQUEST);
        }

        return job.getServiceArguments().getProcessId().endsWith("-workflow");
    }
}
