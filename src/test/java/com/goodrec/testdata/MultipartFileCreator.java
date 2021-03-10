package com.goodrec.testdata;

import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

public class MultipartFileCreator {

    private MultipartFileCreator() {
    }

    public static MockMultipartFile simpleFile(String name, String contentType, byte[] content) {
        return new MockMultipartFile(name, name, contentType, content);
    }

    public static MockMultipartFile fromFile(String name, String contentType, String filePathName) {

        final byte[] fileBytes = getFileBytes(filePathName);
        return new MockMultipartFile(name, name, contentType, fileBytes);
    }

    private static byte[] getFileBytes(String pathName) {

        final Path imagePath = Paths.get(pathName);
        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(format("Could not read bytes from path: %s", imagePath.toString()), e);
        }
    }
}
