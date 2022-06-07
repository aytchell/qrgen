package com.github.aytchell.qrgen.renderers;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CirclesRenderer extends CustomRenderer {
    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    private static final int SIZE_OF_POSITION_MARKER = 7;

    @Override
    protected void renderMatrix(BitMatrix matrix, BufferedImage img, ImgParameters imgParams) {
        final BufferedImage dot = drawDotTemplate(imgParams);
        applyQrCodeDots(img, matrix, dot, imgParams);
    }

    private BufferedImage drawDotTemplate(ImgParameters imgParams) {
        BufferedImage img = new BufferedImage(imgParams.cellSize, imgParams.cellSize, BufferedImage.TYPE_INT_ARGB);

        int onColor = imgParams.onColor;

        final Graphics2D gfx = img.createGraphics();
        gfx.setComposite(AlphaComposite.Src);
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setColor(new Color(onColor, true));
        gfx.fillOval(0, 0, imgParams.cellSize, imgParams.cellSize);
        gfx.dispose();

        return img;
    }

    private void applyQrCodeDots(
            BufferedImage img, BitMatrix matrix, BufferedImage circle, ImgParameters imgParams) {
        BitArray row = null;
        int posY = imgParams.firstCellY;
        final Graphics gfx = img.getGraphics();
        final PositionMarkerDetector detector = new PositionMarkerDetector(matrix.getWidth());

        for (int yCoord = 0; yCoord < matrix.getHeight(); ++yCoord) {
            row = matrix.getRow(yCoord, row);
            int posX = imgParams.firstCellX;

            for (int xCoord = 0; xCoord < matrix.getWidth(); ++xCoord) {
                if (row.get(xCoord)) {
                    if (!detector.detected(xCoord, yCoord)) {
                        gfx.drawImage(circle, posX, posY, null);
                    }
                }
                posX += imgParams.cellSize;
            }

            posY += imgParams.cellSize;
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
