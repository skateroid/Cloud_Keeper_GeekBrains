package com.geekcloud.common.fileprocessing;

import com.geekcloud.common.messaging.FileInfo;
import com.geekcloud.common.messaging.FileListMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    public FileListMessage listFilesToMessage (Path absoluteDirectoryToBeListed, Path restrictedDirectory) throws FileHelperException {
        return new FileListMessage(getRelativePath(absoluteDirectoryToBeListed, restrictedDirectory), listFiles(absoluteDirectoryToBeListed));
    }

    // метод заворачивает все файлы указанной директории в объекты класса FileInfo для отображения в клиенте
    private List<FileInfo> listFiles (Path absoluteDirectory) throws FileHelperException {
        if (!absoluteDirectory.isAbsolute()) throw new FileHelperException("Absolute path required");
        if (Files.notExists(absoluteDirectory, LinkOption.NOFOLLOW_LINKS) || !Files.isDirectory(absoluteDirectory))
            throw new FileHelperException("Directory doesn't exist");
        try {
            List <FileInfo> list = new ArrayList<>();
            Files.newDirectoryStream(absoluteDirectory)
                    .forEach(path -> list.add(new FileInfo(new File (path.toString()))));
            return list;
        } catch (IOException e) {
            throw new FileHelperException(e);
        }
    }

    // метод возвращает путь до целевой директории, начиная с папки, следующей за запретной директорией
    // (чтобы пользователю показывать только ту информацию, которая относится к его файлам)
    private String getRelativePath (Path targetDirectory, Path restrictedDirectory) throws FileHelperException {
        if (!targetDirectory.isAbsolute() || !restrictedDirectory.isAbsolute())
            throw new FileHelperException("Absolute paths required");
        if (!Files.isDirectory(targetDirectory)) throw new FileHelperException("Target path is not a directory.");
        if (!Files.isDirectory(restrictedDirectory)) throw new FileHelperException("Restricted path is not a directory.");
        if (!targetDirectory.startsWith(restrictedDirectory)) throw new FileHelperException("Restricted directory does not contain the target directory");
        return restrictedDirectory.relativize(targetDirectory).toString();
    }

}
