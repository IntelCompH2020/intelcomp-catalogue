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

