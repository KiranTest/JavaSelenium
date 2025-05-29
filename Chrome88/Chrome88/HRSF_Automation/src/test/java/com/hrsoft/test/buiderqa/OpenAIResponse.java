package com.hrsoft.test.buiderqa;

import org.json.JSONArray;
import org.json.JSONObject;


public class OpenAIResponse {
	private String conversationId;
    private String messageContent;

    public OpenAIResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extract conversation ID
            if (jsonObject.has("id")) {
                this.conversationId = jsonObject.getString("id");
            }

            // Extract message content
            if (jsonObject.has("choices")) {
                JSONArray choicesArray = jsonObject.getJSONArray("choices");
                if (choicesArray.length() > 0) {
                    JSONObject choice = choicesArray.getJSONObject(0);
                    JSONObject message = choice.getJSONObject("message");
                    if (message.has("content")) {
                        this.messageContent = message.getString("content");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getMessageContent() {
        return messageContent;
    }

}
