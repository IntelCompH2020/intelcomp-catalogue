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
