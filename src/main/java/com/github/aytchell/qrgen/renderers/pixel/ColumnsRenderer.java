package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.common.ImgParameters;

import java.awt.*;

public class ColumnsRenderer extends PixelRenderer {
    public ColumnsRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        final boolean hasTop = context.isNeighbourSet(PixelContext.Direction.N);
        final boolean hasBottom = context.isNeighbourSet(PixelContext.Direction.S);
        int cellSize = imgParams.getCellSize();
        int xStart = cellSize / 6;
        int xEnd = cellSize - xStart;

        if (hasTop && hasBottom) {
            gfx.fillRect(xStart, 0, xEnd, cellSize);
        } else {
            if (hasTop) {
                gfx.fillRect(xStart, 0, xEnd, cellSize / 2);
            }

            if (hasBottom) {
                gfx.fillRect(xStart, cellSize / 2, xEnd, cellSize);
            }

            gfx.fillOval(xStart, xStart, xEnd, xEnd);
        }
    }
}
