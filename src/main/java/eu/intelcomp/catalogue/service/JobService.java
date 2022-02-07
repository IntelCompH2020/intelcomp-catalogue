package eu.intelcomp.catalogue.service;

import eu.intelcomp.catalogue.domain.Arguments;
import eu.intelcomp.catalogue.domain.Job;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JobService {

    Job add(Job job, Authentication authentication);
    Job get(String id, Authentication authentication);
    List<Job> browse(Arguments arguments, Authentication authentication);
    void delete(String id, Authentication authentication);

}
