package com.github.aytchell.qrgen.renderers.marker;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundCornersRenderer extends MarkerRenderer {
    @Override
    protected void renderTopLeftMarker(Graphics2D gfx, int cellSize, int onColor, int offColor) {
        final int markerSize = cellSize * SIZE_OF_POSITION_MARKER;
        final int whiteSize = cellSize * (SIZE_OF_POSITION_MARKER - 2);
        final int innerSize = cellSize * (SIZE_OF_POSITION_MARKER - 4);
        final double arc = 2 * cellSize;

        gfx.setColor(new Color(onColor, false));
        gfx.fill(new RoundRectangle2D.Double(0, 0, markerSize, markerSize, 2 * arc, 2 * arc));
        gfx.setColor(new Color(offColor, false));
        gfx.fill(new RoundRectangle2D.Double(cellSize, cellSize, whiteSize, whiteSize, arc, arc));
        gfx.setColor(new Color(onColor, false));
        gfx.fill(new RoundRectangle2D.Double(2 * cellSize, 2 * cellSize, innerSize, innerSize, arc, arc));
    }
}
