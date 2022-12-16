package my.archiver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Archiver {

    private static final int SEVEN_BYTES_MASK = 127;
    private static final int EIGHT_BYTE = 128;

    private final FileInputStream _fis;
    private final FileOutputStream _fos;
    private final long _inputSize;

    public Archiver(FileInputStream _fis, FileOutputStream fos, long inputSize) {
        this._fis = _fis;
        this._fos = fos;
        _inputSize = inputSize;
    }

    public long encode() throws IOException {
        long total = 0;
        ArrayList<Byte> bytes = new ArrayList<>();
        byte first = (byte) _fis.read();
        byte buff = 0;
        boolean skip = false;
        int i = 1;
        while (i <= _inputSize) {
            int count = 0;
            if (!skip) {
                buff = (byte) _fis.read(); i++;
            }
            if (first == buff) {
                count++;
                while (first == buff && count < 129) {
                    count++;
                    if (i > _inputSize) {
                        break;
                    }
                    buff = (byte) _fis.read(); i++;
                }
                skip = false;
                _fos.write((byte) ((byte) (count - 2) | EIGHT_BYTE));
                _fos.write(first);
//                bytes.add((byte) ((byte) (count - 2) | EIGHT_BYTE));
//                bytes.add(first);
                total += 2;
                first = buff;
            } else {
                ArrayList<Byte> cache = new ArrayList<>();
                while (first != buff && count < 128) {
                    count++;
                    cache.add(first);
                    first = buff;
                    if (i > _inputSize) {
                        break;
                    }
                    buff = (byte) _fis.read(); i++;
                }
                skip = true;
                total += cache.size() + 1;
                _fos.write((byte) ((byte) (count - 1) & SEVEN_BYTES_MASK));
                _fos.write(byteListToByteArray(cache));
//                bytes.add((byte) ((byte) (count - 1) & SEVEN_BYTES_MASK));
//                bytes.addAll(cache);
            }
        }
        return total;
    }

    public long decode() throws IOException {
        long total = 0;
        int i = 0;
        while (i < _inputSize) {
            byte countByte = (byte) _fis.read(); i++;
            if ((countByte | SEVEN_BYTES_MASK) == (byte) 255) {
                int count = Byte.toUnsignedInt((byte) (countByte & SEVEN_BYTES_MASK));
                byte buff = (byte) _fis.read(); i++;
                for (int j = 0; j < count + 2; j++) {
                    _fos.write(buff);
                    total++;
                }
            } else {
                int count = Byte.toUnsignedInt(countByte);
                for (int j = 0; j < count + 1; j++) {
                    _fos.write(_fis.read());  i++;
                    total++;
                }
            }
        }
        return total;
    }

    public boolean Save() {
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
