package eu.intelcomp.catalogue.service;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jobs")
public class JobProperties {

    /**
     * Job data properties.
     */
    private Data data = new Data();

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        /**
         * Data directories.
         */
        Directories directories = new Directories();

        public Directories getDirectories() {
            return directories;
        }

        public void setDirectories(Directories directories) {
            this.directories = directories;
        }
    }

    public static class Directories {

        /**
         * Should contain the absolute path of the base directory.
         */
        String base = "/workdir";

        /**
         * Should contain the relative path of the input directory.
         */
        String inputRelativePath = "ui";

        /**
         * Should contain the relative path of the output directory.
         */
        String outputRelativePath = "output";

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getInputRelativePath() {
            return inputRelativePath;
        }

        public void setInputRelativePath(String inputRelativePath) {
            this.inputRelativePath = inputRelativePath;
        }

        public String getOutputRelativePath() {
            return outputRelativePath;
        }

        public void setOutputRelativePath(String outputRelativePath) {
            this.outputRelativePath = outputRelativePath;
        }
    }
}
