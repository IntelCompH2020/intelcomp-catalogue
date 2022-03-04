package eu.intelcomp.catalogue.domain;

import java.util.Date;
import java.util.List;

public class JobInfo {

    private String id;
    private String name;
    private String label;
    private Date createdAt;
    private Date launchedAt;
    private Date finishedAt;
    private String user;
    private String infraId;
    private String callerAttributes;
    private String mergedStatus;
    private List<Status> status;

    public JobInfo() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLaunchedAt() {
        return launchedAt;
    }

    public void setLaunchedAt(Date launchedAt) {
        this.launchedAt = launchedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getInfraId() {
        return infraId;
    }

    public void setInfraId(String infraId) {
        this.infraId = infraId;
    }

    public String getCallerAttributes() {
        return callerAttributes;
    }

    public void setCallerAttributes(String callerAttributes) {
        this.callerAttributes = callerAttributes;
    }

    public String getMergedStatus() {
        return mergedStatus;
    }

    public void setMergedStatus(String mergedStatus) {
        this.mergedStatus = mergedStatus;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }
}
