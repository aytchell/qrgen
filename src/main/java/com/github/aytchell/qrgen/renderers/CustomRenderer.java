package com.github.aytchell.qrgen.renderers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomRenderer implements QrCodeRenderer {
    // as stated by the code of ZXing 3.5.0
    protected static final int DEFAULT_MARGIN_FROM_ZXING = 4;

    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    protected static final int SIZE_OF_POSITION_MARKER = 7;

    private final QRCodeWriter writer;

    public CustomRenderer() {
        writer = new QRCodeWriter();
    }

    @Override
    public BufferedImage encodeAndRender(
            String payload, MatrixToImageConfig colorConfig, int width, int height,
            Map<EncodeHintType, ?> encodingHints) throws WriterException {
        final int margin = getMargin(encodingHints);
        final Map<EncodeHintType, Object> hintsCopy = new HashMap<>(encodingHints);
        // ZXing would only compute the QRCode without any border (margin);
        // the margin is then afterward applied by this method
        final Integer zero = 0;
        hintsCopy.put(EncodeHintType.MARGIN, zero);

        final BitMatrix matrix = writer.encode(payload, BarcodeFormat.QR_CODE, 1, 1, hintsCopy);
        final ImgParameters imgParams = computeImageParameters(width, height, matrix, margin, colorConfig);
        final BufferedImage img = drawCanvas(width, height, colorConfig.getPixelOffColor());

        renderMatrix(matrix, img, imgParams);

        final BufferedImage marker = drawPositionMarkerTemplate(imgParams.cellSize, colorConfig);
        applyMarkers(img, marker, imgParams);

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
            int width, int height, BitMatrix matrix, int margin, MatrixToImageConfig colorConfig)
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
                colorConfig.getPixelOnColor(), colorConfig.getPixelOffColor());
    }

    private BufferedImage drawPositionMarkerTemplate(int diameter, MatrixToImageConfig config) {
        final int markerSize = diameter * SIZE_OF_POSITION_MARKER;
        BufferedImage img = new BufferedImage(markerSize, markerSize, BufferedImage.TYPE_INT_ARGB);

        final int whiteSize = diameter * (SIZE_OF_POSITION_MARKER - 2);
        final int innerSize = diameter * (SIZE_OF_POSITION_MARKER - 4);

        int onColor = config.getPixelOnColor();
        int offColor = config.getPixelOffColor();

        final Graphics2D gfx = img.createGraphics();
        gfx.setColor(new Color(onColor, false));
        gfx.fillRect(0, 0, markerSize, markerSize);
        gfx.setColor(new Color(offColor, false));
        gfx.fillRect(diameter, diameter, whiteSize, whiteSize);
        gfx.setColor(new Color(onColor, false));
        gfx.fillRect(2 * diameter, 2 * diameter, innerSize, innerSize);
        gfx.dispose();

        return img;
    }

    private void applyMarkers(BufferedImage img, BufferedImage marker, ImgParameters imgParams) {
        final int xCoordOfRightMarker = imgParams.firstCellX +
                (imgParams.matrixWidthInCells - SIZE_OF_POSITION_MARKER) * imgParams.cellSize;
        final int yCoordOfLowerMarker = xCoordOfRightMarker;

        final Graphics gfx = img.getGraphics();
        gfx.drawImage(marker, imgParams.firstCellX, imgParams.firstCellY, null);
        gfx.drawImage(marker, xCoordOfRightMarker, imgParams.firstCellY, null);
        gfx.drawImage(marker, imgParams.firstCellX, yCoordOfLowerMarker, null);
        gfx.dispose();
    }
}

