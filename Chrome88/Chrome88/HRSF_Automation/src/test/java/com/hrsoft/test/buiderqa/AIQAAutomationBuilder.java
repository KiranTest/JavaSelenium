package com.hrsoft.test.buiderqa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.csv.CSVRecord;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrsoft.config.ConfigFactory;
import com.hrsoft.constants.Constants;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.csvprocessing.CSVFileProcess;
import com.hrsoft.utils.csvprocessing.CSVReader;
import com.hrsoft.utils.files.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AIQAAutomationBuilder extends WebBaseTest {

	private static final String API_URL = ConfigFactory.getConfig().API_URL();
	private static final String API_KEY = ConfigFactory.getConfig().API_KEY();
	private static String input = "";
	private static String relationships = "";
	private static List<String> fileFeeds;
	private static List <String> reportColumns= new ArrayList<> () ;
	private static List <String> reportColumns1= new ArrayList<> () ;
	private static String contentValue1 = "";
	private static List<String> dataObjectNames;
	private static AIQAAutomationBuilder aiqaAutomationBuilder = new AIQAAutomationBuilder();

	@Test
	public static void generatePromptResponse() {
		String userPrompt1 = ConfigFactory.getConfig().userPrompt1();
		userPrompt1 = userPrompt1.replace("\"", "\\\"").replace("\n", "\\n");
		String requestBody1 = createRequestBody(userPrompt1);
		printPrettyJson(requestBody1);
		String jsonResponse1 = makeApiRequest(requestBody1);
		if (jsonResponse1 != null) {
			printPrettyJson(jsonResponse1);
			 contentValue1 = extractContentValue(jsonResponse1).replace("\"", "\\\"").replace("\n", "\\n");
			String userPrompt2 = ConfigFactory.getConfig().userPrompt2();
			userPrompt2 = userPrompt2.replace("\"", "\\\"").replace("\n", "\\n");
			String requestBody2 = createRequestBodyWithSystemAndUserContent(contentValue1, userPrompt2);
			printPrettyJson(requestBody2);
			String jsonResponse2 = makeApiRequest(requestBody2);

			if (jsonResponse2 != null) {
				printPrettyJson(jsonResponse2);
				String contentValue2 = extractContentValue(jsonResponse2).replace("\"", "\\\"").replace("\n", "\\n");
				input = extractContentValue(jsonResponse2);
				String userPrompt3 = ConfigFactory.getConfig().userPrompt3();
				userPrompt3 = userPrompt3.replace("\"", "\\\"").replace("\n", "\\n");
				String requestBody3 = createRequestBodyWithSystemAndUserContent(contentValue2, userPrompt3);
				printPrettyJson(requestBody3);
				String jsonResponse3 = makeApiRequest(requestBody3);

				if (jsonResponse3 != null) {
					relationships = extractContentValue(jsonResponse3);
					String contentValue3 = extractContentValue(jsonResponse2).replace("\"", "\\\"").replace("\n",
							"\\n");
					printPrettyJson(jsonResponse3);
//					String userPrompt4 = ConfigFactory.getConfig().userPrompt4();
//					userPrompt4 = userPrompt4.replace("\"", "\\\"").replace("\n", "\\n");
//					String requestBody4 = createRequestBodyWithSystemAndUserContent(contentValue3, userPrompt4);
//					printPrettyJson(requestBody4);
//					String jsonResponse4 = makeApiRequest(requestBody4);
//
//					if (jsonResponse4 != null) {
//						fileFeeds = extractContentValue(jsonResponse4);
//						printPrettyJson(jsonResponse4);
//						String contentValue4 = extractContentValue(jsonResponse4).replace("\"", "\\\"").replace("\n",
//								"\\n");
					String userPrompt4 = ConfigFactory.getConfig().userPrompt4();
					userPrompt4 = userPrompt4.replace("\"", "\\\"").replace("\n", "\\n");
					String requestBody4 = createRequestBodyWithSystemAndUserContent(contentValue2, userPrompt4);
					printPrettyJson(requestBody4);
					String jsonResponse4 = makeApiRequest(requestBody4);

					if (jsonResponse4 != null) {
						printPrettyJson(jsonResponse4);
					}
				}
			}
		}
		String inputText = input;
		inputText = inputText.substring(1, inputText.length() - 1);
		createFile(inputText, Constants.CSV_DOBJECT_FILES_PATH + "/base_dataObject_definitions.csv");

		String relation = relationships;
		relation = relation.substring(1, relation.length() - 1);
		createFile(relation, Constants.CSV_DOBJECT_FILES_PATH + "/DataObjectRelationships.csv");

		CSVFileProcess.splitCsvFileByTableName(Constants.CSV_INIT_DOBJECT_DEF_FILE, Constants.CSV_DOBJECT_FILES_PATH,
				",");
		dataObjectNames = FileUtils.collectDataObjectCSVList(Constants.CSV_DOBJECT_FILES_PATH);
		fileFeeds = generateFileFeedsForTables(dataObjectNames);
		AIQAAutomationBuilder.createFileFeeds();

	}

	@Test
	public static void createFileFeeds() {
		String fileFeed = "";
		String    contentValueFile="";
		List<String> dataObjectNames = FileUtils.collectDataObjectCSVList(Constants.CSV_DOBJECT_FILES_PATH);
		CSVReader csvReader = new CSVReader();
		for (String s : dataObjectNames) {
            List<CSVRecord> csvRecords = csvReader.readCSVData(Constants.CSV_DOBJECT_FILES_PATH + "/DO_" + s + ".csv");
            for (CSVRecord record : csvRecords) {
                reportColumns1.add(record.get("ColumnName"));
            }
		}
		String userPrompt = String.format(ConfigFactory.getConfig().userPrompt5(), reportColumns1);
            String requestBody1 = createRequestBody(userPrompt);
            printPrettyJson(requestBody1);
            printPrettyJson(requestBody1);
            String jsonResponse1 = makeApiRequest(requestBody1);
            if (jsonResponse1 != null) {
                printPrettyJson(jsonResponse1);
                 contentValueFile = extractContentValue(jsonResponse1).replace("\"", "\\\"").replace("\n", "\\n");
            }
		
		for (String s : dataObjectNames) {
			List<CSVRecord> csvRecords = csvReader.readCSVData(Constants.CSV_DOBJECT_FILES_PATH + "/DO_" + s + ".csv");
			for (CSVRecord record : csvRecords) {
				reportColumns.add(record.get("ColumnName"));
			}
			
			String userPrompt5 = String.format(ConfigFactory.getConfig().userPrompt5(), reportColumns);
			String requestBody5 = createRequestBodyWithSystemAndUserContent(contentValueFile, userPrompt5);
			printPrettyJson(requestBody5);
			String jsonResponse5 = makeApiRequest(requestBody5);
			if (jsonResponse5 != null) {
				fileFeed = extractContentValue(jsonResponse5);
				printPrettyJson(jsonResponse5);
			}
			fileFeed = fileFeed.substring(0, fileFeed.length());
			createFile(fileFeed, Constants.CSV_DOBJECT_FILES_PATH + "/filefeeds/1_test_" + s + ".csv");
			reportColumns.clear();
		}
	}
    
	private void generateAllFiles() {
		CSVFileProcess.splitCsvFileByTableName(Constants.CSV_INIT_DOBJECT_DEF_FILE, Constants.CSV_DOBJECT_FILES_PATH,
				",");
		dataObjectNames = FileUtils.collectDataObjectCSVList(Constants.CSV_DOBJECT_FILES_PATH);
		fileFeeds = generateFileFeedsForTables(dataObjectNames);
		AIQAAutomationBuilder.createFileFeeds();
	}

	public static List<String> generateFileFeedsForTables(List<String> dataObjNames) {
		String prefix = "1_test"; // Change the prefix here if needed
		List<String> fileFeeds = IntStream.range(0, dataObjNames.size())
				.mapToObj(i -> prefix + "_" + dataObjNames.get(i)).collect(Collectors.toList());

		return Arrays.asList(fileFeeds.toArray(new String[0]));
	}
	// Test End

	private static void createFile(String inputText, String path) {
		String[] rows = inputText.split("\\n");
		StringBuilder csvContent = new StringBuilder();
		for (String row : rows) {
			csvContent.append(row).append("\n");
		}
		try {
			FileWriter csvFileWriter = new FileWriter(path);
			csvFileWriter.write(csvContent.toString());
			csvFileWriter.close();
			System.out.println("CSV file created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String createRequestBody(String content) {
		return "{\"model\": \"gpt-4\"," + "\"messages\": [{" + "\"role\": \"user\"," + "\"content\": \"" + content
				+ "\"" + "}]," + "\"temperature\": 0" + "}";
	}

	private static String createRequestBodyWithSystemAndUserContent(String Content, String userPrompt) {
		return "{\"model\": \"gpt-4\"," + "\"messages\": [{" + "\"role\": \"assistant\"," + "\"content\": \"" + Content
				+ "\"" + "},{" + "\"role\": \"user\"," + "\"content\": \"" + userPrompt + "\"" + "}],"
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
}
