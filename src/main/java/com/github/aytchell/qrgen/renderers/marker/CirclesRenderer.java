package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.ImgParameters;

import java.awt.*;

public class CirclesRenderer extends MarkerRenderer {
    @Override
    protected void renderTopLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        final int outerBlackSize = (cellSize * SIZE_OF_POSITION_MARKER);
        final int whiteSize = cellSize * (SIZE_OF_POSITION_MARKER - 2);
        final int innerBlackSize = cellSize * (SIZE_OF_POSITION_MARKER - 4);

        gfx.setColor(imgParams.getOuterMarkerColorForAwt());
        gfx.fillOval(0, 0, outerBlackSize, outerBlackSize);

        gfx.setColor(imgParams.getOffColorForAwt());
        gfx.fillOval(cellSize, cellSize, whiteSize, whiteSize);

        gfx.setColor(imgParams.getInnerMarkerColorForAwt());
        gfx.fillOval(cellSize * 2, cellSize * 2, innerBlackSize, innerBlackSize);
    }
}
