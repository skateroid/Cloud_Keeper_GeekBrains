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
    private int partsCount;
    private int partNumber;

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

    public DataTransferMessage(String fileName, byte[] data, int partsCount, int partNumber) {
        this.fileName = fileName;
        this.data = data;
        this.partsCount = partsCount;
        this.partNumber = partNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public int getPartsCount() {
        return partsCount;
    }

    public int getPartNumber() {
        return partNumber;
    }
}
