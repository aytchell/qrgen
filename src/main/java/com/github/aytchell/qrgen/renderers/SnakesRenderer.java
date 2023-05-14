package com.github.aytchell.qrgen.renderers;

import java.awt.*;

public class SnakesRenderer extends PixelRenderer {
    public SnakesRenderer(ImgParameters imgParams) {
        super(imgParams, true, false);
    }

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        int cellSize = imgParams.getCellSize();

        gfx.fillOval(0, 0, cellSize, cellSize);

        if (context.isNeighbourSet(PixelContext.Direction.W)) {
            gfx.fillRect(0, 0, cellSize / 2, cellSize);
        }

        if (context.isNeighbourSet(PixelContext.Direction.N)) {
            gfx.fillRect(0, 0, cellSize, cellSize / 2);
        }

        if (context.isNeighbourSet(PixelContext.Direction.E)) {
            gfx.fillRect(cellSize / 2, 0, cellSize / 2, cellSize);
        }

        if (context.isNeighbourSet(PixelContext.Direction.S)) {
            gfx.fillRect(0, cellSize / 2, cellSize, cellSize / 2);
        }
    }
}
