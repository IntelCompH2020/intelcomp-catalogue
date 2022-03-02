package eu.intelcomp.catalogue.controller;

import eu.intelcomp.catalogue.domain.*;
import eu.intelcomp.catalogue.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("execute")
    public ResponseEntity<JobInfo> add(@RequestBody Job job, @ApiIgnore Authentication authentication) {
        return new ResponseEntity<>(jobService.add(job, authentication), HttpStatus.OK);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobInfo> get(@PathVariable("jobId") String id,
                                       @RequestParam(value = "user", required = false) String userId,
                                       @ApiIgnore Authentication authentication) {
        if (userId == null || "".equals(userId)) {
            userId = User.of(authentication).getSub();
        }
        return new ResponseEntity<>(jobService.get(id, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<JobInfo>> browse(@RequestBody JobFilters jobFilters, @ApiIgnore Authentication authentication) {
        return new ResponseEntity<>(jobService.browse(jobFilters, authentication), HttpStatus.OK);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> delete(@PathVariable("jobId") String id,
                                       @RequestParam(value = "user", required = false) String userId,
                                       @ApiIgnore Authentication authentication) {
        if (userId == null || "".equals(userId)) {
            userId = User.of(authentication).getSub();
        }
        jobService.delete(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
