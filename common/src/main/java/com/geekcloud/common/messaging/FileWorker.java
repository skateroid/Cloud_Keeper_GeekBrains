package com.geekcloud.common.messaging;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FileWorker {
    public static final int PART_SIZE = 256;

    public static void sendFileFromClient(Path path, ObjectEncoderOutputStream out, ProgressBar bar) {
        try {
            byte[] data = Files.readAllBytes(path);
            int partsCount = data.length / PART_SIZE;
            if (data.length % PART_SIZE != 0) partsCount++;
            if (bar != null) {
                Platform.runLater(() -> {
                    bar.setVisible(true);
                    bar.setManaged(true);
                });
            }
            for (int i = 0; i < partsCount; i++) {
                int startPos = i * PART_SIZE;
                int endPos = (i + 1) * PART_SIZE;
                if (endPos > data.length) endPos = data.length;
                if (bar != null) {
                    final double progress = (double) i / partsCount;
                    Platform.runLater(() -> bar.setProgress(progress));
                }
                DataTransferMessage transferMessage = new DataTransferMessage(path.getFileName().toString(), Arrays.copyOfRange(data, startPos, endPos), partsCount, i);
                out.writeObject(transferMessage);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bar != null) {
                Platform.runLater(() -> {
                    bar.setVisible(false);
                    bar.setManaged(false);
                });
            }
        }
    }

    public static void sendFileFromServer(Path path, Channel out) {
        try {
            byte[] data = Files.readAllBytes(path);
            int partsCount = data.length / PART_SIZE;
            if (data.length % PART_SIZE != 0) partsCount++;

            for (int i = 0; i < partsCount; i++) {
                int startPos = i * PART_SIZE;
                int endPos = (i + 1) * PART_SIZE;
                if (endPos > data.length) endPos = data.length;
                DataTransferMessage transferMessage = new DataTransferMessage(path.getFileName().toString(), Arrays.copyOfRange(data, startPos, endPos), partsCount, i);
                ChannelFuture future = out.writeAndFlush(transferMessage);
                future.await();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
