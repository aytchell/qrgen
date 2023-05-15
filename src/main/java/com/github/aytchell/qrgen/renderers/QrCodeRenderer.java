package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.ColorConfig;
import com.github.aytchell.qrgen.MarkerStyle;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class QrCodeRenderer {
    // as stated by the code of ZXing 3.5.0
    protected static final int DEFAULT_MARGIN_FROM_ZXING = 4;

    private final QRCodeWriter writer;
    private MarkerRenderer markerRenderer;

    public QrCodeRenderer(MarkerStyle markerStyle) {
        this.writer = new QRCodeWriter();
        this.markerRenderer = MarkerFactory.create(markerStyle);
    }

    public void setMarkerStyle(MarkerStyle markerStyle) {
        this.markerRenderer = MarkerFactory.create(markerStyle);
    }

    public BufferedImage encodeAndRender(
            String payload, ColorConfig colorConfig, int width, int height,
            Map<EncodeHintType, ?> encodingHints) throws WriterException {
        final int margin = getMargin(encodingHints);
        final Map<EncodeHintType, Object> hintsCopy = new HashMap<>(encodingHints);
        // ZXing would only compute the QRCode without any border (margin);
        // the margin is then afterward applied by this method
        final Integer zero = 0;
        hintsCopy.put(EncodeHintType.MARGIN, zero);

        final BitMatrix matrix = writer.encode(payload, BarcodeFormat.QR_CODE, 1, 1, hintsCopy);
        final ImgParameters imgParams = computeImageParameters(width, height, matrix, margin, colorConfig);
        final BufferedImage img = drawCanvas(width, height, colorConfig.getOffColor());

        renderMatrix(matrix, img, imgParams);
        markerRenderer.render(img, imgParams);

        return img;
    }

    protected abstract void renderMatrix(BitMatrix matrix, BufferedImage img, ImgParameters imgParams);

    private BufferedImage drawCanvas(int width, int height, int offColor) {
        final BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        final Graphics2D gfx = canvas.createGraphics();
        gfx.setColor(new Color(offColor, false));
        gfx.fillRect(0, 0, width, height);
        gfx.dispose();

        return canvas;
    }

    private int getMargin(Map<EncodeHintType, ?> encodingHints) {
        final Integer margin = (Integer)encodingHints.get(EncodeHintType.MARGIN);
        return (margin == null) ? DEFAULT_MARGIN_FROM_ZXING : margin;
    }

    private ImgParameters computeImageParameters(
            int width, int height, BitMatrix matrix, int margin, ColorConfig colorConfig)
            throws WriterException {
        double targetSize = Math.min(width, height);
        double codeSize = matrix.getWidth();
        double codeAndMarginSize = codeSize + 2 * margin;

        if (targetSize < codeAndMarginSize) {
            // A rendered QR code will not fit into the requested boundaries
            throw new WriterException("Requested width/height is too small for generated QR Code");
        }

        int circleDiameter = (int) Math.floor(targetSize / codeAndMarginSize);
        int allCirclesSize = (int) (circleDiameter * codeSize);
        return new ImgParameters(circleDiameter, matrix.getWidth(),
                (width - allCirclesSize) / 2, (height - allCirclesSize) / 2,
                colorConfig.getOnColor(), colorConfig.getOffColor(), colorConfig.getMarkerColor());
    }
}
