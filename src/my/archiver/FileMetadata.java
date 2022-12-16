package my.archiver;

import java.nio.ByteBuffer;

public class FileMetadata {
    private static final int MAX_NAME_LENGTH = 260;

    public long _size;
    // [4; 260)
    public byte _nameSize;
    public String _name;

    public FileMetadata(long size, String name) {
        _size = size;
        _name = name;
        _nameSize = nameSize(name);
    }

    public FileMetadata(byte[] bytes) {
        _size = ByteBuffer.wrap(bytes, 0, 8).getLong();
        _nameSize = (byte) (bytes[8] + 4);
        _name = new String(ByteBuffer.wrap(bytes, 9, _nameSize).array());
    }

    private byte nameSize(String name) {
        int size = name.getBytes().length;
        if (size < 4) {
            throw new IllegalArgumentException("Too short name.");
        }
        if (size < 260) {
            return (byte) (size - 4);
        } else {
            throw new ClassCastException("Name is too long: " + size);
        }
    }

    public int size() {
        return 4 + (int) _nameSize + 1 + 8;
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[size()];
        byte[] sizeBytes = ByteBuffer.allocate(8).putLong(_size).array();
        byte[] nameBytes = _name.getBytes();
        for (int i = 0; i < 8; i++) {
            bytes[i] = sizeBytes[i];
        }
        bytes[8] = _nameSize;
        for (int i = 0; i < _nameSize; i++) {
            bytes[i + 9] = nameBytes[i];
        }
        return bytes;
    }
}
