package eu.intelcomp.catalogue.controller;

import eu.intelcomp.catalogue.domain.Job;
import eu.intelcomp.catalogue.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Job> add(@RequestBody Job job, @ApiIgnore Authentication authentication) {
        return new ResponseEntity<>(jobService.add(job, authentication), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<?>> browse(@ApiIgnore Authentication authentication) {
        return new ResponseEntity<>(jobService.browse(null, authentication), HttpStatus.OK);
    }
}
