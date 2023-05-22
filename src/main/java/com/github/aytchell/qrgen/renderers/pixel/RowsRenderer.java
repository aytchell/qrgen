package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;

import java.awt.*;

public class RowsRenderer extends PixelRenderer {
    public RowsRenderer(ImgParameters imgParams) {
        super(imgParams, true, false);
    }

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        final boolean hasLeft = context.isNeighbourSet(PixelContext.Direction.W);
        final boolean hasRight = context.isNeighbourSet(PixelContext.Direction.E);
        int cellSize = imgParams.getCellSize();
        int yStart = cellSize / 6;
        int yEnd = cellSize - yStart;

        if (hasLeft && hasRight) {
            gfx.fillRect(0, yStart, cellSize, yEnd);
        } else {
            if (hasLeft) {
                gfx.fillRect(0, yStart, cellSize / 2, yEnd);
            }

            if (hasRight) {
                gfx.fillRect(cellSize / 2, yStart, cellSize, yEnd);
            }

            gfx.fillOval(yStart, yStart, yEnd, yEnd);
        }
    }
}
