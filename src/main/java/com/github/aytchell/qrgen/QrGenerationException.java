package com.github.aytchell.qrgen;

public class QrGenerationException extends Exception {
    public QrGenerationException(String message) {
        super(message);
    }

    public QrGenerationException(String message, Exception e) {
        super(message, e);
    }
}
