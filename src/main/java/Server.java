import static spark.Spark.*;
import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
public class Server {
	static String apiKey = System.getenv("OPENAI_API_KEY");
	

	public static void main(String[] args) {
		
		if (apiKey == null) {
		    System.out.println("API key is not set. Please set the environment variable OPENAI_API_KEY.");
		    System.exit(1);
		}
		
		GPT chat = new GPT();
		 staticFiles.location("/public"); // looks for resources in src/main/resources/public

	        get("/", (req, res) -> {
	            res.redirect("/index.html");
	            return null;
	        });
	    
		
        // Define a GET endpoint: http://localhost:4567/write?message=Hello
        get("/write", (request, response) -> {
            System.out.println("🔥 /write endpoint was hit");

        	String jobDesc = request.queryParams("message");
        	System.out.println("MESSAGEEEE: " + jobDesc);
        	
        	String email = request.queryParams("email");
        	String address = request.queryParams("address");
        	String phone = request.queryParams("phone");
        	String name = request.queryParams("name");
        	System.out.println(name + "  " + phone + "  " + address);

            
            String userDescription = request.queryParams("userDescription"); // User self-description////////////////////////
            //userDescription = "";///// not should be!!!!!
            System.out.println("message: " + jobDesc);
            System.out.println("userDescription: " + userDescription);
            
            String text = chat.sendMessageToChatGPT(apiKey, userDescription,jobDesc, email,address,phone);
            System.out.println("REached");
            
            
            String path = "C:\\Users\\David\\Downloads\\coverLetter.docx";
            try (XWPFDocument doc = new XWPFDocument();
                FileOutputStream out = new FileOutputStream(path)) {

                XWPFParagraph paragraph = doc.createParagraph();
                XWPFRun run = paragraph.createRun();
                
                for (String line : text.split("\n")) {
                    run.setText(line);
                    run.addBreak(); // this adds a new line
                }
                
                run.setFontSize(12);
	            run.setBold(false);
                

                doc.write(out);
                System.out.println("✅ Word file written to " + path);
            } catch (IOException e) {
                e.printStackTrace();
                return "Error writing to file";
            }

            return "Word file created with message: " + text;
        });

        System.out.println("🚀 Server running at http://localhost:4567/write?message=YourMessage");
    }
}
