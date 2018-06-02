package com.geekcloud.common.messaging;

import java.io.File;

// класс, содержащий поля, которые могут понадобиться для отображения пользователю в таблице файлов
// можно расширить при необходимости
public class FileInfo {

    private FileType fileType;
    private String name;
    private long sizeInKb;

    public enum FileType {
        FILE (" "), DIR (">");
        String value;
        FileType(String value) { this.value = value; }
    }

    public FileInfo(File file) {
        if (file.exists()) {
            this.fileType = file.isDirectory() ? FileType.DIR : FileType.FILE;
            this.name = file.getName();
            this.sizeInKb = file.length() / 1024;
        }
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSizeInKb() {
        return sizeInKb;
    }

    public void setSizeInKb(long sizeInKb) {
        this.sizeInKb = sizeInKb;
    }
}
