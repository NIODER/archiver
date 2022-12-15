package my.archiver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Archiver {

    private static final int EIGHT_BYTE = 128;
    private static final int SEVEN_BYTES_MASK = 127;

    private boolean isEncode;
    private String inputPath;
    private String outputPath;

    public Archiver(boolean isEncode, String inputPath, String outputPath) {
        this.isEncode = isEncode;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public void start() throws IOException {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputPath));
        FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
        if (isEncode) {
            fileOutputStream.write(byteListToByteArray(encode(inputBytes)));
        } else {
            fileOutputStream.write(byteListToByteArray(decode(inputBytes)));
        }
    }

    private ArrayList<Byte> encode(byte[] inputBytes) {
        ArrayList<Byte> outputBytes = new ArrayList<>();
        for (int i = 0; i < inputBytes.length; i++) {
            if (inputBytes[i] == inputBytes[i + 1]) {
                byte sameBytesCount = 0;
                byte repeatedValue = inputBytes[i];
                for (; i < inputBytes.length; i++) {
                    if (inputBytes[i] == repeatedValue) {
                        sameBytesCount++;
                    } else {
                        break;
                    }
                }
                outputBytes.add((byte) (sameBytesCount | EIGHT_BYTE));
                outputBytes.add(repeatedValue);
            } else {
                byte differentBytesCount = 0;
                int j = i;
                while (inputBytes[j] != inputBytes[j + 1]) {
                    differentBytesCount++;
                    j++;
                }
                outputBytes.add((byte) (differentBytesCount & SEVEN_BYTES_MASK));
                for (int k = i; k < j; k++) {
                    outputBytes.add(inputBytes[k]);
                    i++;
                }
                i--;
            }
        }
        return outputBytes;
    }

    private ArrayList<Byte> decode(byte[] inputBytes) {
        ArrayList<Byte> output = new ArrayList<>();
        for (int i = 0; i < inputBytes.length; i++) {
            if ((inputBytes[i] | SEVEN_BYTES_MASK) == (byte) 255) {
                int count = Byte.toUnsignedInt((byte) (inputBytes[i] & SEVEN_BYTES_MASK));
                i++;
                for (int j = 0; j < count + 2; j++) {
                    output.add(inputBytes[i]);
                }
            } else {
                int count = Byte.toUnsignedInt(inputBytes[i]);
                for (int j = 0; j < count + 1 && i < inputBytes.length - 1; j++) {
                    i++;
                    output.add(inputBytes[i]);
                }
            }
        }
        return output;
    }

    private byte[] byteListToByteArray(ArrayList<Byte> byteArrayList) {
        byte[] bytes = new byte[byteArrayList.size()];
        for (int i = 0; i < byteArrayList.size(); i++) {
            bytes[i] = byteArrayList.get(i);
        }
        return bytes;
    }
}
