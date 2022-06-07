package com.github.aytchell.qrgen.renderers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.util.Map;

public class DefaultRenderer implements QrCodeRenderer {
    private final QRCodeWriter writer;

    public DefaultRenderer() {
        writer = new QRCodeWriter();
    }

    @Override
    public BufferedImage encodeAndRender(
            String payload, MatrixToImageConfig colorConfig, int width, int height,
            Map<EncodeHintType, ?> encodingHints) throws WriterException {
        final BitMatrix matrix = writer.encode(payload, BarcodeFormat.QR_CODE, width, height, encodingHints);
        return MatrixToImageWriter.toBufferedImage(matrix, colorConfig);
    }
}
