package com.hrsoft.test.buiderqa;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAIApiClient {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";
    
    @Test
    public static void generatePromptResponse() {
    	
//    	String userPrompt1 = "Please provide a comprehensive representation of Human Capital Management (HCM) that covers the complex design of Long-Term Incentive Plans (LTIP) with related tables, with detailed information presented in two different CSV formats.\n\n" +
//                "For the first CSV format, present the database table structure in a tabular format with the following columns: \"TableName\" (without spaces), \"ColumnName,\" \"DataType,\" \"DataLength,\" \"Precision,\" \"Scale,\" \"DefaultValue,\" \"ColumnOrder,\" \"Cardinality,\" \"ForeignKey,\" \"UniqueKey,\" and \"DataDictionary.\" Ensure there are no spaces between the outputs.\n\n" +
//                "For the second CSV format, list the relationships between these tables in a tabular format with the following columns: \"ParentTable,\" \"ChildTable,\" \"ParentColumn,\" \"ChildColumn,\" \"RelationshipType,\" \"OnDelete,\" and \"OnUpdate.\"";

    	String userPrompt1 = "Provide a comprehensive representation of Human Capital Management (HCM) that focuses on the design of Long-Term Incentive Plans (LTIP). First, create a CSV format detailing the database table structure for HCM with the following columns: TableName, ColumnName, DataType, DataLength, Precision, Scale, DefaultValue, ColumnOrder, Cardinality, ForeignKey, UniqueKey, and DataDictionary. Ensure no spaces between outputs. Secondly, create another CSV format listing the relationships between these tables. This should include columns: ParentTable, ChildTable, ParentColumn, ChildColumn, RelationshipType, OnDelete, and OnUpdate.";

   		
        String userPrompt2 = "Generate these tables with sample data of 5 records in a single CSV format, without spaces between tables, and with the table name added before the records in double quotes in a tabular format. Ensure there are no spaces between the outputs.";
        
            // Escape double quotes and line breaks in the paragraph
        userPrompt1 = userPrompt1.replace("\"", "\\\"").replace("\n", "\\n");

        String requestBody = createRequestBody(userPrompt1); 
        String jsonResponse = makeApiRequest(requestBody);

        if (jsonResponse != null) {
            printPrettyJson(jsonResponse);
        }
        
//        String requestBody1 = createRequestBody(userPrompt2); // truncated for brevity
//        String jsonResponse1 = makeApiRequest(requestBody1);
//
//        if (jsonResponse1 != null) {
//            printPrettyJson(jsonResponse1);
//        }
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
}

