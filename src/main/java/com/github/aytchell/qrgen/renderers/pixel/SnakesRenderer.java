package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;

import java.awt.*;

public class SnakesRenderer extends PixelRenderer {
    public SnakesRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        int cellSize = imgParams.getCellSize();

        gfx.fillOval(0, 0, cellSize, cellSize);

        if (context.isNeighbourSet(PixelContext.Direction.W)) {
            final String path = "h 70 v 140 h -70 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (context.isNeighbourSet(PixelContext.Direction.N)) {
            final String path = "h 140 v 70 h -140 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (context.isNeighbourSet(PixelContext.Direction.E)) {
            final String path = "m 70,0 h 70 v 140 h -70 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (context.isNeighbourSet(PixelContext.Direction.S)) {
            final String path = "m 0,70 h 140 v 70 h -140 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }
    }
}
