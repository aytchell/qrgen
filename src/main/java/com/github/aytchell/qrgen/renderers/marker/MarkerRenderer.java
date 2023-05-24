package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.ImgParameters;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class MarkerRenderer {
    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    protected static final int SIZE_OF_POSITION_MARKER = 7;

    public void render(BufferedImage img, Graphics2D gfx, ImgParameters imgParams) {
        final int markerOffset =
                (imgParams.getMatrixWidthInCells() - SIZE_OF_POSITION_MARKER) * imgParams.getCellSize();

        final int cellSize = imgParams.getCellSize();
        final int onColor = imgParams.getMarkerColor();
        final int offColor = imgParams.getOffColor();

        renderTopLeftMarker(gfx, cellSize, onColor, offColor);
        gfx.translate(markerOffset, 0);
        renderTopRightMarker(gfx, cellSize, onColor, offColor);
        gfx.translate(-markerOffset, markerOffset);
        renderBottomLeftMarker(gfx, cellSize, onColor, offColor);
    }

    protected abstract void renderTopLeftMarker(Graphics2D gfx, int cellSize, int onColor, int offColor);

    protected void renderTopRightMarker(Graphics2D gfx, int cellSize, int onColor, int offColor) {
        renderTopLeftMarker(gfx, cellSize, onColor, offColor);
    }

    protected void renderBottomLeftMarker(Graphics2D gfx, int cellSize, int onColor, int offColor) {
        renderTopLeftMarker(gfx, cellSize, onColor, offColor);
    }
}
