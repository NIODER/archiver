package my.archiver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Archiver {

    private static final int SEVEN_BYTES_MASK = 127;
    private static final int EIGHT_BYTE = 128;

    private boolean isEncode;
    private String archiveName;
    private File outputFile;
    private FileOutputStream _fos;

    public Archiver(File input, File output, boolean isEncode) {
        if (isEncode) {
            outputFile = new File(getFreeArchiveName(input.getName(), output.getPath()));
        } else {

        }
    }



    private String getFreeArchiveName(String inputName, String outPath) {
        int additive = 1;
        String name = inputName.split("\\.")[0];
        inputName = name + Constants.archiveSuffix;
        while (Files.exists(Paths.get(outPath + "\\" + inputName))) {
            inputName = name + "(" + additive++ + ")" + Constants.archiveSuffix;
        }
        return outPath + "\\" + inputName;
    }

    public int getFilesCount(File file) {
        int count = 0;
        File[] folderEntries = file.listFiles();
        if (folderEntries == null) {
            return 1;
        }
        for (File entry : folderEntries) {
            count += getFilesCount(entry);
        }
        return count;
    }

    public void encode() {
        File[] folderEntries = outputFile.listFiles();

    }



    public long encodeFile(FileInputStream fis, FileOutputStream fos, long inputSize) throws IOException {
        long total = 0;
        ArrayList<Byte> bytes = new ArrayList<>();
        byte first = (byte) fis.read();
        byte buff = 0;
        boolean skip = false;
        int i = 1;
        while (i <= inputSize) {
            int count = 0;
            if (!skip) {
                buff = (byte) fis.read(); i++;
            }
            if (first == buff) {
                count++;
                while (first == buff && count < 129) {
                    count++;
                    if (i > inputSize) {
                        break;
                    }
                    buff = (byte) fis.read(); i++;
                }
                skip = false;
                fos.write((byte) ((byte) (count - 2) | EIGHT_BYTE));
                fos.write(first);
                total += 2;
                first = buff;
            } else {
                ArrayList<Byte> cache = new ArrayList<>();
                while (first != buff && count < 128) {
                    count++;
                    cache.add(first);
                    first = buff;
                    if (i > inputSize) {
                        break;
                    }
                    buff = (byte) fis.read(); i++;
                }
                skip = true;
                total += cache.size() + 1;
                fos.write((byte) ((byte) (count - 1) & SEVEN_BYTES_MASK));
                fos.write(byteListToByteArray(cache));
            }
        }
        return total;
    }

    public long decode(FileInputStream fis, FileOutputStream fos, long inputSize) throws IOException {
        long total = 0;
        int i = 0;
        while (i < inputSize) {
            byte countByte = (byte) fis.read(); i++;
            if ((countByte | SEVEN_BYTES_MASK) == (byte) 255) {
                int count = Byte.toUnsignedInt((byte) (countByte & SEVEN_BYTES_MASK));
                byte buff = (byte) fis.read(); i++;
                for (int j = 0; j < count + 2; j++) {
                    fos.write(buff);
                    total++;
                }
            } else {
                int count = Byte.toUnsignedInt(countByte);
                for (int j = 0; j < count + 1; j++) {
                    fos.write(fis.read());  i++;
                    total++;
                }
            }
        }
        return total;
    }

    public boolean Save(FileOutputStream _fos) {
        try {
            _fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private byte[] byteListToByteArray(ArrayList<Byte> byteArrayList) {
        byte[] bytes = new byte[byteArrayList.size()];
        for (int i = 0; i < byteArrayList.size(); i++) {
            bytes[i] = byteArrayList.get(i);
        }
        return bytes;
    }
}
