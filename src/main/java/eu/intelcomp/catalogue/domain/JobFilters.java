package eu.intelcomp.catalogue.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JobFilters {

    private String user;
    private List<String> ids;
    private List<String> processes;
    private List<String> statuses;
//    private List<Enum> statuses; #["QUEUED", "FINISHED", "RUNNING"]
    private Date createdAfter;
    private Date createdBefore;
    private Date launchedAfter;
    private Date launchedBefore;
    private Date completedBefore;

    public JobFilters() {
    }

    public JobFilters(String user) {
        this.user = user;
    }

    public JobFilters(String user, List<String> statuses) {
        this.user = user;
        this.statuses = statuses;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getProcesses() {
        return processes;
    }

    public void setProcesses(List<String> processes) {
        this.processes = processes;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
    }

    public Date getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
    }

    public Date getLaunchedAfter() {
        return launchedAfter;
    }

    public void setLaunchedAfter(Date launchedAfter) {
        this.launchedAfter = launchedAfter;
    }

    public Date getLaunchedBefore() {
        return launchedBefore;
    }

    public void setLaunchedBefore(Date launchedBefore) {
        this.launchedBefore = launchedBefore;
    }

    public Date getCompletedBefore() {
        return completedBefore;
    }

    public void setCompletedBefore(Date completedBefore) {
        this.completedBefore = completedBefore;
    }
}
