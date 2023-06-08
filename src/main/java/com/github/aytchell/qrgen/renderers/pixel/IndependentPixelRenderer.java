package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;

import java.awt.*;

public abstract class IndependentPixelRenderer extends PixelRenderer {
    public IndependentPixelRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    protected String getSvgPath() {
        // a rectangle which fills the complete cell
        return "h 140 v 140 h -140 z";
    }

    protected void drawActualShape(ImgParameters imgParams, Graphics2D gfx) {
        renderPixelFromSvgPath(imgParams, gfx, getSvgPath());
    }

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        drawActualShape(imgParams, gfx);
    }
}
