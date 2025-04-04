package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.config.ColorConfig;
import com.github.aytchell.qrgen.config.MarkerStyle;
import com.github.aytchell.qrgen.config.PixelStyle;
import com.github.aytchell.qrgen.renderers.marker.MarkerRenderer;
import com.github.aytchell.qrgen.renderers.pixel.PixelRenderer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.*;
import java.awt.geom.AffineTransform;
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

    private PixelStyle pixelStyle;

    public QrCodeRenderer(PixelStyle pixelStyle, MarkerStyle markerStyle) {
        this.writer = new QRCodeWriter();
        this.markerRenderer = MarkerRendererFactory.create(markerStyle);
        this.pixelStyle = pixelStyle;
    }

    public void setMarkerStyle(MarkerStyle markerStyle) {
        this.markerRenderer = MarkerRendererFactory.create(markerStyle);
    }

    public void setPixelStyle(PixelStyle pixelStyle) {
        this.pixelStyle = pixelStyle;
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

        final Graphics2D gfx = img.createGraphics();
        gfx.setComposite(AlphaComposite.Src);
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        gfx.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        gfx.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        gfx.translate(imgParams.getFirstCellX(), imgParams.getFirstCellY());

        final AffineTransform transform = gfx.getTransform();
        renderMatrix(matrix, gfx, imgParams);
        gfx.setTransform(transform);
        markerRenderer.render(gfx, imgParams);

        gfx.dispose();
        return img;
    }

    private void renderMatrix(BitMatrix matrix, Graphics2D gfx, ImgParameters imgParams) {
        PixelRenderer renderer = PixelRendererFactory.generate(pixelStyle, imgParams);
        gfx.setColor(imgParams.getOnColor().asAwtColor());
        applyQrCodePixels(gfx, matrix, renderer, imgParams);
    }

    private void applyQrCodePixels(Graphics2D gfx, BitMatrix matrix, PixelRenderer renderer, ImgParameters imgParams) {
        BitArray top;
        BitArray mid = null;
        BitArray bottom = matrix.getRow(0, null);

        final PositionMarkerDetector detector = new PositionMarkerDetector(matrix.getWidth());

        for (int yCoord = 0; yCoord < matrix.getHeight(); ++yCoord) {
            top = mid;
            mid = bottom;
            bottom = (yCoord >= (matrix.getHeight() - 1)) ? null : matrix.getRow(yCoord + 1, null);
            PixelContext context = new PixelContext(matrix.getWidth(), top, mid, bottom);

            for (int xCoord = 0; xCoord < matrix.getWidth(); ++xCoord) {
                if (!detector.detected(xCoord, yCoord)) {
                    renderer.renderPixel(context, gfx);
                }
                gfx.translate(imgParams.getCellSize(), 0);
                context.shiftRight();
            }

            gfx.translate(-1 * imgParams.getMatrixWidthInCells() * imgParams.getCellSize(), imgParams.getCellSize());
        }
    }

    private BufferedImage drawCanvas(int width, int height, ColorConfig colorConfig) {
        final BufferedImage canvas = new BufferedImage(width, height, colorConfig.determineImageType());

        final Graphics2D gfx = canvas.createGraphics();
        gfx.setColor(colorConfig.getOffColor().asAwtColor());
        gfx.fillRect(0, 0, width, height);
        gfx.dispose();

        return canvas;
    }

    private int getMargin(Map<EncodeHintType, ?> encodingHints) {
        final Integer margin = (Integer) encodingHints.get(EncodeHintType.MARGIN);
        return (margin == null) ? DEFAULT_MARGIN_FROM_ZXING : margin;
    }

    private ImgParameters computeImageParameters(
            int width, int height, BitMatrix matrix, int margin, ColorConfig colorConfig)
            throws WriterException {
        double targetSize = Math.min(width, height);
        double numCellsOfCode = matrix.getWidth();
        double numCellsOfCodeAndMargin = numCellsOfCode + (2 * margin);

        if (targetSize < numCellsOfCodeAndMargin) {
            // A rendered QR code will not fit into the requested boundaries
            throw new WriterException("Requested width/height is too small for generated QR Code");
        }

        int pixelPerCell = (int) Math.floor(targetSize / numCellsOfCodeAndMargin);
        int sizeOfCodeInPixels = (int) (pixelPerCell * numCellsOfCode);
        return new ImgParameters(
                pixelPerCell,
                matrix.getWidth(),
                (width - sizeOfCodeInPixels) / 2,
                (height - sizeOfCodeInPixels) / 2,
                colorConfig);
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
