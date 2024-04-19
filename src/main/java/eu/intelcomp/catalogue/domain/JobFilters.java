/*
 * Copyright 2021-2024 OpenAIRE AMKE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.intelcomp.catalogue.domain;

import java.util.Date;
import java.util.List;

public class JobFilters {

    private String user;
    private List<String> ids;
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
