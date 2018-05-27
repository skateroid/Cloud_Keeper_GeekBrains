package com.geekcloud.common.settings;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface FileStorageConst {

    String ROOT_DIRECTORY_NAME = "geekcloud";
    String USER_DATABASE_NAME = "users";
    String USER_DATABASE_TYPE ="sqlite";

    default Path getRootDirectory() {
        return Paths.get(ROOT_DIRECTORY_NAME);
    }

    default String getDatabaseURL () {
        return "jdbc:" + USER_DATABASE_TYPE + ":" + getRootDirectory().resolve(USER_DATABASE_NAME);
    }
}
