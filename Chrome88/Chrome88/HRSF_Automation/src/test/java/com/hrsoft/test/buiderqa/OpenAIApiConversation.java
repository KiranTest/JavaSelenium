package com.hrsoft.test.buiderqa;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrsoft.constants.Constants;

import java.io.FileWriter;
import java.io.IOException;

public class OpenAIApiConversation {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";
    
    @Test
    public static void generatePromptResponse() {
        String userPrompt1 = "Provide a comprehensive representation of Human Capital Management (HCM) that focuses on the design of Long-Term Incentive Plans (LTIP). First, create a CSV format detailing the database 7 table structures for HCM with the following columns: TableName, ColumnName, DataType, DataLength, Precision, Scale, DefaultValue, ColumnOrder, Cardinality, ForeignKey, UniqueKey, and DataDictionary. Ensure no spaces between outputs. Secondly, create another CSV format listing the relationships between these tables. This should include columns: ParentTable, ChildTable, ParentColumn, ChildColumn, RelationshipType, OnDelete, and OnUpdate";

        // Escape double quotes and line breaks in the paragraph
        userPrompt1 = userPrompt1.replace("\"", "\\\"").replace("\n", "\\n");

        String requestBody1 = createRequestBody(userPrompt1);
        System.out.println(requestBody1);
        String jsonResponse1 = makeApiRequest(requestBody1);

        if (jsonResponse1 != null) {
        	printPrettyJson(jsonResponse1);
        	String conversationId = extractConversationId(jsonResponse1);
            // Extract the value of 'content' from the JSON response
            String contentValue = extractContentValue(jsonResponse1).replace("\"", "\\\"").replace("\n", "\\n");
            
            // Create a new request with system message first, followed by user message
            String userPrompt2 = "Based on the previously defined table structure for Human Capital Management (HCM) focusing on the design of Long-Term Incentive Plans (LTIP), generate sample data of 10 records for each table for pharmaceuticals company. Represent this information in a single CSV format. Preface each table's data with its name in double quotes. Ensure the tables are in a tabular format and that there are no spaces between any of the outputs.";
            String requestBody2 = createRequestBodyWithSystemAndUser(conversationId, contentValue, userPrompt2);
            System.out.println(requestBody2);
            String jsonResponse2 = makeApiRequest(requestBody2);
            

            if (jsonResponse2 != null) {
                printPrettyJson(jsonResponse2);
            }
        }
    }

    private static String createRequestBody(String content) {
        return "{\"model\": \"gpt-4\"," +
                "\"messages\": [{" +
                "\"role\": \"user\"," +
                "\"content\": \"" + content + "\"" +
                "}]," +
                "\"top_p\": 1,"+
                "\"temperature\": 0" +
                "}";
    }
    
    private static String createRequestBodyWithSystemAndUser(String conversationId, String content, String userPrompt) {
        return "{\"model\": \"gpt-4\"," +
                "\"messages\": [{" +
                "\"role\": \"system\"," +
                "\"content\": \"" + conversationId + "\"" +
                "},{" +
                "\"role\": \"system\"," +
                "\"content\": \"" + content + "\"" +
                "},{" +
                "\"role\": \"user\"," +
                "\"content\": \"" + userPrompt + "\"" +
                "}]," +
                "\"top_p\": 1," +
                "\"temperature\": 0" +
                "}";
    }

    private static String makeApiRequest(String requestBody) {
        Response response = RestAssured.given()
                .header("Content-Type", ContentType.JSON)
                .header("Authorization", "Bearer " + API_KEY)
                .body(requestBody)
                .post(API_URL);

        if (response.getStatusCode() == 200) {
            return response.getBody().asString();
        } else {
            System.out.println("API request failed with status code: " + response.getStatusCode());
            return null;
        }
    }

    private static void printPrettyJson(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
  
            System.out.println(prettyJson);
        } catch (Exception e) {
            System.out.println("Error parsing JSON response: " + e.getMessage());
        }
    }
    
    private static String extractContentValue(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            System.out.println("Error extracting content value: " + e.getMessage());
            return null;
        }
    }
    
    private static String extractConversationId(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            return jsonNode.get("id").asText();
        } catch (Exception e) {
            System.out.println("Error extracting conversation ID: " + e.getMessage());
            return null;
        }
    }
}

