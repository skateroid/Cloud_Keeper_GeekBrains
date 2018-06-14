package com.geekcloud.common.messaging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileListMessage_SimpleVersion extends Message {
    private List<File> files;

    public List<File> getFiles() {
        return files;
    }

    public FileListMessage_SimpleVersion(Path path) {
        try {
            files = Files.list(path).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
