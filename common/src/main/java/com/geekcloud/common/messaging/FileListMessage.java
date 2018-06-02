package com.geekcloud.common.messaging;

import java.util.List;

public class FileListMessage extends Message {

    private String currentDirectory; // путь к текущей директории от корневой папки на сервере
    private List <FileInfo> fileList; // список файлов в формате, удобном для отображения в Java FX TableView

    public FileListMessage(String currentDirectory, List<FileInfo> fileList) {
        this.currentDirectory = currentDirectory;
        this.fileList = fileList;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public List<FileInfo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
    }
}
