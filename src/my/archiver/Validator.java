package my.archiver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Validator {

    private boolean isEncode;
    private String inputPath;
    private String outputPath;
    private String exceptionMessage;

    public Validator(String[] args) {
        if (args[0].equals("encode")) {
            isEncode = true;
        } else if (args[0].equals("decode")) {
            isEncode = false;
        } else {
            exceptionMessage = "Argument 1 should be \"encode\" or \"decode\"";
        }
        if (args[1] == null) {
            exceptionMessage = "No input path specified";
        } else {
            inputPath = args[1];
        }
        if (args[2] == null) {
            exceptionMessage = "No output path specified";
        } else {
            outputPath = args[2];
        }
    }

    private boolean isInputExists() {
        return Files.exists(Paths.get(inputPath));
    }

    private boolean isOutputIsDirectory() {
        return Files.isDirectory(Paths.get(outputPath));
    }

    private boolean isInputIsArchive() {
        return Paths.get(inputPath).endsWith(Constants.archiveSuffix);
    }

    public boolean Validate() {
        if (exceptionMessage != null) {
            return false;
        }
        if (!isInputExists()) {
            exceptionMessage = "Input does not exists.";
            return false;
        }
        if (!isOutputIsDirectory()) {
            exceptionMessage = "Output must be folder (directory).";
            return false;
        }
        if (!isEncode) {
            if (!isInputIsArchive()) {
                exceptionMessage = "Input must be an archive.";
                return false;
            }
        }
        return true;
    }

    public boolean isEncode() {
        return isEncode;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
