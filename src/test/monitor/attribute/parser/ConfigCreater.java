package monitor.attribute.parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Test class for generation big config file.
 *
 * @author Evgenii Morenkov
 */
public class ConfigCreater {
    public static void main(String[] args) {
        Path configFile = Paths.get("big_config.conf");
        try {
            if (!configFile.toFile().exists()) {
                configFile = Files.createFile(configFile);
            }
        } catch (IOException ex) {
            System.err.println("Error creating file");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                configFile, Charset.defaultCharset())) {
            for (int i = 0; i < 1000; i++) {
                writer.append("STRING ").append(String.valueOf(i)).append(" red ")
                        .append(String.valueOf((int) (Math.random() * 39 + 1)));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            // no logger available.
            e.printStackTrace();
        }
    }
}
