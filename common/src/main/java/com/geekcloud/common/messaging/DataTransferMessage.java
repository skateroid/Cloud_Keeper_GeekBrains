package com.geekcloud.common.messaging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataTransferMessage extends Message {
    //Класс для передачи данных
    private String fileName;
    private String path;
    private byte[] data;
    private int size;

    public DataTransferMessage(Path path) {
        this.path = path.toString();
        this.fileName = path.getFileName().toString();
        try {
            this.data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.size = data.length;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    public byte[] getData() {
        return data;
    }
}
