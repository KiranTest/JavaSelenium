package com.hrsoft.test.buiderqa;

import com.hrsoft.utils.csvprocessing.CSVFileProcess;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAIIntegration {
	

	@Test
    public static void openAIClient() { 
        
		String apiUrl = "https://api.openai.com/v1/chat/completions"; 
        String apiKey = "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"; 
        
        String userPrompt1 = "Please provide a comprehensive representation of Human Capital Management (HCM) that covers the complex design of Long-Term Incentive Plans (LTIP) with related tables, with detailed information presented in two different CSV formats.\n\n" +
                "For the first CSV format, present the database table structure in a tabular format with the following columns: \"TableName\" (without spaces), \"ColumnName,\" \"DataType,\" \"DataLength,\" \"Precision,\" \"Scale,\" \"DefaultValue,\" \"ColumnOrder,\" \"Cardinality,\" \"ForeignKey,\" \"UniqueKey,\" and \"DataDictionary.\" Ensure there are no spaces between the outputs.\n\n" +
                "For the second CSV format, list the relationships between these tables in a tabular format with the following columns: \"ParentTable,\" \"ChildTable,\" \"ParentColumn,\" \"ChildColumn,\" \"RelationshipType,\" \"OnDelete,\" and \"OnUpdate.\"";

   		
        String userPrompt2 = "Generate these tables with sample data of 5 records in a single CSV format, without spaces between tables, and with the table name added before the records in double quotes in a tabular format. Ensure there are no spaces between the outputs.";
        userPrompt1 = userPrompt1.replace("\"", "\\\"").replace("\n", "\\n");

        String requestBody = "{\"model\": \"gpt-4\"," +
                "\"messages\": [" +
                "{\"role\": \"user\"," +
                "\"content\": \"" + userPrompt1 + "\"}," +
                "{\"role\": \"user\"," +
                "\"content\": \"" + userPrompt2 + "\"}" +
                "]," +
                "\"temperature\": 0" +
                "}";

        // Make the POST API request with headers and the request body using RestAssured
        Response response = RestAssured.given()
                .header("Content-Type", ContentType.JSON)
                .header("Authorization", "Bearer " + apiKey)
                .body(requestBody)
                .post(apiUrl);

        // Print the API response
     // Check if the response status code is 200 (OK)
        if (response.getStatusCode() == 200) {
            // Parse the JSON response using Jackson
            String jsonResponse = response.getBody().asString();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
                
                // Print the formatted JSON
                System.out.println(prettyJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("API request failed with status code: " + response.getStatusCode());
        }
    }
} 	
    	
    	
//        String apiUrl = "https://api.openai.com/v1/chat/completions"; 
//        String headerName = "Authorization"; 
//        String headerValue = "Bearer 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"; // Replace with the actual header value
//        String prompt = "i would like to design a cloud application for HCM that does complex LTIP calculations for employees of an organization. Please design the database taking into account all the corner cases and use cases";
//
//        // Make the API request
//        Response response = sendApiRequest(apiUrl, headerName, headerValue, prompt);
//
//        // Process the API response
//        processApiResponse(response);


//    // Method to send the API request
//    public static Response sendApiRequest(String apiUrl, String headerName, String headerValue, String contentValue) {
//        String requestBody = "{\"content\": \"" + contentValue + "\"}";
//
//        return RestAssured.given()
//                .header(headerName, headerValue)
//                .contentType(ContentType.JSON)
//                .body(requestBody)
//                .post(apiUrl);
//    }
//
//    // Method to process the API response and save to CSV
//    public static void processApiResponse(Response response) {
//        if (response.getStatusCode() == 200) {
//            String parameterValue = response.jsonPath().getString("data.parameter1");
//            CSVFileProcess.saveToCsv("output.csv", "parameter1", parameterValue);
//            System.out.println("Parameter value saved to 'output.csv'.");
//        } else {
//            System.out.println("API request failed with status code: " + response.getStatusCode());
//        }
//    }


