package eu.intelcomp.catalogue.domain;

import org.json.simple.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class ModelAnswer {

    private String id;
    private String modelId;
    private Map<String, ChapterAnswer> chapterAnswers;

    public ModelAnswer() {
        this.chapterAnswers = new TreeMap<>();
    }

    public ModelAnswer(JSONObject answer) {
        this.chapterAnswers = new TreeMap<>();
        String answerId = answer.get("id").toString();
        this.id = answerId;
        this.chapterAnswers.put(answerId, new ChapterAnswer(answerId, answer));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public Map<String, ChapterAnswer> getChapterAnswers() {
        return chapterAnswers;
    }

    public void setChapterAnswers(Map<String, ChapterAnswer> chapterAnswers) {
        this.chapterAnswers = chapterAnswers;
    }
}
