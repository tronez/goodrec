package com.goodrec.recipe.domain;

import com.goodrec.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

class FileManager {

    private FileManager() {
    }

    public static byte[] decodeFrom(MultipartFile base64) {
        final byte[] fileContent = getBytes(base64);
        return Base64.getDecoder().decode(fileContent);
    }

    public static byte[] encodeFrom(byte[] imageBytes) {
        return Base64.getEncoder().encode(imageBytes);
    }

    public static byte[] getBytes(MultipartFile file) {

        if (file.isEmpty()) {
            return new byte[0];
        }

        try {
            return file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("Could not get file content. Try again");
        }
    }
}
