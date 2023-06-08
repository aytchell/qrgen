package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;
import com.github.aytchell.qrgen.renderers.utils.SvgPath2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class IndependentPixelRenderer extends PixelRenderer {
    public IndependentPixelRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    protected String getSvgPath() {
        // a rectangle which fills the complete cell
        return "h 140 v 140 h -140 z";
    }

    protected void drawActualShape(ImgParameters imgParams, Graphics2D gfx) {
        double cellSize = imgParams.getCellSize();
        double factor = cellSize / 140.0;

        final AffineTransform transform = gfx.getTransform();
        gfx.scale(factor, factor);
        gfx.fill(SvgPath2D.drawSvgCommand(getSvgPath()));
        gfx.setTransform(transform);
    }

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        drawActualShape(imgParams, gfx);
    }
}
