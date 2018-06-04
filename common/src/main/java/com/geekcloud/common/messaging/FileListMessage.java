package com.geekcloud.common.messaging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileListMessage extends Message {

    private String currentDirectory; // путь к текущей директории от корневой папки на сервере
    private List <FileInfo> fileList; // список файлов в формате, удобном для отображения в Java FX TableView
    private List<String> fileListString;

    public FileListMessage(Path targetDirectoryAbsolute, Path restrictedDirectoryAbsolute) throws IOException {
        this.currentDirectory = getRelativePath (targetDirectoryAbsolute, restrictedDirectoryAbsolute);
        this.fileList = listFiles (targetDirectoryAbsolute);
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

    public List<String> getFileListString() {
        return fileListString;
    }

    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
    }

    // метод заворачивает все файлы указанной директории в объекты класса FileInfo для отображения в клиенте
    private List<FileInfo> listFiles (Path targetDirectory) throws IOException {
        if (!targetDirectory.isAbsolute()) throw new IOException("Absolute path required");
        if (Files.notExists(targetDirectory, LinkOption.NOFOLLOW_LINKS) || !Files.isDirectory(targetDirectory))
            throw new IOException("Directory doesn't exist");
        List <FileInfo> list = new ArrayList<>();
        Files.newDirectoryStream(targetDirectory).forEach(path -> list.add(new FileInfo(new File(path.toString()))));
        return list;
    }

    // метод возвращает путь до целевой директории, начиная с папки, следующей за запретной директорией
    // (чтобы пользователю показывать только ту информацию, которая относится к его файлам)
    private String getRelativePath (Path targetDirectory, Path parentDirectory) throws IOException {
        if (parentDirectory == null) return targetDirectory.toString();
        if (!targetDirectory.isAbsolute() || !parentDirectory.isAbsolute()) throw new IOException("Absolute paths required");
        if (!Files.isDirectory(parentDirectory)) throw new IOException("Parent is not a directory.");
        if (!targetDirectory.startsWith(parentDirectory)) throw new IOException("Parent directory does not contain the target directory");
        return parentDirectory.relativize(targetDirectory).toString();
    }
}
