import static spark.Spark.*;
import org.apache.poi.xwpf.usermodel.*;

import spark.Service.StaticFiles;

import java.io.*;

import static spark.Spark.*;
import java.io.*;
import org.apache.poi.xwpf.usermodel.*;

public class Server {
    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        GPT chat = new GPT();
        if (apiKey == null) {
            System.out.println("API key is not set. Please set the environment variable OPENAI_API_KEY.");
            System.exit(1);
        }
        
        String port = System.getenv("PORT");
        if (port == null) {
            // If PORT is not set, use a default value for local testing
            port = "4567";  // Default port for local testing
        }

        int portNumber = Integer.parseInt(port);
        port(portNumber);																									
        /*
        staticFiles.location("/public");
        */

        // CORS configuration: Allow requests from LinkedIn.
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.header("Access-Control-Allow-Credentials", "true");
        });

        // Preflight CORS request (for browsers to make OPTIONS requests)
        options("/*", (request, response) -> {
            response.status(200);
            return "OK";
        });

        /*
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });
		*/
        // Handle write request
        get("/write", (request, response) -> {
            String jobDesc = request.queryParams("jobDesc");
            String userDesc = request.queryParams("userDesc");
            String fullName = request.queryParams("fullName");
            String address = request.queryParams("address");
            String email = request.queryParams("email");
            String phoneNumber = request.queryParams("phoneNumber");
            String message = request.queryParams("message");

            System.out.println("jobDesc: " + jobDesc);
            System.out.println("userDesc: " + userDesc);
            System.out.println("fullName: " + fullName);
            System.out.println("address: " + address);
            System.out.println("email: " + email);
            System.out.println("phoneNumber: " + phoneNumber);
            System.out.println("message: " + message);
            String text = chat.sendMessageToChatGPT(apiKey, userDesc, jobDesc, email, address, phoneNumber);
            System.out.println(text);    

            try (XWPFDocument doc = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                XWPFParagraph paragraph = doc.createParagraph();
                XWPFRun run = paragraph.createRun();

               

                for (String line : text.split("\n")) {
                    run.setText(line);
                    run.addBreak(); 
                }

                run.setFontSize(12);
                run.setBold(false);

                doc.write(out);
                
                
               

                byte[] byteArray = out.toByteArray();
                System.out.println("Generated byte array size: " + byteArray.length);

                response.header("Content-Disposition", "attachment; filename=\"coverLetter.docx\"");
                response.type("application/msword");  

                response.raw().getOutputStream().write(byteArray);
                response.raw().getOutputStream().flush();
                response.raw().getOutputStream().close();

                return null;
            } catch (IOException e) {
                e.printStackTrace();
                response.status(500);
                return "Error creating document.";
            }
        });

       }
}

  