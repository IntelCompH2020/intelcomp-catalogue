package eu.intelcomp.catalogue.service;

import eu.intelcomp.catalogue.domain.Job;
import eu.intelcomp.catalogue.domain.JobFilters;
import eu.intelcomp.catalogue.domain.JobInfo;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JobService {

    JobInfo add(Job job, Authentication authentication);
    JobInfo get(String id, String userId);
    List<JobInfo> browse(JobFilters filters, Authentication authentication);
    void delete(String id, String userId);

}
