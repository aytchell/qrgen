package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class WaterRenderer extends PixelRenderer {
    public WaterRenderer(ImgParameters imgParams) {
        super(imgParams);
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
        double cornerRadius = Math.ceil((double)cellSize * 2 / 3);
        int halfSize = (int) Math.ceil((double)cellSize / 2);

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
            gfx.fillRect(0, 0, halfSize, halfSize);
        }

        if (hasTop && hasRight && context.isNeighbourSet(PixelContext.Direction.NE)) {
            gfx.fillRect(halfSize, 0, halfSize, halfSize);
        }

        if (hasRight && hasBottom && context.isNeighbourSet(PixelContext.Direction.SE)) {
            gfx.fillRect(halfSize, halfSize, halfSize, halfSize);
        }

        if (hasBottom && hasLeft && context.isNeighbourSet(PixelContext.Direction.SW)) {
            gfx.fillRect(0, cellSize / 2, cellSize / 2, cellSize / 2);
        }

        gfx.setClip(globalClip);
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
