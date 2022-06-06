package com.github.aytchell.qrgen;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface QrCodeRenderer {
    BufferedImage encodeAndRender(
            String payload, MatrixToImageConfig colorConfig, int width, int height,
            Map<EncodeHintType, ?> encodingHints) throws WriterException;
}
