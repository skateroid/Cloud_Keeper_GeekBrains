package com.geekcloud.common.messaging;

import java.io.File;

// в объекты этого класса будут заворачиваться команды от пользователя
// с указанием необходимых для исполнения команды параметров (имя файла, папки и т.п.)
public class CommandMessage extends Message {
    public static enum Command {
        // нужно прикинуть, какие ещё действия могут понадобиться пользователю
        CREATE,
        DELETE,
        DOWNLOAD,
        UPLOAD,
        LIST_FILES,
        RENAME
    }

    private Command command;
    private String fileName;
    private String newFileName;
    private String directoryName;
    private String newDirectoryName;
    private String addition;
    private File additionFile;

    public Command getCommand() {
        return command;
    }

    public String getFileName() {
        return fileName;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getNewDirectoryName() {
        return newDirectoryName;
    }

    public CommandMessage setCommand(Command command) {
        this.command = command;
        return this;
    }

    public CommandMessage(Command command) {
        this.command = command;
    }

    public CommandMessage(Command command, String addition) {
        this.command = command;
        this.addition = addition;
    }

    public CommandMessage(Command command, File additionFile) {
        this.command = command;
        this.additionFile = additionFile;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAddition() {
        return addition;
    }

    public File getAdditionFile() {
        return additionFile;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void setNewDirectoryName(String newDirectoryName) {
        this.newDirectoryName = newDirectoryName;
    }


}
