package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.common.ImgParameters;
import com.github.aytchell.qrgen.renderers.common.SvgPath2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class PixelRenderer {
    private final ImgParameters imgParams;

    public PixelRenderer(ImgParameters imgParams) {
        this.imgParams = imgParams;
    }

    public void renderPixel(PixelContext context, Graphics2D gfx) {
        if (context.isSet()) {
            renderActiveShape(imgParams, context, gfx);
        } else {
            renderInactiveShape(imgParams, context, gfx);
        }
    }

    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
    }

    protected void renderInactiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
    }

    protected void renderPixelFromSvgPath(ImgParameters imgParams, Graphics2D gfx, String path) {
        double cellSize = imgParams.getCellSize();
        double factor = cellSize / 140.0;

        final AffineTransform transform = gfx.getTransform();
        gfx.scale(factor, factor);
        gfx.fill(SvgPath2D.drawSvgCommand(path));
        gfx.setTransform(transform);
    }
}
