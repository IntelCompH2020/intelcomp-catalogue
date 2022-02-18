package eu.intelcomp.catalogue.domain;

import java.util.ArrayList;
import java.util.List;

public class Arguments {

    private ServiceArguments serviceArguments;
    private List<String> callerAttributes = null;
    private List<JobArgument> jobArguments = new ArrayList<>();

    public Arguments() {
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

    public List<String> getCallerAttributes() {
        return callerAttributes;
    }

    public void setCallerAttributes(List<String> callerAttributes) {
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

    public class JobArgument {
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
