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

import java.util.ArrayList;
import java.util.List;

public class Job {

    private ServiceArguments serviceArguments;
    private String callerAttributes;
    private List<JobArgument> jobArguments = new ArrayList<>();

    public Job() {
    }

    public ServiceArguments getServiceArguments() {
        return serviceArguments;
    }

    public void addServiceArguments(String processId, String userId) {
        this.serviceArguments = new ServiceArguments(processId, userId);
    }

    public void setServiceArguments(ServiceArguments serviceArguments) {
        this.serviceArguments = serviceArguments;
    }

    public String getCallerAttributes() {
        return callerAttributes;
    }

    public void setCallerAttributes(String callerAttributes) {
        this.callerAttributes = callerAttributes;
    }

    public void addJobArgument(String name, String value) {
        this.jobArguments.add(new JobArgument(name, value));
    }

//    public void addJobArgument(String name, String value) {
//        this.jobArguments.add(new JobArgument(name, Collections.singletonList(value)));
//    }
//
//    public void addJobArgument(String name, List<String> values) {
//        this.jobArguments.add(new JobArgument(name, values));
//    }

    public List<JobArgument> getJobArguments() {
        return jobArguments;
    }

    public void setJobArguments(List<JobArgument> jobArguments) {
        this.jobArguments = jobArguments;
    }

    public class ServiceArguments {
        private String processId;
        private String user;
        private String infraId = "k8s";

        public ServiceArguments() {
        }

        public ServiceArguments(String processId, String user) {
            this.processId = processId;
            this.user = user;
        }

        public ServiceArguments(String processId, String user, String infraId) {
            this.processId = processId;
            this.user = user;
            this.infraId = infraId;
        }

        public String getProcessId() {
            return processId;
        }

        public void setProcessId(String processId) {
            this.processId = processId;
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
    }

    public static class JobArgument {
        private String name;
        private String value;
//        private List<String> value = new ArrayList<>();

        public JobArgument() {
        }

        public JobArgument(String name, String value) {
            this.name = name;
            this.value = value;
        }

//        public JobArgument(String name, List<String> value) {
//            this.name = name;
//            this.value = value;
//        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

//        public List<String> getValue() {
//            return value;
//        }
//
//        public void setValue(List<String> value) {
//            this.value = value;
//        }
    }
}
