package eu.intelcomp.catalogue.domain;

import org.json.simple.JSONObject;

public class ChapterAnswer {

    private String chapterId;
    private JSONObject answer;

    public ChapterAnswer() {
        answer = new JSONObject();
    }

    public ChapterAnswer(String id, JSONObject answer) {
        this.answer = answer;
    }

    public ChapterAnswer(String chapterAnswerId, String chapterId) {
        this.answer = new JSONObject();
        this.setId(chapterAnswerId);
        this.chapterId = chapterId;
    }

    public String getId() {
        return answer.get("id").toString();
    }

    public void setId(String id) {
        this.answer.put("id", id);
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public JSONObject getAnswer() {
        return answer;
    }

    public void setAnswer(JSONObject answer) {
        this.answer = answer;
    }
}

