/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.clementlevallois.functions.mapsofscience;

/**
 *
 * @author ChatGPT and LEVALLOIS
 */
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StringQueueProcessor implements Runnable {

    private static final int MAX_BYTES_PER_WRITE = 1024 * 1024; // 1 MB
    private static  Duration FLUSH_INTERVAL = Duration.ofSeconds(10);

    private final Path outputFilePath;
    private final ConcurrentLinkedQueue<String> stringQueue;
    private boolean jsonFilteringRunning = true;

    public StringQueueProcessor(Path outputFilePath, ConcurrentLinkedQueue<String> stringQueue, int flushIntervalInSeconds) {
        this.outputFilePath = outputFilePath;
        this.stringQueue = stringQueue;
        FLUSH_INTERVAL = Duration.ofSeconds(flushIntervalInSeconds);
    }

    public void stop() {
        jsonFilteringRunning = false;
    }

    @Override
    public void run() {
        Instant lastFlushTime = Instant.now();
        try (FileChannel outputChannel = FileChannel.open(outputFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND)) {            
            while (jsonFilteringRunning) {
                String string = stringQueue.poll();
                if (string == null) {
                    // No more items in the queue, sleep for a bit before checking again
                    Thread.sleep(100);
                    continue;
                }
                ByteBuffer byteBuffer = ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
                do {
                    int bytesToWrite = Math.min(byteBuffer.remaining(), MAX_BYTES_PER_WRITE);
                    byteBuffer.limit(byteBuffer.position() + bytesToWrite);
                    outputChannel.write(byteBuffer);
                    byteBuffer.compact();
                } while (byteBuffer.position() > 0);

                Instant now = Instant.now();
                if (Duration.between(lastFlushTime, now).compareTo(FLUSH_INTERVAL) >= 0) {
                    outputChannel.force(true);
                    lastFlushTime = now;
                }
            }
        } catch (IOException | InterruptedException e) {
            // Handle exceptions as appropriate for your application
        }
    }
}
