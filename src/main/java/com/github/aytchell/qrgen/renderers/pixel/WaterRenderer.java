package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.common.ImgParameters;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class WaterRenderer extends PixelRenderer {
    public WaterRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    private static boolean hasLeftNeighbour(PixelContext context) {
        return context.isNeighbourSet(PixelContext.Direction.W);
    }

    private static boolean hasTopNeighbour(PixelContext context) {
        return context.isNeighbourSet(PixelContext.Direction.N);
    }

    private static boolean hasRightNeighbour(PixelContext context) {
        return context.isNeighbourSet(PixelContext.Direction.E);
    }

    private static boolean hasBottomNeighbour(PixelContext context) {
        return context.isNeighbourSet(PixelContext.Direction.S);
    }

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        int cellSize = imgParams.getCellSize();
        double cornerRadius = (double) cellSize * 2 / 3;

        gfx.fill(new RoundRectangle2D.Double(0, 0, cellSize, cellSize, cornerRadius, cornerRadius));

        if (hasLeftNeighbour(context)) {
            final String path = "h 70 v 140 h -70 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (hasTopNeighbour(context)) {
            final String path = "h 140 v 70 h -140 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (hasRightNeighbour(context)) {
            final String path = "m 70,0 h 70 v 140 h -70 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (hasBottomNeighbour(context)) {
            final String path = "m 0,70 h 140 v 70 h -140 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }
    }

    @Override
    protected void renderInactiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        int cellSize = imgParams.getCellSize();
        double cornerRadius = Math.ceil((double) cellSize * 2 / 3);

        final boolean hasLeft = hasLeftNeighbour(context);
        final boolean hasTop = hasTopNeighbour(context);
        final boolean hasRight = hasRightNeighbour(context);
        final boolean hasBottom = hasBottomNeighbour(context);
        final Shape globalClip = gfx.getClip();

        final Area corners = new Area(new Rectangle(0, 0, cellSize, cellSize));
        corners.subtract(
                new Area(new RoundRectangle2D.Double(0, 0, cellSize, cellSize, cornerRadius, cornerRadius)));
        gfx.clip(corners);

        if (hasLeft && hasTop && context.isNeighbourSet(PixelContext.Direction.NW)) {
            final String path = "h 70 v 70 h -70 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (hasTop && hasRight && context.isNeighbourSet(PixelContext.Direction.NE)) {
            final String path = "m 70,0 h 70 v 70 h -70 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (hasRight && hasBottom && context.isNeighbourSet(PixelContext.Direction.SE)) {
            final String path = "m 70,70 h 70 v 70 h -70 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        if (hasBottom && hasLeft && context.isNeighbourSet(PixelContext.Direction.SW)) {
            final String path = "m 0,70 h 70 v 70 h -70 z";
            renderPixelFromSvgPath(imgParams, gfx, path);
        }

        gfx.setClip(globalClip);
    }
}
