package com.github.aytchell.qrgen.renderers;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class WaterRenderer extends PixelRenderer {
    public WaterRenderer(ImgParameters imgParams) {
        super(imgParams, true, true);
    }

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        int cellSize = imgParams.getCellSize();
        double cornerRadius = (double)cellSize * 2 / 3;

        gfx.fill(new RoundRectangle2D.Double(0, 0, cellSize, cellSize, cornerRadius, cornerRadius));

        if (hasLeftNeighbour(context)) {
            gfx.fillRect(0, 0, cellSize / 2, cellSize);
        }

        if (hasTopNeighbour(context)) {
            gfx.fillRect(0, 0, cellSize, cellSize / 2);
        }

        if (hasRightNeighbour(context)) {
            gfx.fillRect(cellSize / 2, 0, cellSize / 2, cellSize);
        }

        if (hasBottomNeighbour(context)) {
            gfx.fillRect(0, cellSize / 2, cellSize, cellSize / 2);
        }
    }

    @Override
    protected void renderInactiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        int cellSize = imgParams.getCellSize();
        double cornerRadius = (double)cellSize * 2 / 3;

        final boolean hasLeft = hasLeftNeighbour(context);
        final boolean hasTop = hasTopNeighbour(context);
        final boolean hasRight = hasRightNeighbour(context);
        final boolean hasBottom = hasBottomNeighbour(context);

        final Area corners = new Area(new Rectangle(0, 0, cellSize, cellSize));
        corners.subtract(
                new Area(new RoundRectangle2D.Double(0, 0, cellSize, cellSize, cornerRadius, cornerRadius)));
        gfx.clip(corners);

        if (hasLeft && hasTop && context.isNeighbourSet(PixelContext.Direction.NW)) {
            gfx.fillRect(0, 0, cellSize / 2, cellSize / 2);
        }

        if (hasTop && hasRight && context.isNeighbourSet(PixelContext.Direction.NE)) {
            gfx.fillRect(cellSize / 2, 0, cellSize / 2, cellSize / 2);
        }

        if (hasRight && hasBottom && context.isNeighbourSet(PixelContext.Direction.SE)) {
            gfx.fillRect(cellSize / 2, cellSize / 2, cellSize / 2, cellSize / 2);
        }

        if (hasBottom && hasLeft && context.isNeighbourSet(PixelContext.Direction.SW)) {
            gfx.fillRect(0, cellSize / 2, cellSize / 2, cellSize / 2);
        }
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
}
