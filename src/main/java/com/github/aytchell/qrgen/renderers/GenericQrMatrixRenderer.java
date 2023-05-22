package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.MarkerStyle;
import com.github.aytchell.qrgen.PixelStyle;
import com.github.aytchell.qrgen.renderers.pixel.PixelRenderer;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GenericQrMatrixRenderer extends QrCodeRenderer {
    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    private static final int SIZE_OF_POSITION_MARKER = 7;

    @Setter
    private PixelStyle pixelStyle;

    public GenericQrMatrixRenderer(PixelStyle pixelStyle, MarkerStyle markerStyle) {
        super(markerStyle);
        this.pixelStyle = pixelStyle;
    }

    @Override
    protected void renderMatrix(BitMatrix matrix, BufferedImage img, ImgParameters imgParams) {
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
