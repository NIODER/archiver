package my.archiver;

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

    public boolean Validate() {
        return exceptionMessage == null;
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
