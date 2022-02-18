package eu.intelcomp.catalogue.service;

import eu.intelcomp.catalogue.domain.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExternalApiJobService implements JobService {

    private static final Logger logger = LogManager.getLogger(ExternalApiJobService.class);

    private static final String GET_JOB = "job";
    private static final String BROWSE_JOBS = "jobs";
    private static final String EXECUTE_JOB = "job/execute";
    private static final String DELETE_JOB = "job/delete";

    private final ExternalJobServiceProperties properties;
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ExternalApiJobService(ExternalJobServiceProperties properties) {
        this.properties = properties;
    }

    @Override
    public JobInfo add(Job job, Authentication authentication) {
        Arguments arguments = new Arguments();
        arguments.addServiceArguments(job.getProject(), User.of(authentication).getSub());
        arguments.addJobArgument("dateFrom", "2019-06-10");
        arguments.addJobArgument("dateTo", "2020-06-10");
        List<String> acronyms = new ArrayList<>();
        acronyms.add("acronym1");
        acronyms.add("acronym2");
//        arguments.addJobArgument("projectAcronym", acronyms);
        arguments.addJobArgument("projectAcronym", "acronym1");
        arguments.addJobArgument("queryMode", "0");
        HttpEntity<?> request = new HttpEntity<>(arguments, createHeaders());
        return restTemplate.postForObject(String.join("/", properties.getApiUrl(), EXECUTE_JOB), request, JobInfo.class);
    }

    @Override
    public JobInfo get(String id, String userId) {
        HttpEntity<?> request = new HttpEntity<>("", createHeaders());
        String url = String.join("/", properties.getApiUrl(), GET_JOB, id + "?user=" + userId);
        ResponseEntity<JobInfo> response = restTemplate.exchange(url, HttpMethod.GET, request, JobInfo.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Could not find Job with id : " + id + " for User: " + userId);
        }
        return response.getBody();
    }

    @Override
    public List<JobInfo> browse(JobFilters filters, Authentication authentication) {
        filters.setUser(User.of(authentication).getSub());
        HttpEntity<?> request = new HttpEntity<>(filters, createHeaders());
        return restTemplate.postForObject(String.join("/", properties.getApiUrl(), BROWSE_JOBS), request, List.class);
    }

    @Override
    public void delete(String id, String userId) {
        HttpEntity<?> request = new HttpEntity<>("", createHeaders());
        String url = String.join("/", properties.getApiUrl(), DELETE_JOB, id + "?user=" + userId);
        ResponseEntity<JobInfo> response = restTemplate.exchange(url, HttpMethod.DELETE, request, JobInfo.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Could not delete Job with id : " + id + " for User: " + userId);
        }
    }

    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.getAuthorization().getGrantType());
        body.add("client_id", properties.getAuthorization().getClientId());
        body.add("client_secret", properties.getAuthorization().getClientSecret());
        HttpEntity<?> request = new HttpEntity<>(body, headers);
        JSONObject response = restTemplate.postForObject(properties.getAuthorization().getUrl(), request, JSONObject.class);
        if (response == null) {
            return new HttpHeaders();
        }
        String token = response.get("access_token").toString();
        headers.remove("Content-Type");
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}