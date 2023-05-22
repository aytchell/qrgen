package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.ColorConfig;
import com.github.aytchell.qrgen.MarkerStyle;
import com.github.aytchell.qrgen.PixelStyle;
import com.github.aytchell.qrgen.renderers.marker.MarkerRenderer;
import com.github.aytchell.qrgen.renderers.pixel.PixelRenderer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class QrCodeRenderer {
    // as stated by the code of ZXing 3.5.0
    private static final int DEFAULT_MARGIN_FROM_ZXING = 4;

    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    private static final int SIZE_OF_POSITION_MARKER = 7;

    private final QRCodeWriter writer;
    private MarkerRenderer markerRenderer;

    @Setter
    private PixelStyle pixelStyle;

    public QrCodeRenderer(PixelStyle pixelStyle, MarkerStyle markerStyle) {
        this.writer = new QRCodeWriter();
        this.markerRenderer = MarkerRendererFactory.create(markerStyle);
        this.pixelStyle = pixelStyle;
    }

    public void setMarkerStyle(MarkerStyle markerStyle) {
        this.markerRenderer = MarkerRendererFactory.create(markerStyle);
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
        final BufferedImage img = drawCanvas(width, height, colorConfig);

        renderMatrix(matrix, img, imgParams);
        markerRenderer.render(img, imgParams);

        return img;
    }

    private void renderMatrix(BitMatrix matrix, BufferedImage img, ImgParameters imgParams) {
        PixelRenderer renderer = PixelRendererFactory.generate(pixelStyle, imgParams);
        applyQrCodePixels(img, matrix, renderer, imgParams);
    }

    private void applyQrCodePixels(
            BufferedImage img, BitMatrix matrix, PixelRenderer renderer, ImgParameters imgParams) {
        BitArray top;
        BitArray mid = null;
        BitArray bottom = matrix.getRow(0, null);

        int posY = imgParams.getFirstCellY();
        final Graphics gfx = img.getGraphics();
        final PositionMarkerDetector detector = new PositionMarkerDetector(matrix.getWidth());

        for (int yCoord = 0; yCoord < matrix.getHeight(); ++yCoord) {
            int posX = imgParams.getFirstCellX();
            top = mid;
            mid = bottom;
            bottom = (yCoord >= (matrix.getHeight() - 1)) ? null : matrix.getRow(yCoord + 1, null);
            PixelContext context = new PixelContext(matrix.getWidth(), top, mid, bottom);

            for (int xCoord = 0; xCoord < matrix.getWidth(); ++xCoord) {
                if (!detector.detected(xCoord, yCoord)) {
                    final Image qrPixel = renderer.renderPixel(context);
                    if (qrPixel != null) {
                        gfx.drawImage(qrPixel, posX, posY, null);
                    }
                }
                posX += imgParams.getCellSize();
                context.shiftRight();
            }

            posY += imgParams.getCellSize();
        }
        gfx.dispose();
    }

    private BufferedImage drawCanvas(int width, int height, ColorConfig colorConfig) {
        final BufferedImage canvas = new BufferedImage(width, height, colorConfig.determineImageType());

        final Graphics2D gfx = canvas.createGraphics();
        gfx.setColor(new Color(colorConfig.getRawOffColor(), false));
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
                colorConfig.getRawOnColor(), colorConfig.getRawOffColor(), colorConfig.getRawMarkerColor());
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
}
