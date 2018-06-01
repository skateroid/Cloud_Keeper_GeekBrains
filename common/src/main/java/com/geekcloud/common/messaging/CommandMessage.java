package com.geekcloud.common.messaging;

// в объекты этого класса будут заворачиваться команды от пользователя
// с указанием необходимых для исполнения команды параметров (имя файла, папки и т.п.)
public class CommandMessage extends Message {

    //  private static final long serialVersionUID = 5193392663743561680L;

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

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
