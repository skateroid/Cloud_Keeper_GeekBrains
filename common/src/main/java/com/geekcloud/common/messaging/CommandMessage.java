package com.geekcloud.common.messaging;

import java.io.File;

// в объекты этого класса будут заворачиваться команды от пользователя
// с указанием необходимых для исполнения команды параметров (имя файла, папки и т.п.)
public class CommandMessage extends Message {
    public enum Command {
        DELETE,
        DOWNLOAD,
        LIST_FILES,
    }

    private Command command;
    private String addition;
    private File additionFile;

    public Command getCommand() {
        return command;
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

    public String getAddition() {
        return addition;
    }

    public File getAdditionFile() {
        return additionFile;
    }
}
