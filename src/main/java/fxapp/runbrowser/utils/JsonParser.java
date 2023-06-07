package fxapp.runbrowser.utils;

import fxapp.runbrowser.model.OpenTabsState;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonParser {

    private final String FILE_NAME = "tabs.json";
    private final ObjectMapper MAPPER = new ObjectMapper();

    public void writeObjectToJsonFile(OpenTabsState state) {
        try {
            File file = new File(FILE_NAME);
            MAPPER.writeValue(file, state);
            System.out.printf("Properties were saved to JSON file: '%s' successfully.\n", FILE_NAME);
        } catch (IOException e) {
            System.out.printf("Properties can't be saved to JSON file: '%s' successfully.\n", FILE_NAME);
            System.out.println(e.getMessage());
        }
    }

    public OpenTabsState readObjectFromJsonFile() {
        OpenTabsState state = null;
        try {
            File file = new File(FILE_NAME);
            state = MAPPER.readValue(file, OpenTabsState.class);
            System.out.printf("Properties were read from JSON file: '%s' successfully.\n", FILE_NAME);
        } catch (IOException e) {
            System.out.printf("%s file was not found or could not be read\n", FILE_NAME);
        }
        return state;
    }
}
