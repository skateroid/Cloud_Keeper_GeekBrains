package com.geekcloud.common.messaging;

public class ResultMessage {

    public enum Result {
        OK,
        FAILED
    }

    private Result result;

    public ResultMessage(Result result) {
        this.result = result;
    }
}
