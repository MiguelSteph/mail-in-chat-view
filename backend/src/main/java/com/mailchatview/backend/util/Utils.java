package com.mailchatview.backend.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class Utils {

    public static List<String> readFileLines(String path) throws IOException {
        return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
    }

    public static String readFile(String path) throws IOException {
        log.info("GOOGLE CREDENTIAL PATH IS " + path);
        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }

    public static String readSecret(String path) {
        String value = "";
        try {
            value = Utils.readFileLines(path).get(0).trim();
        } catch (IOException ex) {
            log.error(String.format("Failed to read secret from of %s", path), ex);
        }
        return value;
    }
}
