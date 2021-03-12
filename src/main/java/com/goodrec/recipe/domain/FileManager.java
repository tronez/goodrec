package com.goodrec.recipe.domain;

import com.goodrec.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

class FileManager {

    private FileManager() {
    }

    static byte[] getBytes(MultipartFile file) {

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
