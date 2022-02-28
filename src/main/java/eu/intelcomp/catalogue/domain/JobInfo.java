package eu.intelcomp.catalogue.domain;

import java.util.Date;

public class JobInfo {

    private String id;
    private String name;
    private String label;
    private Date createdAt;
    private Date launchedAt;
    private Date finishedAt;
    private String status;
    private String user;
    private String callerAttributes;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCallerAttributes() {
        return callerAttributes;
    }

    public void setCallerAttributes(String callerAttributes) {
        this.callerAttributes = callerAttributes;
    }
}
