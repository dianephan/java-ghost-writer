import java.util.*;
import java.util.function.Supplier;
import java.util.ArrayList;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.engine.Engine;
import com.theokanning.openai.search.SearchRequest;
import com.theokanning.openai.completion.CompletionChoice;
import static spark.Spark.*;

public class GhostWriter {
    public static void main(String... args) {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);
        Engine davinci = service.getEngine("davinci");
        ArrayList<CompletionChoice> storyArray = new ArrayList<CompletionChoice>();
        System.out.println("\nBrewing up a story...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("The following is a spooky story written for kids, just in time for Halloween. Everyone always talks about the old house at the end of the street, but I couldn’t believe what happened when I went inside.")
                .temperature(0.7)
                .maxTokens(96)
                .topP(1.0)
                .frequencyPenalty(0.0)
                .presencePenalty(0.3)
                .echo(true)     // echoes back the prompt in addition to tokens
                .build();
        service.createCompletion("davinci", completionRequest).getChoices().forEach(line -> {storyArray.add(line);});
        System.out.println("The story array = " + storyArray);
        get("/", (req, res) -> storyArray);
    }
}