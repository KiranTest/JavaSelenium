package com.hrsoft.test.buiderqa;

import java.io.FileWriter;
import java.io.IOException;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrsoft.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ApiConversation3Promots {

	private static final String API_URL = "https://api.openai.com/v1/chat/completions";
	private static final String API_KEY = "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";

	@Test
	public  void generatePromptResponse() {
		
		// Prompt 1
		String userPrompt1 = "Provide a overview of Long-Term Incentive Plans (LTIP) within the framework of Human Capital Management (HCM) for a software product. The output should be free from any unnecessary text and spaces.";
		// Escape double quotes and line breaks in the paragraph
		userPrompt1 = userPrompt1.replace("\"", "\\\"").replace("\n", "\\n");
		String requestBody1 = createRequestBody(userPrompt1);
		printPrettyJson(requestBody1);
		String jsonResponse1 = makeApiRequest(requestBody1);
		if (jsonResponse1 != null) {
			printPrettyJson(jsonResponse1);
			System.out.println("******_________________________________________");

			String conversationId1 = extractConversationId(jsonResponse1);
			// Extract the value of 'content' from the JSON response
            String contentValue1 = extractContentValue(jsonResponse1).replace("\"", "\\\"").replace("\n", "\\n");
			// Create a new request with system message first, followed by user message
			String userPrompt2 = "Using the overview of LTIP, design a CSV-format database schema for LTIP. The schema must accommodate advanced functionalities, auditing, and historical data. Format the CSV with the following columns: TableName, ColumnName, DataType, DataLength, Precision, Scale, DefaultValue, ColumnOrder, Cardinality, Constraints (choose either ForeignKey or PrimaryKey), and DataDictionary. Provide only CSV; no extra texts or spaces are needed. Use DB Normalization principles to organize the columns (attributes) and tables (relations) of the database to ensure that their dependencies are properly enforced by database integrity constraints. Above all you must ensure that the table structure is a maximally efficient relational database. Essentially, databases should be organized to decrease redundancy and avoid dependence anomalies.";
			userPrompt2 = userPrompt2.replace("\"", "\\\"").replace("\n", "\\n");
			String requestBody2 = createRequestBodyWithSystemAndUserContent(contentValue1, userPrompt2);
			printPrettyJson(requestBody2);
			String jsonResponse2 = makeApiRequest(requestBody2);
			
			if (jsonResponse2 != null) {
				printPrettyJson(jsonResponse2);
				System.out.println("******_________________________________________");
				// Extract the conversation ID from the response of the second request
				String conversationId2 = extractConversationId(jsonResponse2);
				
				// Extract the value of 'content' from the JSON response
				String contentValue2 = extractContentValue(jsonResponse2);
				testMile(contentValue2);
	            contentValue2 = extractContentValue(jsonResponse2).replace("\"", "\\\"").replace("\n", "\\n");
				
	            // Prompt 3
				String userPrompt3 = "Based on the above content, articulate the inter-table relationships in a CSV layout. The columns to be used are: ParentTable, ChildTable, ParentColumn, ChildColumn, RelationshipType, OnDelete, and OnUpdate. The output should remain free from spaces.";
				userPrompt3 = userPrompt3.replace("\"", "\\\"").replace("\n", "\\n");
				String requestBody3 = createRequestBodyWithSystemAndUserContent(contentValue2, userPrompt3);
				printPrettyJson(requestBody3);
				String jsonResponse3 = makeApiRequest(requestBody3);
				
				if (jsonResponse3 != null) {
					printPrettyJson(jsonResponse3);
					System.out.println("******_________________________________________");
					// Extract the conversation ID from the response of the second request
					String conversationId3 = extractConversationId(jsonResponse3);
					// Extract the value of 'content' from the JSON response
		            String contentValue3 = extractContentValue(jsonResponse3).replace("\"", "\\\"").replace("\n", "\\n");
					// Prompt 4
					String userPrompt4 = "With the LTIP schema for HCM, create sample data for a pharmaceutical enterprise in CSV format. Offer three sample entries for each table, ensuring each table's title is enclosed in double quotation marks. Provide only CSV no extra texts or spaces needed.";
					userPrompt4 = userPrompt4.replace("\"", "\\\"").replace("\n", "\\n");
					String requestBody4 = createRequestBodyWithSystemAndUserContent(contentValue2, userPrompt4);
					printPrettyJson(requestBody4);
					String jsonResponse4 = makeApiRequest(requestBody4);
					
					if (jsonResponse4 != null) {
						printPrettyJson(jsonResponse4);
						System.out.println("******_________________________________________");
						// Extract the conversation ID from the response of the second request
						String conversationId4 = extractConversationId(jsonResponse4);
						// Extract the value of 'content' from the JSON response
			            String contentValue4 = extractContentValue(jsonResponse4).replace("\"", "\\\"").replace("\n", "\\n");
						// Prompt 5
						String userPrompt5 = "Generate a comprehensive report using the LTIP sample data that merges necessary fields from all tables. Provide only CSV no extra texts or spaces needed.";
						userPrompt5 = userPrompt5.replace("\"", "\\\"").replace("\n", "\\n");
						String requestBody5 = createRequestBodyWithSystemAndUserContent(contentValue4, userPrompt5);
						printPrettyJson(requestBody5);
						String jsonResponse5 = makeApiRequest(requestBody5);
					
						if (jsonResponse5 != null) {
							printPrettyJson(jsonResponse5);
							System.out.println("******_________________________________________");
						}

					}
				}
			}
		}
	}

	private static String createRequestBody(String content) {
		return "{\"model\": \"gpt-4\"," + "\"messages\": [{" + "\"role\": \"user\"," + "\"content\": \"" + content
				+ "\"" + "}],"  + "\"temperature\": 0" + "}";
	}

	private static String createRequestBodyWithSystemAndUser(String conversationId, String userPrompt) {
		return "{\"model\": \"gpt-4\"," + "\"messages\": [{" + "\"role\": \"assistant\"," + "\"content\": \""
				+ conversationId + "\"" + "},{" + "\"role\": \"user\"," + "\"content\": \"" + userPrompt + "\"" + "}],"
				+ "\"top_p\": 1," + "\"temperature\": 0" + "}";
	}
	
	private static String createRequestBodyWithSystemAndUserContent(String Content, String userPrompt) {
		return "{\"model\": \"gpt-4\"," + "\"messages\": [{" + "\"role\": \"assistant\"," + "\"content\": \""
				+ Content + "\"" + "},{" + "\"role\": \"user\"," + "\"content\": \"" + userPrompt + "\"" + "}],"
			    + "\"temperature\": 0" + "}";
	}

	private static String createRequestBodyWithSystemAndUser(String conversationId, String content, String userPrompt) {
		return "{\"model\": \"gpt-4\"," + "\"messages\": [{" + "\"role\": \"system\"," + "\"content\": \""
				+ conversationId + "\"" + "},{" + "\"role\": \"system\"," + "\"content\": \"" + content + "\"" + "},{"
				+ "\"role\": \"user\"," + "\"content\": \"" + userPrompt + "\"" + "}]," + "\"top_p\": 1,"
				+ "\"temperature\": 0" + "}";
	}

	private static String makeApiRequest(String requestBody) {
		Response response = RestAssured.given().header("Content-Type", ContentType.JSON)
				.header("Authorization", "Bearer " + API_KEY).body(requestBody).post(API_URL);

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
	
	public void testMile(String inputText) {
	      //  String inputText = "\"TableName,ColumnName,DataType,DataLength,Precision,Scale,DefaultValue,ColumnOrder,Cardinality,ForeignKey,UniqueKey,Constraints,DataDictionary\nEmployee,EmployeeID,INT,10,,,,1,1,,YES,NOT NULL,Unique identifier for each employee\nEmployee,FirstName,VARCHAR,50,,,,2,,,,,Employee's first name\nEmployee,LastName,VARCHAR,50,,,,3,,,,,Employee's last name\nEmployee,DateOfJoining,DATE,,,,,4,,,,,Date when the employee joined the company\nEmployee,Position,VARCHAR,50,,,,5,,,,,Employee's position in the company\nLTIP,LTIPID,INT,10,,,,1,1,,YES,NOT NULL,Unique identifier for each LTIP\nLTIP,EmployeeID,INT,10,,,,2,,Employee,NO,NOT NULL,Foreign key referencing Employee\nLTIP,StartDate,DATE,,,,,3,,,,,Start date of the LTIP\nLTIP,EndDate,DATE,,,,,4,,,,,End date of the LTIP\nLTIP,Status,VARCHAR,20,,,,5,,,,,Current status of the LTIP\nLTIP,TargetKPI,DECIMAL,10,2,,,6,,,,,Target KPI for the LTIP\nLTIP,ActualKPI,DECIMAL,10,2,,,7,,,,,Actual KPI achieved\nLTIP,IncentiveType,VARCHAR,50,,,,8,,,,,Type of incentive offered\nLTIP,IncentiveValue,DECIMAL,10,2,,,9,,,,,Value of the incentive\nAudit,AuditID,INT,10,,,,1,1,,YES,NOT NULL,Unique identifier for each audit record\nAudit,LTIPID,INT,10,,,,2,,LTIP,NO,NOT NULL,Foreign key referencing LTIP\nAudit,ChangeDate,DATE,,,,,3,,,,,Date of the change\nAudit,ChangeType,VARCHAR,20,,,,4,,,,,Type of change (add, update, delete)\nAudit,ChangedBy,VARCHAR,50,,,,5,,,,,Employee who made the change\nHistoricalData,RecordID,INT,10,,,,1,1,,YES,NOT NULL,Unique identifier for each historical record\nHistoricalData,LTIPID,INT,10,,,,2,,LTIP,NO,NOT NULL,Foreign key referencing LTIP\nHistoricalData,RecordDate,DATE,,,,,3,,,,,Date of the record\nHistoricalData,RecordType,VARCHAR,20,,,,4,,,,,Type of record (add, update, delete)\nHistoricalData,RecordDetails,VARCHAR,255,,,,5,,,,,Details of the record\"";
	     // Remove the leading and trailing quotation marks
	        //inputText = inputText.substring(1, inputText.length() - 1);
	        createFile(inputText);
	        //createFile(inputText, Constants.CSV_DOBJECT_FILES_PATH +"/output.csv");
	    }
		
		private  void createFile(String inputText) {
		    // Split the input text by newline characters to get all rows
		    String[] rows = inputText.split("\\n");

		    // Create a StringBuilder to build the CSV content
		    StringBuilder csvContent = new StringBuilder();

		    // Iterate through the rows and append them to the CSV content
		    for (String row : rows) {
		        csvContent.append(row).append("\n");
		    }

		    // Write the CSV content to a CSV file
		    try {
		        FileWriter csvFileWriter = new FileWriter(Constants.CSV_DOBJECT_FILES_PATH +"/output.csv");
		        csvFileWriter.write(csvContent.toString());
		        csvFileWriter.close();
		        System.out.println("CSV file created successfully.");
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
}
