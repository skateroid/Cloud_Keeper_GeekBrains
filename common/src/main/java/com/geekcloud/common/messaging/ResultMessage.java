package com.geekcloud.common.messaging;

public class ResultMessage {

    private Result result;

    public ResultMessage (Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}
