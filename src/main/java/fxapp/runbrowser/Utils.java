package fxapp.runbrowser;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@UtilityClass
public class Utils {

    private final String TASK_LIST = "tasklist";
    private final String PROCESS_NAME = "chromedriver";

    public void killChromeProcess() {
        killAllProcessesByName(PROCESS_NAME);
    }

    public void killAllProcessesByName(String name) {
        if (!isProcessRunning(name)) return;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/IM", PROCESS_NAME + "*");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Process(es) killed successfully.");
            } else {
                System.out.println("Failed to kill the process(es).");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isProcessRunning(String name) {
        boolean isRunning = false;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(TASK_LIST, "/FI", "IMAGENAME eq " + name + "*");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
            isRunning = output.toString().contains(name);
            if (isRunning) {
                System.out.println("The process is running.");
            } else {
                System.out.println("The process is not running.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isRunning;
    }
}
