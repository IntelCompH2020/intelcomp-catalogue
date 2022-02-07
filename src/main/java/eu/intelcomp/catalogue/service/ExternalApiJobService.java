package eu.intelcomp.catalogue.service;

import com.sun.net.httpserver.HttpsParameters;
import eu.intelcomp.catalogue.domain.Arguments;
import eu.intelcomp.catalogue.domain.Job;
import eu.intelcomp.catalogue.domain.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private static final String DELETE_JOB = "job/";

    private final ExternalJobServiceProperties properties;
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ExternalApiJobService(ExternalJobServiceProperties properties) {
        this.properties = properties;
    }

    @Override
    public Job add(Job job, Authentication authentication) {
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
        restTemplate.postForObject(String.join("/", properties.getApiUrl(), EXECUTE_JOB), request, Object.class);
        return null;
    }

    @Override
    public Job get(String id, Authentication authentication) {
        HttpEntity<?> request = new HttpEntity<>("", createHeaders());
        restTemplate.postForObject(String.join("/", properties.getApiUrl(), GET_JOB, id), request, List.class);
        return null;
    }

    @Override
    public List<Job> browse(Arguments arguments, Authentication authentication) {
        String user = "\"user\": \"%s\"";
        HttpEntity<?> request = new HttpEntity<>(String.format(user, User.of(authentication).getSub()), createHeaders());
        return restTemplate.postForObject(String.join("/", properties.getApiUrl(), BROWSE_JOBS), request, List.class);
    }

    @Override
    public void delete(String id, Authentication authentication) {
        HttpEntity<?> request = new HttpEntity<>("", createHeaders());
        restTemplate.postForObject(String.join("/", properties.getApiUrl(), DELETE_JOB, id), request, List.class);
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
