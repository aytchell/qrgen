package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.QrCodeRenderer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class CirclesRenderer implements QrCodeRenderer {
    // as stated by the code of ZXing 3.5.0
    private static final int DEFAULT_MARGIN_FROM_ZXING = 4;

    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    private static final int SIZE_OF_POSITION_MARKER = 7;

    private final QRCodeWriter writer;

    public CirclesRenderer() {
        writer = new QRCodeWriter();
    }

    @Override
    public BufferedImage encodeAndRender(
            String payload, MatrixToImageConfig colorConfig, int width, int height,
            Map<EncodeHintType, ?> encodingHints) throws WriterException {
        final int margin = getMargin(encodingHints);
        final Map<EncodeHintType, Object> hintsCopy = new HashMap<>(encodingHints);
        // ZXing whould only computer the QRCode without any border (margin);
        // the margin is then afterwars applied by this method
        final Integer zero = 0;
        hintsCopy.put(EncodeHintType.MARGIN, zero);

        final BitMatrix matrix = writer.encode(payload, BarcodeFormat.QR_CODE, 1, 1, hintsCopy);
        final ImgParameters imgParams = computeImageParameters(width, height, matrix, margin);

        final BufferedImage img = drawCanvas(width, height, colorConfig.getPixelOffColor());
        final BufferedImage dot = drawDotTemplate(imgParams.circleDiameter, colorConfig);
        applyQrCodeDots(img, matrix, dot, imgParams);
        final BufferedImage marker = drawPositionMarkerTemplate(imgParams.circleDiameter, colorConfig);
        applyMarkers(img, marker, imgParams);

        return img;
    }

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

    private ImgParameters computeImageParameters(int width, int height, BitMatrix matrix, int margin)
            throws WriterException {
        double targetSize = Math.min(width, height);
        double codeSize = matrix.getWidth();
        double codeAndMarginSize = codeSize + 2 * margin;

        if (targetSize < codeAndMarginSize) {
            // A rendered QR code will not fit into the rquested boundaries
            throw new WriterException("Requested width/height is too small for generated QR Code");
        }

        int circleDiameter = (int) Math.floor(targetSize / codeAndMarginSize);
        int allCirclesSize = (int) (circleDiameter * codeSize);
        return new ImgParameters(circleDiameter, matrix.getWidth(),
                (width - allCirclesSize) / 2, (height - allCirclesSize) / 2);
    }

    private BufferedImage drawDotTemplate(int diameter, MatrixToImageConfig config) {
        BufferedImage img = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        int onColor = config.getPixelOnColor();

        final Graphics2D gfx = img.createGraphics();
        gfx.setComposite(AlphaComposite.Src);
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setColor(new Color(onColor, true));
        gfx.fillOval(0, 0, diameter, diameter);
        gfx.dispose();

        return img;
    }

    private void applyQrCodeDots(
            BufferedImage img, BitMatrix matrix, BufferedImage circle, ImgParameters imgParams) {
        BitArray row = null;
        int posY = imgParams.firstCircleY;
        final Graphics gfx = img.getGraphics();
        final PositionMarkerDetector detector = new PositionMarkerDetector(matrix.getWidth());

        for (int yCoord = 0; yCoord < matrix.getHeight(); ++yCoord) {
            row = matrix.getRow(yCoord, row);
            int posX = imgParams.firstCircleX;

            for (int xCoord = 0; xCoord < matrix.getWidth(); ++xCoord) {
                if (row.get(xCoord)) {
                    if (!detector.detected(xCoord, yCoord)) {
                        gfx.drawImage(circle, posX, posY, null);
                    }
                }
                posX += imgParams.circleDiameter;
            }

            posY += imgParams.circleDiameter;
        }
        gfx.dispose();
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
        final int xCoordOfRightMarker = imgParams.firstCircleX +
                (imgParams.sizeInDiameters - SIZE_OF_POSITION_MARKER) * imgParams.circleDiameter;
        final int yCoordOfLowerMarker = xCoordOfRightMarker;

        final Graphics gfx = img.getGraphics();
        gfx.drawImage(marker, imgParams.firstCircleX, imgParams.firstCircleY, null);
        gfx.drawImage(marker, xCoordOfRightMarker, imgParams.firstCircleY, null);
        gfx.drawImage(marker, imgParams.firstCircleX, yCoordOfLowerMarker, null);
        gfx.dispose();
    }

    private static class PositionMarkerDetector {
        private final int xStartOfLeftMarker;
        private final int yStartOfLowerMarker;

        PositionMarkerDetector(int matrixSize) {
            xStartOfLeftMarker = matrixSize - SIZE_OF_POSITION_MARKER;
            yStartOfLowerMarker = matrixSize - SIZE_OF_POSITION_MARKER;
        }

        public boolean detected(int xCoord, int yCoord) {
            if (xCoord < SIZE_OF_POSITION_MARKER) {
                return (yCoord < SIZE_OF_POSITION_MARKER) ||
                        (yCoord >= yStartOfLowerMarker);
            }

            return (xCoord >= xStartOfLeftMarker) &&
                    (yCoord < SIZE_OF_POSITION_MARKER);
        }
    }

    private static class ImgParameters {
        int circleDiameter;
        int sizeInDiameters;
        int firstCircleX;
        int firstCircleY;

        public ImgParameters(int circleDiameter, int sizeInDiameters, int firstX, int firstY) {
            this.circleDiameter = circleDiameter;
            this.sizeInDiameters = sizeInDiameters;
            this.firstCircleX = firstX;
            this.firstCircleY = firstY;
        }
    }
}
