package com.github.aytchell.qrgen.renderers;

import com.google.zxing.client.j2se.MatrixToImageConfig;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MarkerRenderer {
    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    protected static final int SIZE_OF_POSITION_MARKER = 7;

    public void render(MatrixToImageConfig colorConfig, BufferedImage img, ImgParameters imgParams) {
        final BufferedImage marker = drawPositionMarkerTemplate(imgParams.getCellSize(), colorConfig);
        applyMarkers(img, marker, imgParams);
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
        final int xCoordOfRightMarker = imgParams.getFirstCellX() +
                (imgParams.getMatrixWidthInCells() - SIZE_OF_POSITION_MARKER) * imgParams.getCellSize();
        final int yCoordOfLowerMarker = xCoordOfRightMarker;

        final Graphics gfx = img.getGraphics();
        gfx.drawImage(marker, imgParams.getFirstCellX(), imgParams.getFirstCellY(), null);
        gfx.drawImage(marker, xCoordOfRightMarker, imgParams.getFirstCellY(), null);
        gfx.drawImage(marker, imgParams.getFirstCellX(), yCoordOfLowerMarker, null);
        gfx.dispose();
    }
}
