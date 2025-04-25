import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.time.LocalDate;


public class GPT {
	static LocalDate today = LocalDate.now();
	static String context = "Attach the following info(make it apear at the top)- Address: Woodcrest 5429, Philadelphia. email: david.schwartzman@hotmail.com. number: 210-7904790. date : " + today.toString() 
			+" I am 27, I served one year in the IDF and I played soccer proffesionally in israel until I moved to college at 22. My bachelors is in buisness and administration and my master's is in computer science. "
			+ "I am the best at Java, I am a lab instructor where I teach Java. I also know python, C# and JavaScript. I have expirience with Postman, SpringBoot and Flutter. The job posting: "; 
	
	public static String sendMessageToChatGPT(String apiKey, String userMessage, String jobDesc, String email, String address,String phone) throws Exception {
        // Create the HTTP client
		String forEach = "Write a cover letter based on the information i give you, don't ever do something like [company name][address]etc...!!). Include the following information on top(if not null): " 
        +"\nemail:  " + email + "\nphone: " + phone + "\naddress: " + address + "\nDate: "+ today.toString();

		String cleanedMessage = sanitize(userMessage.trim().replaceAll("[\\r\\n]+", " "));
	    String cleanedContext = sanitize(jobDesc.trim().replaceAll("[\\r\\n]+", " "));

	    String fullMessage = forEach + " " + cleanedMessage + " " + cleanedContext;
	    

	    // Step 2: Build JSON safely with Gson
	    JsonObject userMsg = new JsonObject();
	    userMsg.addProperty("role", "user");
	    userMsg.addProperty("content", fullMessage);

	    JsonArray messages = new JsonArray();
	    messages.add(userMsg);

	    JsonObject requestBody = new JsonObject();
	    requestBody.addProperty("model", "gpt-3.5-turbo");
	    requestBody.add("messages", messages);

	    // Convert to JSON string
	    String jsonBody = requestBody.toString();

	    // Optional: Debug output
	    System.out.println("Sending JSON:\n" + jsonBody);

		// Step 3: Prepare HTTP request
		HttpClient client = HttpClient.newHttpClient();

        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey) // Using the API key here
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8)) // Send the body
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse the JSON response
        Gson gson = new Gson();
        JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);

        // Extract the response message from the response JSON
        String responseMessage = responseJson.getAsJsonArray("choices")
                                             .get(0)
                                             .getAsJsonObject()
                                             .getAsJsonObject("message")
                                             .get("content")
                                             .getAsString();

        return responseMessage;
    }	
	public static String escapeSpecialChars(String input) {
	    // Escape backslashes and double quotes
	    return input.replace("\\", "\\\\").replace("\"", "\\\"");
	}


	public static String sanitize(String input) {
	    // Remove non-ASCII characters and replace smart quotes
	    return input.replaceAll("[^\\x20-\\x7E]", "")  // Only printable ASCII
	                .replace("“", "\"")
	                .replace("”", "\"")
	                .replace("‘", "'")
	                .replace("’", "'");
	}
}

/**
	String cleanedMessage = userMessage.trim().replaceAll("[\\r\\n]+", " ");

	    // Escape special characters
	    String escapedMessage = escapeSpecialChars(cleanedMessage);

	    // Clean up the context similarly if needed
	    String escapedContext = escapeSpecialChars(context.trim().replaceAll("[\\r\\n]+", " "));

        HttpClient client = HttpClient.newHttpClient();

        // Construct the message body (JSON) for the API request
        String jsonBody = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"" +escapedContext+" " +escapedMessage + "\"}\n" +
                "  ]\n" +
                "}";
        System.out.println("Reached");
*
*/
