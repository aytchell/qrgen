package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.common.ImgParameters;

import java.awt.*;

public class RectangleRenderer extends MarkerRenderer {
    @Override
    protected void renderTopLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        final int markerSize = cellSize * SIZE_OF_POSITION_MARKER;
        final int whiteSize = cellSize * (SIZE_OF_POSITION_MARKER - 2);
        final int innerSize = cellSize * (SIZE_OF_POSITION_MARKER - 4);

        gfx.setColor(imgParams.getOuterMarkerColorForAwt());
        gfx.fillRect(0, 0, markerSize, markerSize);

        gfx.setColor(imgParams.getOffColorForAwt());
        gfx.fillRect(cellSize, cellSize, whiteSize, whiteSize);

        gfx.setColor(imgParams.getInnerMarkerColorForAwt());
        gfx.fillRect(2 * cellSize, 2 * cellSize, innerSize, innerSize);
    }
}
