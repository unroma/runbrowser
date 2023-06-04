package fxapp.runbrowser;

import fxapp.runbrowser.model.OpenTabsState;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Parser {

    private final String FILE_NAME = "tabs.json";
    private final ObjectMapper MAPPER = new ObjectMapper();


    public void writeObjectToJsonFile(OpenTabsState state) {
        try {
            File file = new File(FILE_NAME);
            MAPPER.writeValue(file, state);
            System.out.println("Object saved to JSON file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OpenTabsState readObjectFromJsonFile() {
        OpenTabsState state = null;
        try {
            File file = new File(FILE_NAME);
            state = MAPPER.readValue(file, OpenTabsState.class);
            System.out.println("Object was read from JSON file successfully.");
        } catch (IOException e) {
            System.out.printf("%s file was not found or could not be read%n", FILE_NAME);
        }
        return state;
    }
}
