package com.geekcloud.common.messaging;

public class ResultMessage {

    public static enum Result {
        OK,
        FAILED
    }

    private Result result;

    public ResultMessage(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

}
