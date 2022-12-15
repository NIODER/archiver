package my.archiver;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
//        Archiver archiver = new Archiver(false, "", "");
//        byte[] data = { 1, 1, 2, 3, 4, 5, 5, 5 };
//        byte[] data1 = { 1, 2, 3, 3, 3, 3, 3, 3 };
//        for (int i = 0; i < data1.length; i++) {
//            System.out.print(data1[i] + " ");
//        }
//        var out = archiver.encode(data1);
//        System.out.println();
//        for (int i = 0; i < out.size(); i++) {
//            System.out.print(out.get(i) + " ");
//        }
        Validator validator = new Validator(args);
        if (validator.Validate()) {
            Archiver archiver = new Archiver(
                    validator.isEncode(),
                    validator.getInputPath(),
                    validator.getOutputPath()
            );
            try {
                archiver.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.print(validator.getExceptionMessage());
        }
    }
}
