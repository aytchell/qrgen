package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.ImgParameters;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class MarkerRenderer {
    // regardless of the size of the payload or the error correction level
    // the position markers will always be seven pixels high and wide
    // (tested with ZXing 3.5.0)
    public static final int SIZE_OF_POSITION_MARKER = 7;

    public void render(Graphics2D gfx, ImgParameters imgParams) {
        final int markerOffset =
                (imgParams.getMatrixWidthInCells() - SIZE_OF_POSITION_MARKER) * imgParams.getCellSize();

        final int cellSize = imgParams.getCellSize();

        renderTopLeftMarker(gfx, cellSize, imgParams);
        gfx.translate(markerOffset, 0);
        renderTopRightMarker(gfx, cellSize, imgParams);
        gfx.translate(-markerOffset, markerOffset);
        renderBottomLeftMarker(gfx, cellSize, imgParams);
    }

    protected abstract void renderTopLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams);

    protected void renderTopRightMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderTopLeftMarker(gfx, cellSize, imgParams);
    }

    protected void renderBottomLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderTopLeftMarker(gfx, cellSize, imgParams);
    }

    protected void renderRotated(
            double angle, Graphics2D gfx, int cellSize, ImgParameters imgParams, SingleMarkerRenderer renderer) {
        final AffineTransform transform = gfx.getTransform();
        final int halfMarkerSize = (cellSize * SIZE_OF_POSITION_MARKER) / 2;
        gfx.translate(halfMarkerSize, halfMarkerSize);
        gfx.rotate(angle);
        gfx.translate(-halfMarkerSize, -halfMarkerSize);
        renderer.renderMarker(gfx, cellSize, imgParams);
        gfx.setTransform(transform);
    }

    @FunctionalInterface
    protected interface SingleMarkerRenderer {
        void renderMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams);
    }
}
