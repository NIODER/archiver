package my.archiver;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Validator validator = new Validator(args);
        if (validator.Validate()) {
            File file = new File(validator.getInputPath());
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(validator.getOutputPath());
            Archiver archiver = new Archiver(fileInputStream, fileOutputStream, file.length());
            if (validator.isEncode()) {
                System.out.println("Encoding...");
                System.out.println("Total output bytes: " + archiver.encode());
            } else {
                System.out.println("Decoding...");
                System.out.println("Total output bytes: " + archiver.decode());
            }
            System.out.println("Saving...");
            archiver.Save();
            System.out.println("Complete.");
        } else {
            System.out.print(validator.getExceptionMessage());
        }
    }
}
