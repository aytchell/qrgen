package com.github.aytchell.qrgen.renderers;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class IndependentPixelsRenderer extends CustomRenderer {
    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    private static final int SIZE_OF_POSITION_MARKER = 7;

    @Override
    protected void renderMatrix(BitMatrix matrix, BufferedImage img, ImgParameters imgParams) {
        final BufferedImage tpl = drawPixelTemplate(imgParams);
        applyQrCodePixels(img, matrix, tpl, imgParams);
    }

    protected BufferedImage drawPixelTemplate(ImgParameters imgParams) {
        BufferedImage img = new BufferedImage(
                imgParams.getCellSize(), imgParams.getCellSize(), BufferedImage.TYPE_INT_ARGB);

        int onColor = imgParams.getOnColor();

        final Graphics2D gfx = img.createGraphics();
        gfx.setComposite(AlphaComposite.Src);
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setColor(new Color(onColor, true));
        drawActualShape(imgParams, gfx);
        gfx.dispose();

        return img;
    }

    protected abstract void drawActualShape(ImgParameters imgParams, Graphics2D gfx);

    private void applyQrCodePixels(
            BufferedImage img, BitMatrix matrix, BufferedImage qrPixel, ImgParameters imgParams) {
        BitArray row = null;
        int posY = imgParams.getFirstCellY();
        final Graphics gfx = img.getGraphics();
        final PositionMarkerDetector detector = new PositionMarkerDetector(matrix.getWidth());

        for (int yCoord = 0; yCoord < matrix.getHeight(); ++yCoord) {
            row = matrix.getRow(yCoord, row);
            int posX = imgParams.getFirstCellX();

            for (int xCoord = 0; xCoord < matrix.getWidth(); ++xCoord) {
                if (row.get(xCoord)) {
                    if (!detector.detected(xCoord, yCoord)) {
                        gfx.drawImage(qrPixel, posX, posY, null);
                    }
                }
                posX += imgParams.getCellSize();
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
