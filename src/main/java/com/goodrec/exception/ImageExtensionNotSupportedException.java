package com.goodrec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ImageExtensionNotSupportedException extends RuntimeException {

    public ImageExtensionNotSupportedException() {
        super("Image extension is not supported");
    }

    public ImageExtensionNotSupportedException(String imageExtension) {
        super(String.format("Image extension: %s is not supported", imageExtension));
    }
}
