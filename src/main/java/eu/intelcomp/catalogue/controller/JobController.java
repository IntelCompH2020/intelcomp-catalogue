package eu.intelcomp.catalogue.controller;

import eu.intelcomp.catalogue.domain.Job;
import eu.intelcomp.catalogue.domain.JobFilters;
import eu.intelcomp.catalogue.domain.JobInfo;
import eu.intelcomp.catalogue.domain.User;
import eu.intelcomp.catalogue.service.JobService;
import gr.athenarc.catalogue.exception.ResourceException;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("execute")
    @PreAuthorize("(hasAuthority('OPERATOR-WORKFLOW_PROCESSOR') && @jobController.jobIsWorkflow(#job)) || (hasAuthority('OPERATOR_DATA-PROCESSOR') && !@jobController.jobIsWorkflow(#job))")
    public ResponseEntity<JobInfo> add(@RequestBody Job job, @Parameter(hidden = true) Authentication authentication) {
        return new ResponseEntity<>(jobService.add(job, authentication), HttpStatus.OK);
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

    public static boolean jobIsWorkflow(Job job) {
        if (job == null || job.getServiceArguments() == null || job.getJobArguments() == null) {
            throw new ResourceException("Incomplete Job information", HttpStatus.BAD_REQUEST);
        }
        if (job.getServiceArguments().getProcessId() == null || "".equals(job.getServiceArguments().getProcessId())) {
            throw new ResourceException("'processId' cannot be empty", HttpStatus.BAD_REQUEST);
        }

        return job.getServiceArguments().getProcessId().endsWith("-workflow");
    }
}
